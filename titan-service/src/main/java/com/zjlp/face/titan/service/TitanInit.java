package com.zjlp.face.titan.service;

import com.thinkaurelius.titan.core.Cardinality;
import com.thinkaurelius.titan.core.Multiplicity;
import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.schema.SchemaAction;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.thinkaurelius.titan.core.util.TitanCleanup;
import com.thinkaurelius.titan.graphdb.database.management.ManagementSystem;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

public class TitanInit {

    private TitanConPool pool = new TitanConPool(1);

    public TitanConPool getPool() {
        return pool;
    }

    public void setPool(TitanConPool pool) {
        this.pool = pool;
    }

    private static final Logger logger = LoggerFactory.getLogger(TitanInit.class);

    /**
     * 清空图,删除所有的顶点、边和索引
     */
    public void cleanTitanGraph() {
        TitanGraph graph = pool.getTitanGraph();
        graph.close();
        TitanCleanup.clear(graph);
        logger.info("清空tatin中的所有数据");
    }

    public void createVertexLabel(TitanGraph graph) {
        TitanManagement mgmt = graph.openManagement();
        mgmt.makePropertyKey("userId").dataType(String.class).cardinality(Cardinality.SINGLE).make();
        mgmt.makeVertexLabel("person").make();
        mgmt.commit();
    }

    public void setGlobalOfflineOption(String key, Object value) {
        pool.killAllTitanInstances();
        TitanGraph graph = pool.getTitanGraph();
        TitanManagement mgmt = graph.openManagement();
        //设置GLOBAL_OFFLINE属性
        mgmt.set(key, value);
        mgmt.commit();
    }

    public void setDBcacheTime() {
        setGlobalOfflineOption("cache.db-cache-time", 4200000);
    }

    public void createEdgeLabel(TitanGraph graph) {
        TitanManagement mgmt = graph.openManagement();
        mgmt.makeEdgeLabel("knows").multiplicity(Multiplicity.SIMPLE).make();
        mgmt.commit();
    }

    public void userIdUnique(TitanGraph graph) {

        graph.tx().rollback();  //Never create new indexes while a transaction is active
        TitanManagement mgmt = graph.openManagement();
        PropertyKey userId = mgmt.getPropertyKey("userId");
        mgmt.buildIndex("userIdUnique", Vertex.class).addKey(userId).unique().buildCompositeIndex();
        mgmt.commit();
        //Wait for the index to become available
        try {
            ManagementSystem.awaitGraphIndexStatus(graph, "userIdUnique")
                    .timeout(60, ChronoUnit.MINUTES) // set timeout to 60 min
                    .call();
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
        }
        //Reindex the existing data
        mgmt = graph.openManagement();
        try {
            mgmt.updateIndex(mgmt.getGraphIndex("userIdUnique"), SchemaAction.REINDEX).get();
        } catch (InterruptedException e) {
            logger.error("InterruptedException", e);
        } catch (ExecutionException e) {
            logger.error("ExecutionException", e);
        }
        mgmt.commit();
    }

    public void run() {
        cleanTitanGraph();
        TitanGraph graph = pool.getTitanGraph();
        createVertexLabel(graph);
        createEdgeLabel(graph);
        pool.closeTitanGraph();
    }

}

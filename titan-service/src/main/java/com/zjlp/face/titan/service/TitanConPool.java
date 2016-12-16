package com.zjlp.face.titan.service;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.zjlp.face.titan.common.utils.ConfigUtil;

import java.util.Iterator;

public class TitanConPool {

    private int poolSize;
    private TitanGraph[] graphs;

    public TitanConPool(int poolSize) {
        this.poolSize = poolSize;
        this.graphs = new TitanGraph[poolSize];
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    //= Integer.valueOf(ConfigUtil.get("titan-con-pool-size"));


    public void closeTitanGraph() {
        for (TitanGraph graph : graphs) {
            if (graph != null && graph.isOpen())
                graph.close();
        }
    }

    public TitanGraph getTitanGraph(String userId) {
        return getTitanGraph(Math.abs((userId+"101").hashCode()));
    }

    public TitanGraph getTitanGraph() {
        return getTitanGraph((int) System.currentTimeMillis());
    }

    public TitanGraph getTitanGraph(int j) {
        int i = Math.abs(j % poolSize);
        if (graphs[i] == null || graphs[i].isClosed()) {
            graphs[i] = TitanFactory.open(ConfigUtil.get("titan-cassandra"));
        }
        return graphs[i];
    }

    public void killAllTitanInstances() {
        TitanGraph graph = getTitanGraph();
        TitanManagement mgmt = graph.openManagement();
        //更改GLOBAL_OFFLINE属性前需要先关闭其他Titan实例
        Iterator<String> it = mgmt.getOpenInstances().iterator();
        while (it.hasNext()) {
            String nxt = it.next();
            if (!nxt.contains("current"))
                mgmt.forceCloseInstance(nxt);
        }
        mgmt.commit();
        graph.close();
    }

}

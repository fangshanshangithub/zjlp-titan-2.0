package com.zjlp.face.titan.migration

import com.zjlp.face.titan.common.utils.ConfigUtil
import org.apache.spark.sql.SparkSession

object Spark {
  def session(): SparkSession = return SparkSession.builder()
    .config("spark.app.name", ConfigUtil.get("spark.app.name"))
    .config("spark.master", ConfigUtil.get("spark.master"))
    .config("spark.sql.shuffle.partitions", ConfigUtil.get("spark.sql.shuffle.partitions"))
    .config("spark.executor.memory", ConfigUtil.get("spark.executor.memory"))
    .config("spark.executor.cores", ConfigUtil.get("spark.executor.cores"))
    .config("spark.driver.memory", ConfigUtil.get("spark.driver.memory"))
    .config("spark.driver.cores", ConfigUtil.get("spark.driver.cores"))
    .config("spark.default.parallelism", ConfigUtil.get("spark.default.parallelism"))
    .config("spark.jars", ConfigUtil.get("spark.jars"))
    .config("es.nodes", ConfigUtil.get("es.nodes"))
    .config("es.port", ConfigUtil.get("es.port"))
    .config("pushdown",ConfigUtil.get("pushdown"))
    .config("strict", ConfigUtil.get("strict"))
    .getOrCreate()
}


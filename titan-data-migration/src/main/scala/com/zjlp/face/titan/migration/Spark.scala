package com.zjlp.face.titan.migration

import org.apache.spark.sql.SparkSession

object Spark {
  def session(): SparkSession = {
    if(MigrationConfig.get("spark.debug").toBoolean) {
      return SparkSession.builder()
        .config("spark.app.name","ZJLP Titan Data Migration")
        .config("spark.master", "local[6]")
        .config("spark.sql.shuffle.partitions",4)
        .config("spark.executor.memory", "500m")
        .config("spark.executor.cores", 2)
        .config("spark.driver.memory", "1g")
        .config("spark.driver.cores", 1)
        .config("spark.default.parallelism", 4)
        .config("es.nodes", "192.168.175.11")
        .config("es.port", 9200)
        .config("pushdown", true)
        .config("strict", true)
        .getOrCreate()
    } else {
      return SparkSession.builder().getOrCreate()
    }
  }
}


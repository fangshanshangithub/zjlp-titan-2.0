package com.zjlp.face.titan.migration

import java.util.Properties

import org.apache.spark.sql.SparkSession

class JdbcDF(val spark: SparkSession) {
  val MYSQL_CONNECTION_URL = MigrationConfig.get("mysql.connection.url") //"jdbc:mysql://192.168.175.12:3306/spark_search"
  val connectionProperties = new Properties()
  connectionProperties.put("user", MigrationConfig.get("mysql.connection.user"))
  connectionProperties.put("password", MigrationConfig.get("mysql.connection.password"))

  def cacheRelationFromMysql = {

    spark.read.jdbc(MYSQL_CONNECTION_URL, " (select rosterID as rosterId,username,loginAccount,userID as userId from view_ofroster where sub=3 and userID is not null and username != loginAccount) as tb", "rosterId", 1, getMaxRosterId(),  spark.conf.get("spark.sql.shuffle.partitions","60").toInt , connectionProperties)
    .createOrReplaceTempView("relation")
  }

  private def getMaxRosterId(): Long = {
    return spark.read.jdbc(MYSQL_CONNECTION_URL, "(select max(rosterId) from view_ofroster) as max_roster_id", connectionProperties)
      .map(r => r(0).toString.toLong).collect()(0)
  }

}

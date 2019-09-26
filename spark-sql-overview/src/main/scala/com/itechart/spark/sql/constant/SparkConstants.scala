package com.itechart.spark.sql.constant

object SparkConstants {

  val awsAccessKeyId = ""
  val awsSecretAccessKey = ""
  val redshiftDBName = ""
  val redshiftUserId = ""
  val redshiftPassword = ""
  val redshiftUrl = ""
  val redshiftJdbcURL = s"jdbc:redshift://$redshiftUrl/$redshiftDBName?user=$redshiftUserId&password=$redshiftPassword"

}

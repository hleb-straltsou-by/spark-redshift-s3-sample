package com.itechart.spark.sql

import com.itechart.spark.sql.constant.SparkConstants._
import org.apache.spark.sql.SparkSession

object CreateSparkRedshiftConnector {

  def main(args: Array[String]): Unit = {

    val jdbcURL = s"jdbc:redshift://$redshiftUrl/$redshiftDBName?user=$redshiftUserId&password=$redshiftPassword"

    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Spark Redshift App")
      .config("spark.hadoop.fs.s3a.access.key", awsAccessKeyId)
      .config("spark.hadoop.fs.s3a.secret.key", awsSecretAccessKey)
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    val tempS3Dir = "s3a://redshift-training-01/"

    val usersDF = spark.read
      .format("com.databricks.spark.redshift")
      .option("url", jdbcURL)
      .option("tempdir", tempS3Dir)
      .option("dbtable", "USERS_SOURCE_ALPHA")
      .option("forward_spark_s3_credentials", "true")
      .load()

    usersDF.show()

    spark.stop()
  }

}
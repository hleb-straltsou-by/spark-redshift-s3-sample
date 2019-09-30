package com.itechart.spark.sql.utils

import com.itechart.spark.sql.constant.SparkConstants.{awsAccessKeyId, awsSecretAccessKey, redshiftJdbcURL}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object RedshiftUtils {

  def getSparkSession(masterUrl: String, appName: String, awsAccessKeyId: String, awsSecretAccessKey: String): SparkSession = {
    SparkSession.builder()
      .master(masterUrl)
      .appName(appName)
      .config("spark.hadoop.fs.s3a.access.key", awsAccessKeyId)
      .config("spark.hadoop.fs.s3a.secret.key", awsSecretAccessKey)
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()
  }

  def getDF(spark: SparkSession, tempS3Dir: String, query: String): DataFrame = {
     spark.read
      .format("com.databricks.spark.redshift")
      .option("url", redshiftJdbcURL)
      .option("tempdir", tempS3Dir)
      .option("query", query)
      .option("forward_spark_s3_credentials", "true")
      .load()
  }

  def saveDFToS3(df: DataFrame, s3Dir: String): Unit = {
    df.write.save(s3Dir)
  }

  def saveDFToRedshift(df: DataFrame, table: String, tempS3Dir: String): Unit = {
    df.write
      .format("com.databricks.spark.redshift")
      .option("url", redshiftJdbcURL)
      .option("dbtable", table)
      .option("tempdir", tempS3Dir)
      .option("forward_spark_s3_credentials", "true")
      .mode(SaveMode.Append)
      .save()
  }

}

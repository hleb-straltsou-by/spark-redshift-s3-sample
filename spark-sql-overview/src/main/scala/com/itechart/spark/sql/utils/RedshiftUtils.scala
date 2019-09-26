package com.itechart.spark.sql.utils

import com.itechart.spark.sql.constant.SparkConstants.redshiftJdbcURL
import org.apache.spark.sql.{DataFrame, SparkSession}

object RedshiftUtils {

  def getDF(spark: SparkSession, tempS3Dir: String, query: String): DataFrame = {
     spark.read
      .format("com.databricks.spark.redshift")
      .option("url", redshiftJdbcURL)
      .option("tempdir", tempS3Dir)
      .option("query", query)
      .option("forward_spark_s3_credentials", "true")
      .load()
  }

}

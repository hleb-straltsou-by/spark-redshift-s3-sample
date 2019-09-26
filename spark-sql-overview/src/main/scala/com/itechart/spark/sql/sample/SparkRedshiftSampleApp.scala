package com.itechart.spark.sql.sample

import com.itechart.spark.sql.constant.SparkConstants._
import org.apache.spark.sql.functions._
import com.itechart.spark.sql.utils.RedshiftUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkRedshiftSampleApp {

  def main(args:Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .master("local")
      .appName("Spark Redshift App")
      .config("spark.hadoop.fs.s3a.access.key", awsAccessKeyId)
      .config("spark.hadoop.fs.s3a.secret.key", awsSecretAccessKey)
      .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
      .getOrCreate()

    val tempS3Dir = "s3a://redshift-training-01/"

    val queryForUsersAlphaSource = "SELECT * FROM USERS_SOURCE_ALPHA"
    val usersAlphaSourceDF = RedshiftUtils.getDF(spark, tempS3Dir, queryForUsersAlphaSource)
    usersAlphaSourceDF.show

    val queryForUsersBetaSource = "SELECT * FROM USERS_SOURCE_BETA"
    val usersBetaSourceDF = RedshiftUtils.getDF(spark, tempS3Dir, queryForUsersBetaSource)
    usersBetaSourceDF.show

    val schemaA = usersAlphaSourceDF.schema.toSet
    val schemaB = usersBetaSourceDF.schema.toSet

    val missingFields = schemaA.diff(schemaB)
    var extendedDF: DataFrame = usersBetaSourceDF
    for (field <- missingFields) {
      extendedDF = extendedDF.withColumn(field.name, expr("null"))
    }

    val resultDF = usersAlphaSourceDF.union(extendedDF)
    resultDF.foreach(user => println(user))

    spark.stop()
  }



}

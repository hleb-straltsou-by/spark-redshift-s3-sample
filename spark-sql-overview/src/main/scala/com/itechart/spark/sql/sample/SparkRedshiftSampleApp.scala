package com.itechart.spark.sql.sample

import com.itechart.spark.sql.constant.SparkConstants._
import org.apache.spark.sql.functions._
import com.itechart.spark.sql.utils.RedshiftUtils
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkRedshiftSampleApp {

  def main(args:Array[String]): Unit = {

    val masterUrl = "local"
    val appName = "Spark Redshift Sample"
    val spark = RedshiftUtils.getSparkSession(masterUrl, appName, awsAccessKeyId, awsSecretAccessKey)

    val tempS3Dir = "s3a://redshift-training-01/temp"

    val queryForUsersAlphaSource = "SELECT * FROM USERS_SOURCE_ALPHA"
    val usersAlphaSourceDF = RedshiftUtils.getDF(spark, tempS3Dir, queryForUsersAlphaSource)

    val queryForUsersBetaSource = "SELECT * FROM USERS_SOURCE_BETA"
    val usersBetaSourceDF = RedshiftUtils.getDF(spark, tempS3Dir, queryForUsersBetaSource)

    val schemaA = usersAlphaSourceDF.schema.toSet
    val schemaB = usersBetaSourceDF.schema.toSet

    val missingFields = schemaA.diff(schemaB)
    var extendedDF: DataFrame = usersBetaSourceDF
    for (field <- missingFields) {
      extendedDF = extendedDF.withColumn(field.name, expr("null"))
    }

    val resultDF = usersAlphaSourceDF.union(extendedDF)
    //resultDF.foreach(user => println(user))
    resultDF.show

    resultDF.write
      .mode(SaveMode.Overwrite)
      .format("csv")
      .save("output")

    val outputS3Dir = "s3a://redshift-training-01/output"
    RedshiftUtils.saveDFToS3(resultDF, outputS3Dir)

    val saveTable = "USERS_RESULT"
    RedshiftUtils.saveDFToRedshift(resultDF, saveTable, tempS3Dir)

    spark.stop()
  }



}

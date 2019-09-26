package com.itechart.spark.sql.domain

case class UserAlpha
(
  userId: Int,
  userName: String,
  firstName: String,
  lastName: String,
  city: String,
  state: String,
  email: String,
  phone: String,
  likeSports: Boolean,
  likeTheatre: Boolean,
  likeConcerts: Boolean,
  likeJazz: Boolean,
  likeClassical: Boolean,
  likeOpera: Boolean,
  likeRock: Boolean,
  likeVegas: Boolean,
  likeBroadway: Boolean,
  likeMusicals: Boolean
)

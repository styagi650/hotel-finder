package com.test.agoda.dao

trait ApiKeyDao {
  def create(apiKey:String, permits:Int): Unit
  def acquire(apiKey: String): Boolean
}

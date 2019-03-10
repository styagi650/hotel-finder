package com.test.agoda.dao

import java.util.concurrent.ConcurrentHashMap

import com.test.agoda.business.CustomRateLimiter
import com.test.agoda.exceptions.KeyNotFoundException

trait ApiKeyDao {
  def createOrUpdate(apiKey:String, permits:Int): Unit
  def acquire(apiKey: String): Boolean
}

package com.test.agoda.dao

import java.util.concurrent.ConcurrentHashMap

import com.test.agoda.business.CustomRateLimiter
import com.test.agoda.exceptions.{KeyAlreadyExistsException, KeyNotFoundException}

class InMemoryApiKeyDao extends ApiKeyDao {
  private var keyToRateLimiter: ConcurrentHashMap[String, CustomRateLimiter] = new ConcurrentHashMap()

  override def create(apiKey:String, permits:Int): Unit = {
    if(keyToRateLimiter.containsKey(apiKey)) {
      throw new KeyAlreadyExistsException("Api key is already registered. Please create some other key")
    }
    keyToRateLimiter.put(apiKey, new CustomRateLimiter(apiKey, permits))
  }

  override def acquire(apiKey: String): Boolean = {
    if(!keyToRateLimiter.containsKey(apiKey)) {
      throw new KeyNotFoundException("Api Key : " + apiKey + " is not registered")
    }
    keyToRateLimiter.get(apiKey).tryAcquire()
  }
}

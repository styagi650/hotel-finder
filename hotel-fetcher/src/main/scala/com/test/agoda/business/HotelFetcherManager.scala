package com.test.agoda.business


import com.test.agoda.dao.{ApiKeyDao, HotelDao}
import com.test.agoda.entity.Hotel
import com.test.agoda.exceptions.{KeyAlreadyExistsException, KeyNotFoundException, RateLimitExhaustedException}

class HotelFetcherManager(val hotelsDao: HotelDao, val apiKeyDao: ApiKeyDao) {

  def getHotelsForCity(apiKey: String, city: String, sortType: String): Seq[Hotel] = {
    hotelsDao.getHotelsSortByPrice(city, sortType)
  }

  def create(apiKey: String, permits: Int): Unit = {
    try {
      apiKeyDao.create(apiKey, permits)
    } catch {
      case ex: KeyAlreadyExistsException => throw ex
    }
  }

  def validateRequest(apiKey: String): Unit = {
    try {
      if(!apiKeyDao.acquire(apiKey)) {
        throw new RateLimitExhaustedException("Rate limit Exhausted. You are suspended for 5 minutes")
      }
    }
    catch {
      case ex : KeyNotFoundException => throw ex
    }
  }

}

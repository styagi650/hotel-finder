package com.test.agoda.business

import com.test.agoda.dao.HotelDao
import com.test.agoda.entity.Hotel
import com.test.agoda.exceptions.RateLimitExhaustedException

class HotelsInfoFetcher (val hotelsDao: HotelDao, val requestsValidator: RequestsValidator) {

  def getHotelsForCity(apiKey: String, city: String, sortType: String): Seq[Hotel] = {
    try {
      requestsValidator.validateRequest(apiKey)
    } catch {
      case ex: RateLimitExhaustedException  => throw ex
    }
    hotelsDao.getHotelsSortByPrice(city, sortType)
  }

}

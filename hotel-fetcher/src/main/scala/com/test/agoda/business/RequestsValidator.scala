package com.test.agoda.business

import com.test.agoda.exceptions.RateLimitExhaustedException

class RequestsValidator () {
    var keyToRateLimiter:Map[String, CustomRateLimiter] = Map()
    def validateRequest(apiKey: String): Unit = {
      if(!keyToRateLimiter.contains(apiKey)) {
        keyToRateLimiter = keyToRateLimiter + (apiKey -> new CustomRateLimiter(apiKey, 2))
      }
      if(!keyToRateLimiter(apiKey).tryAcquire()) {
        throw new RateLimitExhaustedException("Rate limit Exhausted. You are suspended for 5 minutes")
      }
    }
}

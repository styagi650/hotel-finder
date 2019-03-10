package com.test.agoda.business

import java.util.concurrent.ConcurrentHashMap
import com.test.agoda.exceptions.RateLimitExhaustedException

class RequestsValidator () {
    var keyToRateLimiter: ConcurrentHashMap[String, CustomRateLimiter] = new ConcurrentHashMap

    def validateRequest(apiKey: String): Unit = {
      keyToRateLimiter.putIfAbsent(apiKey, new CustomRateLimiter(apiKey, 2))
      if(!keyToRateLimiter.get(apiKey).tryAcquire()) {
        throw new RateLimitExhaustedException("Rate limit Exhausted. You are suspended for 5 minutes")
      }
    }
}

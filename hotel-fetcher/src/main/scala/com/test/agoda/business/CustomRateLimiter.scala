package com.test.agoda.business

import java.util.concurrent.TimeUnit

import scala.collection.mutable
import scala.concurrent.duration.Duration

class CustomRateLimiter(key: String, maxPermits:Int) {

  val duration: Long = Duration(1, TimeUnit.SECONDS).toMillis
  val suspendedDuration: Long = Duration(20, TimeUnit.SECONDS).toMillis
  var requests:mutable.TreeSet[Long] = mutable.TreeSet()
  var suspended:Long = -1

  def tryAcquire(): Boolean = synchronized {
    val currentTime = System.currentTimeMillis()
      if(isSuspended(currentTime)) return false
      requests = requests.from(currentTime - duration)
      if (requests.size >= maxPermits) {
        suspended = currentTime
        return false
      }  else {
        requests.add(currentTime)
        return true
      }
  }

  private def isSuspended(currentTime : Long): Boolean = {
    if(suspended == -1) {
      return false
    }
    if (suspended + suspendedDuration < currentTime) {
      suspended = -1
      return false
    }
    true
  }
}

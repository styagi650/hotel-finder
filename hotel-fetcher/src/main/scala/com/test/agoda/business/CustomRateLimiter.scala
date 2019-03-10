package com.test.agoda.business

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import com.test.agoda.exceptions.RateLimitExhaustedException

import scala.collection.{SortedMap, mutable}
import scala.concurrent.duration.Duration

class CustomRateLimiter(val key: String, var maxPermits:Int = 10, val suspensionTime :Int = 5, val timeUnit: TimeUnit = TimeUnit.MINUTES) {

  private val duration: Long = Duration(1, TimeUnit.SECONDS).toMillis
  private val suspendedDuration: Long = Duration(suspensionTime, timeUnit).toMillis
  private var tasks:SortedMap[Long, Int] = mutable.SortedMap()
  private var permits = 0
  private var suspended:Long = -1

  def tryAcquire(): Boolean = synchronized {
    val currentTime = System.currentTimeMillis()
    //Release permits for tasks which are not in window
    tasks.filterKeys(_ <= (currentTime - duration)).foreach((x) => {
      permits = permits - x._2
    })

    //Update tasks for the ones in window
    tasks = tasks.filterKeys(_ > (currentTime - duration))

    if(isSuspended(currentTime)) {
      return false
    }

    var acquired = false

    //Suspend if num of tasks exceeds maxPermits
    if (permits == maxPermits) {
      suspended = currentTime
    } else {
      if(!tasks.contains(currentTime)) {
        tasks = tasks + (currentTime -> 0)
      }
      tasks = tasks + (currentTime -> (tasks(currentTime) + 1))
      permits = permits + 1
      acquired = true
    }

    acquired
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

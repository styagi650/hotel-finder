package scala

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executors, Future, TimeUnit}

import com.test.agoda.business.CustomRateLimiter
import org.scalatest.FunSuite

class CustomRateLimiterTest extends FunSuite {
  test("rateLimiterTest") {
    val rateLimiter:CustomRateLimiter = new CustomRateLimiter("test", 5, 5, TimeUnit.SECONDS)
    val executorService = Executors.newFixedThreadPool(4)
    val count = new AtomicInteger
    var futures:List[Future[_]] = List()

    for( i <- 1 to 10){
      futures = futures :+ executorService.submit(new Runnable() {
        override def run(): Unit = {
          val acquired = rateLimiter.tryAcquire()
          if(acquired) count.set(count.get() + 1)
        }
      })

    }

    futures.foreach(x => while(!x.isDone){})

    assert(count.get() == 5)

    assert(!rateLimiter.tryAcquire())

    Thread.sleep(5000)

    assert(rateLimiter.tryAcquire())

  }

}

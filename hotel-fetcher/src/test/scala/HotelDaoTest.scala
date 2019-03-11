package scala

import com.test.agoda.business.HotelFetcherManager
import com.test.agoda.dao.{ApiKeyDao, HotelDao}
import com.test.agoda.entity.{Hotel, Room}
import org.mockito.Mockito
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite}

class HotelDaoTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  test("test sort by price") {
    val hotelDaoMock = Mockito.mock(classOf[HotelDao])
    val apiKeyDaoMock = Mockito.mock(classOf[ApiKeyDao])

    var hotels = Seq[Hotel]()

    for(i <- 1 to 3) {
        hotels = hotels :+  Hotel("city", i, Room.DELUXE, i*100.0)
    }

    Mockito.when(hotelDaoMock.getHotelForCity("city")) thenReturn hotels
    Mockito.when(hotelDaoMock.getHotelsSortByPrice("city", "asc")) thenCallRealMethod()
    Mockito.when(hotelDaoMock.getHotelsSortByPrice("city", "desc")) thenCallRealMethod()

    val hotelFetcherManager = new HotelFetcherManager(hotelDaoMock, apiKeyDaoMock)

    assert(hotels == hotelFetcherManager.getHotelsForCity("city", "asc"))
    assert(hotels.reverse == hotelFetcherManager.getHotelsForCity("city", "desc"))

    Mockito.verify(hotelDaoMock).getHotelsSortByPrice("city", "asc")
    Mockito.verify(hotelDaoMock).getHotelsSortByPrice("city", "desc")

  }

}

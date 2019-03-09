package  com.test.agoda.dao


import scala.io.Source
import com.test.agoda.entity._

class HotelDao {
  var hotels:Map[Int, Hotel] = Map()
  var cityToHotelsIndex:Map[String, Seq[Int]] = Map()

  def init(): Unit = {
    val lines : Iterator[String] = Source.fromURL(getClass.getResource("/hoteldb.csv")).getLines
    lines.foreach(line => {
        val data = line.split(",")
        val hotelId = data(1).toInt
        val city = data(0)
        val hotel = Hotel(city, hotelId, Room.withName(data(2)), data(3).toDouble)
        hotels = hotels + (hotelId -> hotel)

        if (!cityToHotelsIndex.contains(city)) {
          cityToHotelsIndex = cityToHotelsIndex + (city -> List())
        }
        var hotelsInCity: Seq[Int] = cityToHotelsIndex(city)
        hotelsInCity = hotelsInCity :+ hotelId
        cityToHotelsIndex = cityToHotelsIndex + (city -> hotelsInCity)
      })
  }

  def getHotelById(hotelId: Int): Hotel = {
    this.hotels(hotelId)
  }

  def getHotelForCity(city: String) : Seq[Hotel] = {
      this.cityToHotelsIndex(city).map(this.getHotelById).toList
  }

//  def getHotelsSortByPrice(city: String, sortType: String = "asc"): Seq[Hotel] = {
//    val hotels = getHotelForCity(city)
//    sortType match {
//      case "asc" =>
//        hotels.s(new HotelsDao#PriceAscendingsort)
//        break //todo: break is not supported
//      case "desc" =>
//        hotels.sort(new HotelsDao#PriceDescendingsort)
//        break //todo: break is not supported
//    }
//    hotels
//  }
//
//  private[test] class PriceAscendingsort extends Nothing {
//    private[test] val eps = 1e-6
//
//    // Used for sorting in ascending order of
//    // roll number
//    def compare(a: Nothing, b: Nothing): Int = {
//      val diff = a.price - b.price
//      if (diff < eps) return -1
//      1
//    }
//  }
//
//  private[test] class PriceDescendingsort extends Nothing {
//    private[test] val eps = 1e-6
//
//    // roll name
//    def compare(a: Nothing, b: Nothing): Int = {
//      val diff = a.price - b.price
//      if (diff > eps) return -1
//      1
//    }
//  }
}

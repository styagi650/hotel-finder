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

  private def getHotelForCity(city: String) : Seq[Hotel] = {
      this.cityToHotelsIndex(city).map(this.getHotelById).toList
  }

  def getHotelsSortByPrice(city: String, sortType: String): Seq[Hotel] = {
    var hotels = getHotelForCity(city)
    sortType match {
      case "asc" =>
        hotels = hotels.sortWith(priceAscendingsort)
      case "desc" =>
        hotels = hotels.sortWith(priceDescendingsort)
      case default =>
        new IllegalArgumentException("Invalid sort Type")
    }
    hotels
  }

  private def priceAscendingsort (hotel1 : Hotel, hotel2 : Hotel) : Boolean = {
      hotel1.price < hotel2.price
  }

  private def priceDescendingsort (hotel1 : Hotel, hotel2 : Hotel) : Boolean = {
    hotel1.price > hotel2.price
  }

}

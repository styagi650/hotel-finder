package  com.test.agoda.dao

import com.test.agoda.entity._

abstract class HotelDao {

  def getHotelForCity(city : String): Seq[Hotel]

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

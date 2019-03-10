package com.test.agoda.dao

import com.test.agoda.entity.{Hotel, Room}

import scala.io.Source

class CsvHotelDao(fileName : String) extends HotelDao {
  private var hotels:Map[Int, Hotel] = Map()
  private var cityToHotelsIndex:Map[String, Seq[Int]] = Map()

  override def init(): Unit = {
    val lines : Iterator[String] = Source.fromURL(getClass.getResource("/" + fileName)).getLines
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

  private def getHotelById(hotelId: Int): Hotel = {
    this.hotels(hotelId)
  }

  override def getHotelForCity(city: String) : Seq[Hotel] = {
    this.cityToHotelsIndex(city).map(this.getHotelById).toList
  }
}

package com.test.agoda.entity

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration

class RoomType extends TypeReference[Room.type]

case class Hotel (city: String, hotelId: Int, @JsonScalaEnumeration(classOf[RoomType]) roomType: Room.Room, price : Double) {
}


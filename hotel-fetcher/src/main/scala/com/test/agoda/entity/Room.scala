package com.test.agoda.entity

import com.test.agoda.entity

object Room extends Enumeration {
  type Room = Value

  val DELUXE: entity.Room.Value = Value("Deluxe")
  val SUPERIOR: entity.Room.Value = Value("Superior")
  val SWEET_SUITE: entity.Room.Value = Value("Sweet Suite")

}


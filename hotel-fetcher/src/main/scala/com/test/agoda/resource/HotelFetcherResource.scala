package com.test.agoda.resource

import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import javax.ws.rs.{GET, Path, PathParam, Produces}

import com.test.agoda.dao.HotelDao

@Path("/hotel")
class HotelFetcherResource (val hotelDao: HotelDao) {

    @GET
    @Path("/{city}")
    @Produces(Array(APPLICATION_JSON))
    def getHotelsForCity(@PathParam("city") city : String): Response = {
        Response.ok.entity(hotelDao.getHotelForCity(city)).build()
    }
}

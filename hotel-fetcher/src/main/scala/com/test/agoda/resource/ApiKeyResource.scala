package com.test.agoda.resource

import javax.annotation.Nonnull
import javax.ws.rs._
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

import com.test.agoda.business.HotelFetcherManager
import com.test.agoda.exceptions.KeyAlreadyExistsException

@Path("apiKey")
class ApiKeyResource (val hotelFetcherManager: HotelFetcherManager) {

  @PUT
  @Path("create/{apiKey}")
  @Produces(Array(APPLICATION_JSON))
  def generateKey(@PathParam("apiKey") @Nonnull apiKey:String, @QueryParam("permits") @DefaultValue("10") permits:Int): Response = {
    var response:Response = null
    try {
      hotelFetcherManager.create(apiKey, permits)
      response = Response.ok.entity("Success").build()

    } catch {
      case ex : KeyAlreadyExistsException =>
        response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
    }
    response
  }

}

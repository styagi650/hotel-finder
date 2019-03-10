package com.test.agoda.resource

import javax.annotation.Nonnull
import javax.ws.rs._
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

import com.test.agoda.business.HotelFetcherManager
import com.test.agoda.exceptions.RateLimitExhaustedException

@Path("apiKey")
class ApiKeyResource (val hotelFetcherManager: HotelFetcherManager) {

  @PUT
  @Path("createOrUpdate/{apiKey}")
  @Produces(Array(APPLICATION_JSON))
  def generateKey(@PathParam("apiKey") @Nonnull apiKey:String, @QueryParam("permits") @DefaultValue("10") permits:Int): Response = {
    hotelFetcherManager.createOrUpdateApiKeyEntry(apiKey, permits)
    Response.ok.entity("Success").build()
  }

}

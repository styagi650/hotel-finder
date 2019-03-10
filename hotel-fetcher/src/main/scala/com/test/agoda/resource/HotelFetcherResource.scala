package com.test.agoda.resource

import javax.annotation.Nonnull
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import javax.ws.rs._

import com.test.agoda.business.HotelFetcherManager
import com.test.agoda.exceptions.{KeyNotFoundException, RateLimitExhaustedException}

@Path("/hotel")
class HotelFetcherResource (val hotelsInfoFetcher: HotelFetcherManager) {

    @GET
    @Path("/{city}")
    @Produces(Array(APPLICATION_JSON))
    def getHotelsForCity(@PathParam("city") city : String, @QueryParam("sortType") @DefaultValue("asc") sortType : String,
                         @QueryParam("apiKey") @Nonnull apiKey: String): Response = {

        var response:Response = null
        try {
            hotelsInfoFetcher.validateRequest(apiKey)
            val hotels = hotelsInfoFetcher.getHotelsForCity(apiKey, city, sortType)
            response = Response.ok.entity(hotels).build()

        } catch {
            case ex : RateLimitExhaustedException =>
                response = Response.status(429).entity(ex.getMessage).build()
            case ex : KeyNotFoundException =>
                response = Response.status(400).entity(ex.getMessage).build()
        }
        response
    }
}

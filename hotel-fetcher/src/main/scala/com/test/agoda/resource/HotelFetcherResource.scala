package com.test.agoda.resource

import javax.annotation.Nonnull
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import javax.ws.rs._

import com.test.agoda.business.HotelsInfoFetcher
import com.test.agoda.entity.Hotel
import com.test.agoda.exceptions.RateLimitExhaustedException

@Path("/hotel")
class HotelFetcherResource (val hotelsInfoFetcher: HotelsInfoFetcher) {

    @GET
    @Path("/{city}")
    @Produces(Array(APPLICATION_JSON))
    def getHotelsForCity(@PathParam("city") city : String, @QueryParam("sortType") @DefaultValue("asc") sortType : String,
                         @QueryParam("apiKey") @Nonnull apiKey: String): Response = {

        var response:Response = null
        try {
            val hotels = hotelsInfoFetcher.getHotelsForCity(apiKey, city, sortType)
            response = Response.ok.entity(hotels).build()

        } catch {
            case ex : RateLimitExhaustedException =>
                response = Response.status(429).entity(ex.getMessage).build()
        }
        response
    }
}

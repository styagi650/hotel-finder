package com.test.agoda

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.test.agoda.dao.{ApiKeyDao, CsvHotelDao, InMemoryApiKeyDao}
import com.test.agoda.resource.{ApiKeyResource, HotelFetcherResource}
import io.dropwizard.Application
import io.dropwizard.setup.Environment
import com.test.agoda.business.HotelFetcherManager

class HotelFetcherApplication extends Application[HotelFetcherConfig] {
  override def getName: String = "Hotel_fetching-Application"

  override def run(t: HotelFetcherConfig, env: Environment): Unit = {
    val hotelDao = new CsvHotelDao("hoteldb.csv")
    val apiKeyDao = new InMemoryApiKeyDao
    hotelDao.init()
    val hotelsInfoFetcher = new HotelFetcherManager(hotelDao, apiKeyDao)
    env.jersey().register(new HotelFetcherResource(hotelsInfoFetcher))
    env.jersey().register(new ApiKeyResource(hotelsInfoFetcher))
    env.jersey.register(jacksonJaxbJsonProvider)
  }

  private def jacksonJaxbJsonProvider: JacksonJaxbJsonProvider = {
    val provider = new JacksonJaxbJsonProvider()
    val objectMapper = new ObjectMapper()
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.registerModule(new JodaModule)
    objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    provider.setMapper(objectMapper)
    provider
  }

}

object Main {
  def main(args: Array[String]): Unit = {
    new HotelFetcherApplication().run(args: _*)
  }
}


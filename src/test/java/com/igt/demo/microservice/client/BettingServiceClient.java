package com.igt.demo.microservice.client;

import com.igt.demo.microservice.betting.*;
import feign.*;
import feign.gson.*;
import feign.okhttp.*;

public interface BettingServiceClient {

   @RequestLine("POST /betting/capture")
   @Headers({"Accept: application/json", "Content-Type: application/json"})
   long capture(Bet bet);

   @RequestLine("GET /betting/betCount")
   @Headers({"Content-Type: application/json"})
   int betCount();


   static BettingServiceClient client(String url) {
      return Feign.builder()
         .client(new OkHttpClient())
         .encoder(new GsonEncoder())
         .decoder(new GsonDecoder())
         .target(BettingServiceClient.class, url);
   }
}

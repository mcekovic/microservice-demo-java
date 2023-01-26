package com.igt.demo.microservice;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import com.igt.demo.microservice.betting.*;
import com.igt.demo.microservice.client.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class BetCaptureServiceET {

   private static final int BET_COUNT = 1000;
   private static final Random rnd = new Random();

   @Test
   void placeSomeRandomBets() {
      var client = BettingServiceClient.client("http://localhost:8080");

      for (int i = 0; i < BET_COUNT; i++) {
         var bet = makeBet(i);

         var betId = client.capture(bet);

         assertThat(betId).isNotZero();
      }
   }

   private static Bet makeBet(int i) {
      long playerId = 12345L + i;
      var unitStake = BigDecimal.valueOf(1 + rnd.nextInt(10));
      var legCount = 1 + rnd.nextInt(5);
      var legs = IntStream.range(0, legCount)
         .mapToObj(legIndex -> {
            long selectionId = 123456L + legIndex;
            var price = BigDecimal.valueOf(1.0 + 0.5 * rnd.nextInt(20));
            return new BetLeg(selectionId, price);
         })
         .toList();
      var unitSize = 1 + rnd.nextInt(legCount);
      return new Bet(playerId, unitStake, legs, unitSize);
   }
}

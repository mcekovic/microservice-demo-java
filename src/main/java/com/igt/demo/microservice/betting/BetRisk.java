package com.igt.demo.microservice.betting;

import java.math.*;

public record BetRisk(
   int legCount,
   long unitCount,
   BigDecimal totalStake,
   BigDecimal totalReturn
) {

   public BetRisk(Bet bet) {
      this(bet.legCount(), bet.unitCount(), bet.totalStake(), bet.totalReturn());
   }

   public BetRisk add(BetRisk betRisk) {
      return new BetRisk(
         legCount + betRisk.legCount,
         unitCount + betRisk.unitCount,
         totalStake.add(betRisk.totalStake),
         totalReturn.add(betRisk.totalReturn)
      );
   }

   public BigDecimal betPrice() {
      return totalReturn.divide(totalStake, RoundingMode.HALF_EVEN);
   }
}

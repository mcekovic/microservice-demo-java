package com.igt.demo.microservice.betting;

import java.math.*;

public record BetRisk(
   Bet bet,
   long unitCount,
   BigDecimal totalStake,
   BigDecimal totalReturn
) {
   public BetRisk(Bet bet) {
      this(bet, bet.unitCount(), bet.totalStake(), bet.totalReturn());
   }
}

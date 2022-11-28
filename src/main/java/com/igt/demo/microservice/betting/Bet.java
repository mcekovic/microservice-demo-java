package com.igt.demo.microservice.betting;

import java.math.*;
import java.util.*;

import static com.google.common.base.Preconditions.*;
import static org.paukov.combinatorics3.Generator.*;

public record Bet(
   long playerId,
   BigDecimal unitStake,
   List<BetLeg> legs,
   Integer unitSize
) {

   public Bet {
      checkArgument(checkNotNull(unitStake).signum() > 0, "Stake must be positive");
      checkArgument(!checkNotNull(legs).isEmpty(), "Legs must not be empty");
      checkArgument(unitSize == null || unitSize <= legs.size(), "Unit size must be less or equal to leg count");
   }

   public int legCount() {
      return legs.size();
   }

   public long unitCount() {
      return unitSize == null ? 1 : combination(legs).simple(unitSize).stream().count();
   }

   public BigDecimal totalStake() {
      return unitStake.multiply(BigDecimal.valueOf(unitCount()));
   }

   public BigDecimal totalReturn() {
      return combination(legs).simple(unitSize).stream()
         .map(unitLegs -> cumulativePrice(unitLegs).multiply(unitStake))
         .reduce(BigDecimal::add)
         .orElseThrow();
   }

   private static BigDecimal cumulativePrice(List<BetLeg> legs) {
      return legs.stream()
         .map(BetLeg::price)
         .reduce(BigDecimal::multiply)
         .orElseThrow();
   }
}

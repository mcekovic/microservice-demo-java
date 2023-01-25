package com.igt.demo.microservice.betting;

import java.math.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.igt.demo.microservice.tools.*;
import io.micrometer.core.annotation.*;
import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service @Slf4j
public class BettingService {

   private final Map<Long, BetRisk> betRisks;
   private final AtomicLong lastBetId;
   private final Counter betCounter;
   private final Counter betLegCounter;
   private final Counter unitCounter;
   private final DistributionSummary totalStakeSummary;
   private final DistributionSummary totalReturnSummary;

   @Autowired
   public BettingService(MeterRegistry meterRegistry) {
      betRisks = new ConcurrentHashMap<>();
      lastBetId = new AtomicLong();
      betCounter = meterRegistry.counter("bet.count");
      betLegCounter = meterRegistry.counter("bet.leg.count");
      unitCounter = meterRegistry.counter("unit.count");
      meterRegistry.gauge("average.price", List.of(), this, BettingService::averagePrice);
      totalStakeSummary = DistributionSummary.builder("stake.total.amount")
         .maximumExpectedValue(10000.0)
         .publishPercentileHistogram().register(meterRegistry);
      totalReturnSummary = DistributionSummary.builder("return.total.amount")
         .publishPercentiles(0.01, 0.05, 0.1, 0.9, 0.95, 0.99)
         .register(meterRegistry);
   }

   @Timed @Traced
   public long capture(Bet bet) {
      var betId = lastBetId.incrementAndGet();
      var betRisk = new BetRisk(bet);
      betRisks.put(betId, betRisk);
      updateMetrics(betRisk);
      return betId;
   }

   private void updateMetrics(BetRisk betRisk) {
      betCounter.increment();
      betLegCounter.increment(betRisk.legCount());
      unitCounter.increment(betRisk.unitCount());
      totalStakeSummary.record(betRisk.totalStake().doubleValue());
      totalReturnSummary.record(betRisk.totalReturn().doubleValue());
   }

   private double averagePrice() {
      return betRisks.values().stream()
         .reduce(BetRisk::add)
         .map(BetRisk::betPrice)
         .orElse(BigDecimal.ONE)
         .doubleValue();
   }

   public int betCount() {
      return betRisks.size();
   }
}

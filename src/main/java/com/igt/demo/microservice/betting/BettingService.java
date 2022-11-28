package com.igt.demo.microservice.betting;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

import com.igt.demo.microservice.tools.*;
import io.micrometer.core.annotation.*;
import io.micrometer.core.instrument.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class BettingService {

   private final Map<Long, BetRisk> betRisks;
   private final AtomicLong lastBetId;
   private final Counter betCounter;
   private final Counter betLegCounter;
   private final Counter unitCounter;
   private final Counter totalStakeCounter;
   private final Counter totalReturnCounter;

   @Autowired
   public BettingService(MeterRegistry meterRegistry) {
      betRisks = new ConcurrentHashMap<>();
      lastBetId = new AtomicLong();
      betCounter = meterRegistry.counter("bet.count");
      betLegCounter = meterRegistry.counter("bet.leg.count");
      unitCounter = meterRegistry.counter("unit.count");
      totalStakeCounter = meterRegistry.counter("stake.total.amount");
      totalReturnCounter = meterRegistry.counter("return.total.amount");
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
      betLegCounter.increment(betRisk.bet().legCount());
      unitCounter.increment(betRisk.unitCount());
      totalStakeCounter.increment(betRisk.totalStake().doubleValue());
      totalReturnCounter.increment(betRisk.totalReturn().doubleValue());
   }
}

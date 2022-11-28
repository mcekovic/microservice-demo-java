package com.igt.demo.microservice.betting;

import java.math.*;

public record BetLeg(
   long selectionId,
   BigDecimal price
) {}

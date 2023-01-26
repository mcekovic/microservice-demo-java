package com.igt.demo.microservice.tools;

import org.junit.jupiter.api.*;

import static com.igt.demo.microservice.tools.Buckets.*;
import static org.assertj.core.api.Assertions.*;

class BucketsTest {

   @Test
   void linearBucketsTest() {
      var buckets = linearBuckets(1.0, 2.0, 3);

      assertThat(buckets).containsExactly(1.0, 3.0, 5.0);
   }

   @Test
   void exponentialBucketsTest() {
      var buckets = exponentialBuckets(1.0, 2.0, 3);

      assertThat(buckets).containsExactly(1.0, 2.0, 4.0);
   }

   @Test
   void powersOfTenDividedBucketsTest() {
      var buckets = powersOfTenDividedBuckets(0, 2, 4);

      assertThat(buckets).containsExactly(1.0, 2.5, 5.0, 7.5, 10.0, 25.0, 50.0, 75.0, 100.0);
   }
}

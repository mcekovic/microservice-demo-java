package com.igt.demo.microservice.tools;

public interface Buckets {

   static double[] linearBuckets(double start, double width, int count) {
      var buckets = new double[count];
      var bucket = start;
      var index = 0;
      while (true) {
         buckets[index] = bucket;
         if (++index >= count)
            break;
         bucket += width;
      }
      return buckets;
   }

   static double[] exponentialBuckets(double start, double factor, int count) {
      var buckets = new double[count];
      var bucket = start;
      var index = 0;
      while (true) {
         buckets[index] = bucket;
         if (++index >= count)
            break;
         bucket *= factor;
      }
      return buckets;
   }

   static double[] powersOfTenDividedBuckets(int startPower, int endPower, int divisions) {
      var buckets = new double[(endPower - startPower) * divisions + 1];
      var powerBucket = Math.pow(10.0, startPower);
      var index = 0;
      buckets[index] = powerBucket;
      for (var powerIndex = 0; powerIndex < endPower; powerIndex++) {
         powerBucket *= 10;
         var divisionWidth = powerBucket / divisions;
         var bucket = divisionWidth;
         var divisionIndex = 0;
         while (true) {
            buckets[++index] = bucket;
            if (++divisionIndex >= divisions)
               break;
            bucket += divisionWidth;
         }
      }
      return buckets;
   }
}

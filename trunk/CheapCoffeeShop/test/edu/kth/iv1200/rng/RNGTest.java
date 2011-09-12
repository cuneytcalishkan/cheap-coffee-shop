/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.rng;

import org.junit.Test;

/**
 *
 * @author cuneyt
 */
public class RNGTest {

    private int N = 1000;
    private double interArrivalRate = 0.2;
    private double serviceRate = 0.25;
    private LCG lcg = new LCG(System.currentTimeMillis(), serviceRate, interArrivalRate);
    private double[] generated = new double[N];

    private void generateRandomNumbers() {
        for (int i = 0; i < N; i++) {
            generated[i] = lcg.nextArrivalExp();
        }
    }

    @Test
    public void chiSquareTest() {
        int k = 40;
        double e = N / k;
        int[] frequency = new int[k];
        double acc = 0;
        double j1 = 0, j2 = 0;
        for (int i = 0; i < k; i++) {
            frequency[i] = 0;
        }
        generateRandomNumbers();

        for (int i = 0; i < generated.length; i++) {
            for (int j = 1; j <= k; j++) {
                j1 = (j - 1) * (e / 1000);
                j2 = j * (e / 1000);
                if ((generated[i] >= j1) && (generated[i] < j2)) {
                    frequency[j - 1]++;
                    break;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            acc += Math.pow(frequency[i] - e, 2) / e;
        }
        System.out.println("X^2 = " + acc);
    }
}

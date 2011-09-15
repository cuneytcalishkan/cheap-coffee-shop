package edu.kth.iv1200.rng;

import org.junit.Test;

/**
 *
 * @author cuneyt
 */
public class RNGTest {

    private int N = 1000;
    private double interArrivalRate = 5;
    private double serviceRate = 4;
    private LCG lcg = new LCG(34234321, serviceRate, interArrivalRate);
    private double[] generated = new double[N];
    private double avg = 0;

    private void generateRandomNumbers() {
        for (int i = 0; i < N; i++) {
            //generated[i] = lcg.nextRand();
            generated[i] = lcg.nextArrivalExp();
            //generated[i] = lcg.nextDepartureExp();
            System.out.println(generated[i]);
            avg += generated[i];
        }
        System.out.println("average time " + avg / N);
    }

    /**
     * The Chi Square test to test whether the random number generator is uniform or not.
     */
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

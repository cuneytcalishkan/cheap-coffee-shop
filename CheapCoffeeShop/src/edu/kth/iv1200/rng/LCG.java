/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.rng;

import java.util.Random;

/**
 *
 * @author Cuneyt Caliskan
 * 
 * X0 = seed
 * for i = 0,1,2,...,n
 * Xi = (a.Xi-1 + c) mod m
 * Ui = Xi/m
 * where m>0, a<m, c<m, X0<m
 */
public class LCG {

    private double Xi;
    private double serviceRate;
    private double interArrivalRate;
    public static double m = Math.pow(2, 31) - 1;
    public static double a = 314159269;
    public static double c = 453806245;

    public LCG(double Xi, double serviceRate, double interArrivalRate) {
        this.Xi = Xi;
        this.serviceRate = serviceRate;
        this.interArrivalRate = interArrivalRate;
    }

    public double nextRand() {
        Xi = (a * Xi + c) % m;
        return Xi / m;
    }

    public double nextArrivalExp() {
        double result = -interArrivalRate * Math.log(nextRand());
        return result;
    }

    public double nextDepartureExp() {
        double result = -serviceRate * Math.log(nextRand());
        return result;
    }
}

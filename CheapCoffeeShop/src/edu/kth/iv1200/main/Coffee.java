/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.main;

/**
 *
 * @author cuneyt
 */
public class Coffee {

    private static String howToRun = "java Coffee [seed] [replications] [queue size] [service rate] [interarrival rate]\n"
            + "Example: java Coffee 3242563 10 100 15 12\n"
            + "queue size: -1 denotes infinite queue\n"
            + "service rate & interarrival rate: denotes the number of customers per hour";

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println(howToRun);
            System.exit(-1);
        }
        int index = 0;
        double seed = Double.parseDouble(args[index++]);
        int replications = Integer.parseInt(args[index++]);
        int queueSize = Integer.parseInt(args[index++]);
        double serviceRate = Double.parseDouble(args[index++]);
        double interArrivalRate = Double.parseDouble(args[index++]);


        System.out.println(seed + "|" + replications + "|" + queueSize + "|" + serviceRate + "|" + interArrivalRate);

    }
}

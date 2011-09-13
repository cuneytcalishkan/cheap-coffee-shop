/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.main;

import edu.kth.iv1200.model.Statistics;
import edu.kth.iv1200.simulation.Simulator;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        double avgWaitingTime = 0;
        double avgRejectedPercentage = 0;
        double avgCustomer = 0;


        ArrayList<Future<Statistics>> statistics = new ArrayList<Future<Statistics>>();
        ExecutorService es = java.util.concurrent.Executors.newFixedThreadPool(replications);

        for (int i = 0; i < replications; i++) {
            Callable<Statistics> sim = new Simulator(seed, queueSize, i + 1, serviceRate, interArrivalRate);
            Future<Statistics> future = es.submit(sim);
            statistics.add(future);
        }

        for (Future<Statistics> future : statistics) {
            try {
                Statistics s = future.get();
                avgWaitingTime += s.getAvgWaitingTime();
                avgRejectedPercentage += s.getRejectedPercentage();
                avgCustomer += s.getCustomers();

                System.out.println("Replication ID: " + s.getId());
                System.out.println("Total customers: " + s.getCustomers());
                System.out.println("Rejected customers count: " + s.getRejectedCount());
                System.out.println("Rejected customers percentage: %" + (s.getRejectedPercentage() * 100));
                System.out.println("Average waiting time: " + s.getAvgWaitingTime());
                System.out.println("\n------------------------------------------------");

            } catch (InterruptedException ex) {
                Logger.getLogger(Coffee.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(Coffee.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("\n------------------------------------------------");
        System.out.println("Average results of " + replications + " replicas");
        System.out.println("------------------------------------------------");
        System.out.println("Average customer number: " + avgCustomer / replications);
        System.out.println("Average rejected customers percentage: %" + (avgRejectedPercentage / replications)*100);
        System.out.println("Average waiting time: " + avgWaitingTime / replications);

        System.exit(1);
    }
}

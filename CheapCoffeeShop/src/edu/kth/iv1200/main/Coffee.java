package edu.kth.iv1200.main;

import edu.kth.iv1200.model.Statistics;
import edu.kth.iv1200.rng.LCG;
import edu.kth.iv1200.simulation.Simulator;
import java.text.DecimalFormat;
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

    /**
     * Instructions on how to run the program from the command line.
     */
    private static String howToRun = "java Coffee [seed] [replications] [queue size] [service mean] [interarrival mean]\n"
            + "Example: java Coffee 3242563 10 -1 4 5\n"
            + "seed: -1 denotes current system time in milliseconds\n"
            + "queue size: -1 denotes infinite queue\n"
            + "service rate & interarrival rate: mean service and interarrival time in minutes";

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println(howToRun);
            System.exit(-1);
        }
        int index = 0;
        double seed = Double.parseDouble(args[index++]);
        if (seed == -1) {
            seed = System.currentTimeMillis();
        }
        int replications = Integer.parseInt(args[index++]);
        int queueSize = Integer.parseInt(args[index++]);
        double serviceMean = Double.parseDouble(args[index++]);
        double interArrivalMean = Double.parseDouble(args[index++]);
        /**
         * Average waiting time statistics.
         */
        double avgWaitingTime = 0;
        /**
         * Average percentage of the rejected customers.
         */
        double avgRejectedPercentage = 0;
        /**
         * Average number of customers per simulation.
         */
        double avgCustomer = 0;

        /**
         * A list of the results to be obtained after execution.
         */
        ArrayList<Future<Statistics>> statistics = new ArrayList<Future<Statistics>>();
        ExecutorService es = java.util.concurrent.Executors.newFixedThreadPool(replications);
        LCG lcg = new LCG(seed, serviceMean, interArrivalMean);
        for (int i = 0; i < replications; i++) {
            Callable<Statistics> sim = new Simulator(lcg, queueSize, i + 1);
            Future<Statistics> future = es.submit(sim);
            statistics.add(future);
        }
        DecimalFormat format = new DecimalFormat(".####");
        for (Future<Statistics> future : statistics) {
            try {
                Statistics s = future.get();
                avgWaitingTime += s.getAvgWaitingTime();
                avgRejectedPercentage += s.getRejectedPercentage();
                avgCustomer += s.getCustomers();
                System.out.println("Replication ID: " + s.getId());
                System.out.println("Server utilization: %" + format.format(100 - s.getIdlePercentage() * 100));
                System.out.println("Total customers served: " + s.getCustomers());
                System.out.println("Rejected customers count: " + s.getRejectedCount());
                System.out.println("Rejected customers percentage: %" + format.format(s.getRejectedPercentage() * 100));
                System.out.println("Average waiting time: " + format.format(s.getAvgWaitingTime()));
                System.out.println("Average queue length: " + format.format(s.getQueueLength()));
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
        System.out.println("Average served customers number: " + format.format(avgCustomer / replications));
        System.out.println("Average rejected customers percentage: %" + format.format((avgRejectedPercentage / replications) * 100));
        System.out.println("Average waiting time: " + format.format(avgWaitingTime / replications));
        System.exit(1);
    }
}

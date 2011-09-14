/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.simulation;

import edu.kth.iv1200.model.ArrivalEvent;
import edu.kth.iv1200.model.CCEvent;
import edu.kth.iv1200.model.Customer;
import edu.kth.iv1200.model.DepartureEvent;
import edu.kth.iv1200.model.Statistics;
import edu.kth.iv1200.rng.LCG;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author cuneyt
 */
public class Simulator implements Callable<Statistics> {

    private double seed;
    private int queueSize;
    private int replicationId;
    private double idleTime = 0;
    private double lastTimeSetToIdle = 0;
    private boolean idle = true;
    private double clock = 0;
    private double avgWaitingTime = 0;
    private double queueLength = 0;
    private double step = 0;
    private double rejectedCustomerCount = 0;
    private double rejectedPercentage = 0;
    private ArrayList<Customer> customers;
    private ArrayList<Customer> queue;
    private TreeMap<Double, CCEvent> fel;
    private LCG lcg;

    /**
     * @param seed - seed for the random number generator
     * @param queueSize - the capacity of the queue
     * @param replicationId - id of the simulation replica
     * @param serviceRate - mean service time in minutes. e.g: 4
     * @param interArrivalRate - mean inter arrival time in minutes. e.g: 5
     */
    public Simulator(double seed, int queueSize, int replicationId, double serviceRate, double interArrivalRate) {
        this.seed = seed;
        this.queueSize = queueSize;
        this.replicationId = replicationId;
        this.queue = new ArrayList<Customer>();
        this.customers = new ArrayList<Customer>();
        this.fel = new TreeMap<Double, CCEvent>();
        this.lcg = new LCG(seed, serviceRate, interArrivalRate);
    }

    @Override
    public Statistics call() throws Exception {
        boolean stopSimulation = false;
        ArrivalEvent firstEvent = new ArrivalEvent(clock + lcg.nextArrivalExp());
        Customer fc = new Customer(firstEvent.getTime());
        firstEvent.setBelongsTo(fc);
        customers.add(fc);
        fel.put(firstEvent.getTime(), firstEvent);

        while (!fel.isEmpty()) {
            double key = fel.firstKey();
            CCEvent e = fel.remove(key);
            clock = e.getTime();
            step++;
            queueLength += queue.size();
            if (e instanceof ArrivalEvent) {
                processArrivalEvent((ArrivalEvent) e, stopSimulation);

            } else if (e instanceof DepartureEvent) {
                processDepartureEvent((DepartureEvent) e);
            }

            if (clock >= 720) {
                stopSimulation = true;
            }
        }
        return getStatistics();
    }

    private void processArrivalEvent(ArrivalEvent e, boolean stop) {

        if (!stop) {
            ArrivalEvent next = new ArrivalEvent(clock + lcg.nextArrivalExp());
            Customer c = new Customer(next.getTime());
            next.setBelongsTo(c);
            customers.add(c);
            fel.put(next.getTime(), next);
        }
        if (isIdle()) {
            setBusy();
            idleTime += clock - lastTimeSetToIdle;
            DepartureEvent dp = new DepartureEvent(clock + lcg.nextDepartureExp());
            dp.setBelongsTo(e.getBelongsTo());
            dp.getBelongsTo().setEndService(dp.getTime());
            dp.getBelongsTo().setWaitingTime(0);
            fel.put(dp.getTime(), dp);
        } else {
            if (queueSize == -1) {
                queue.add(e.getBelongsTo());
            } else {
                if (queue.size() >= queueSize) {
                    rejectedCustomerCount++;
                } else {
                    queue.add(e.getBelongsTo());
                }
            }
        }
    }

    private void processDepartureEvent(DepartureEvent e) {
        if (queue.isEmpty()) {
            setIdle();
            lastTimeSetToIdle = clock;
        } else {
            Customer c = queue.remove(0);
            c.setWaitingTime(clock - c.getArrivalTime());
            DepartureEvent de = new DepartureEvent(clock + lcg.nextDepartureExp());
            c.setEndService(de.getTime());
            de.setBelongsTo(c);
            fel.put(de.getTime(), de);
        }
    }

    private Statistics getStatistics() throws InterruptedException, ExecutionException {
        double acc = 0;

        rejectedPercentage = rejectedCustomerCount / customers.size();

        for (Customer customer : customers) {
            acc += customer.getWaitingTime();
        }
        avgWaitingTime = acc / (customers.size() - rejectedCustomerCount);
        return new Statistics(replicationId, customers.size() - rejectedCustomerCount, rejectedPercentage, avgWaitingTime, rejectedCustomerCount, idleTime / clock, queueLength / step);
    }

    public TreeMap<Double, CCEvent> getFel() {
        return fel;
    }

    public void setFel(TreeMap<Double, CCEvent> fel) {
        this.fel = fel;
    }

    public boolean isIdle() {
        return idle;
    }

    public void setBusy() {
        this.idle = false;
    }

    public void setIdle() {
        this.idle = true;
    }

    public double getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(double idleTime) {
        this.idleTime = idleTime;
    }

    public ArrayList<Customer> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Customer> queue) {
        this.queue = queue;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getReplicationId() {
        return replicationId;
    }

    public void setReplicationId(int replicationId) {
        this.replicationId = replicationId;
    }

    public double getSeed() {
        return seed;
    }

    public void setSeed(double seed) {
        this.seed = seed;
    }

    public LCG getLcg() {
        return lcg;
    }

    public void setLcg(LCG lcg) {
        this.lcg = lcg;
    }
}

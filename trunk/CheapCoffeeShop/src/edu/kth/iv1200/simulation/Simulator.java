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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author cuneyt
 */
public class Simulator implements RunnableFuture<Statistics> {

    private double seed;
    private int queueSize;
    private int replicationId;
    private double idleTime = 0;
    private double lastTimeSetToIdle = 0;
    private boolean idle = true;
    private double clock = 0;
    private double avgWaitingTime = 0;
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
     * @param serviceRate - number of customers being served in an hour. e.g: 15
     * @param interArrivalRate - number of customers arriving in an hour. e.g: 12
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
    public void run() {

        ArrivalEvent firstEvent = new ArrivalEvent(clock + lcg.nextArrivalExp());
        fel.put(firstEvent.getTime(), firstEvent);

        //TODO customer statistics

        while (!fel.isEmpty()) {
            double key = fel.firstKey();
            CCEvent e = fel.remove(key);
            clock = e.getTime();

            if (e instanceof ArrivalEvent) {
                ArrivalEvent next = new ArrivalEvent(clock + lcg.nextArrivalExp());
                fel.put(next.getTime(), next);

                if (isIdle()) {
                    setBusy();
                    idleTime += clock - lastTimeSetToIdle;
                    DepartureEvent dp = new DepartureEvent(clock + lcg.nextDepartureExp());
                    fel.put(dp.getTime(), dp);
                } else {
                    //TODO customer statistics
                    Customer c = new Customer(next.getTime());
                    if (queueSize == -1) {
                        queue.add(c);
                    } else {
                        if (queue.size() >= queueSize) {
                            rejectedCustomerCount++;
                        } else {
                            queue.add(c);
                        }
                    }
                }

            } else if (e instanceof DepartureEvent) {

                if (queue.isEmpty()) {
                    setIdle();
                    lastTimeSetToIdle = clock;
                } else {
                    Customer c = queue.remove(0);
                    c.setWaitingTime(clock - c.getArrivalTime());
                    c.setStartService(clock);
                    DepartureEvent de = new DepartureEvent(clock + lcg.nextDepartureExp());
                    fel.put(de.getTime(), de);
                }

            }

            //TODO end of simulation control
            if (clock >= 720) {
                break;
            }
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Statistics get() throws InterruptedException, ExecutionException {
        double acc = 0;

        rejectedPercentage = rejectedCustomerCount / customers.size();

        for (Customer customer : customers) {
            acc += customer.getWaitingTime();
        }
        avgWaitingTime = acc / (customers.size() - rejectedCustomerCount);
        return new Statistics(replicationId, customers.size(), rejectedPercentage, avgWaitingTime);
    }

    @Override
    public Statistics get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet.");
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

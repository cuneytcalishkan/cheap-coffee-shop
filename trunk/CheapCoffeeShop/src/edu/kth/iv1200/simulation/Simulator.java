/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.simulation;

import edu.kth.iv1200.model.CCEvent;
import edu.kth.iv1200.model.Customer;
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
public class Simulator implements RunnableFuture<String> {

    private double seed;
    private int queueSize;
    private int replicationId;
    private double idleTime = 0;
    private boolean idle = true;
    private double rejectedCustomerCount = 0;
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
        throw new UnsupportedOperationException("Not supported yet.");
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
    public String get() throws InterruptedException, ExecutionException {
        double avgWaitingTime = 0;
        double acc = 0;
        double rejectedPercentage = 0;

        rejectedPercentage = rejectedCustomerCount / customers.size();

        for (Customer customer : customers) {
            acc += customer.getWaitingTime();
        }
        avgWaitingTime = acc / (customers.size() - rejectedCustomerCount);
        return replicationId + "|" + customers.size() + "|" + rejectedPercentage + "|" + avgWaitingTime;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
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

    public void setIdle(boolean idle) {
        this.idle = idle;
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
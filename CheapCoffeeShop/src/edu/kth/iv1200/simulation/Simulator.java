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

/**
 *
 * @author Cuneyt Caliskan
 */
public class Simulator implements Callable<Statistics> {

    /**
     * The size of the queue.
     */
    private int queueSize;
    /**
     * The replica id.
     */
    private int replicationId;
    /**
     * The total time that the server was idle.
     */
    private double idleTime = 0;
    /**
     * Last time when the server was set to idle.
     */
    private double lastTimeSetToIdle = 0;
    /**
     * Server status, idle or not.
     */
    private boolean idle = true;
    /**
     * The simulation clock.
     */
    private double clock = 0;
    /**
     * Average waiting time of the customers on the queue.
     */
    private double avgWaitingTime = 0;
    /**
     * The length of the queue.
     */
    private double queueLength = 0;
    /**
     * Indicates how many times the queue size changed.
     */
    private double step = 0;
    /**
     * The number of rejected customers due to queue overcapacity.
     */
    private double rejectedCustomerCount = 0;
    /**
     * Percentage of the customers rejected.
     */
    private double rejectedPercentage = 0;
    /**
     * All customers arriving at the shop.
     */
    private ArrayList<Customer> customers;
    /**
     * The queue consisting of the customers waiting for service.
     */
    private ArrayList<Customer> queue;
    /**
     * The future event list.
     */
    private TreeMap<Double, CCEvent> fel;
    /**
     * The random number generator.
     */
    private LCG lcg;

    /**
     * The actual simulator thread that implements <code>Callable</code> interface and returns <code>Statistics</code> object as a result.
     * 
     * @param lcg - Linear Congruential Generator random number generator
     * @param queueSize - the capacity of the queue
     * @param replicationId - id of the simulation replica
     */
    public Simulator(LCG lcg, int queueSize, int replicationId) {
        this.lcg = lcg;
        this.queueSize = queueSize;
        this.replicationId = replicationId;
        this.queue = new ArrayList<Customer>();
        this.customers = new ArrayList<Customer>();
        this.fel = new TreeMap<Double, CCEvent>();
    }

    /**
     * Executes the simple DES algorithm.
     * @return The statistics collected about the simulation.
     */
    @Override
    public Statistics call() {
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

    /**
     * If stop!=true, a new arrival event is generated and put into the future event list.
     * If the server is idle, the arrival event is processed and a departure event is scheduled.
     * If the server is busy, the customer is placed on the queue or rejected depending on the
     * queue size.
     * @param e The event to be handled.
     * @param stop Stop creating new arrival events or not.
     */
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

    /**
     * If the queue is empty, the server is set to idle and server idle time statistics are collected.
     * If the queue is not empty, the first customer is removed from the queue and a departure event
     * is scheduled.
     * @param e The event to be handled.
     */
    private void processDepartureEvent(DepartureEvent e) {
        if (queue.isEmpty()) {
            setIdle();
            lastTimeSetToIdle = clock;
        } else {
            Customer c = queue.remove(0);
            c.setWaitingTime(clock - c.getArrivalTime());
            DepartureEvent de = new DepartureEvent(clock + lcg.nextDepartureExp());
            de.setBelongsTo(c);
            fel.put(de.getTime(), de);
        }
    }

    /**
     * 
     * @return The calculated statistics values of the simulation.
     */
    private Statistics getStatistics() {
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

    public LCG getLcg() {
        return lcg;
    }

    public void setLcg(LCG lcg) {
        this.lcg = lcg;
    }
}

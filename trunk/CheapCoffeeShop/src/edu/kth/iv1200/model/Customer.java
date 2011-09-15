package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class Customer {

    /**
     * Arrival time of the customer.
     */
    private double arrivalTime;
    /**
     * Waiting time of the customer in the queue.
     */
    private double waitingTime = 0;

    /**
     * Customer.
     * @param arrivalTime Time of arrival.
     */
    public Customer(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
}

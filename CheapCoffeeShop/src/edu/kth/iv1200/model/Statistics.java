/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class Statistics {

    private int id;
    private double customers;
    private double rejectedPercentage;
    private double avgWaitingTime;
    private double rejectedCount;
    private double idlePercentage;
    private double queueLength;

    public Statistics(int id, double customers, double rejectedPercentage, double avgWaitingTime, double rejectedCount, double idlePercentage, double queueLength) {
        this.id = id;
        this.customers = customers;
        this.rejectedPercentage = rejectedPercentage;
        this.avgWaitingTime = avgWaitingTime;
        this.rejectedCount = rejectedCount;
        this.idlePercentage = idlePercentage;
        this.queueLength = queueLength;
    }

    public double getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(double avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public double getCustomers() {
        return customers;
    }

    public void setCustomers(double customers) {
        this.customers = customers;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRejectedPercentage() {
        return rejectedPercentage;
    }

    public void setRejectedPercentage(double rejectedPercentage) {
        this.rejectedPercentage = rejectedPercentage;
    }

    public double getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(double rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public double getIdlePercentage() {
        return idlePercentage;
    }

    public void setIdlePercentage(double idlePercentage) {
        this.idlePercentage = idlePercentage;
    }

    public double getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(double queueLength) {
        this.queueLength = queueLength;
    }
}

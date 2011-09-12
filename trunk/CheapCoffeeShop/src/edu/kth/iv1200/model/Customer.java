/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class Customer {

    private int id;
    private double arrivalTime;
    private double departureTime;
    private double startService;
    private double endService;
    private double waitingTime = 0;

    public Customer(int id, double arrivalTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getEndService() {
        return endService;
    }

    public void setEndService(double endService) {
        this.endService = endService;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStartService() {
        return startService;
    }

    public void setStartService(double startService) {
        this.startService = startService;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
}

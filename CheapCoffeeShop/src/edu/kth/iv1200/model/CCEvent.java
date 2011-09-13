/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class CCEvent {

    private double time;
    private Customer belongsTo;

    public CCEvent(double time) {
        this.time = time;
    }

    public CCEvent(double time, Customer belongsTo) {
        this.time = time;
        this.belongsTo = belongsTo;
    }

    public Customer getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(Customer belongsTo) {
        this.belongsTo = belongsTo;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}

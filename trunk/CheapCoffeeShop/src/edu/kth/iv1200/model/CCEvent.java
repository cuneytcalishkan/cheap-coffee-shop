package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class CCEvent {

    /**
     * Time of happening.
     */
    private double time;
    /**
     * The customer that this event belongs to.
     */
    private Customer belongsTo;

    /**
     * Any possible event that can take place in the system.
     * @param time Time of happening.
     */
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

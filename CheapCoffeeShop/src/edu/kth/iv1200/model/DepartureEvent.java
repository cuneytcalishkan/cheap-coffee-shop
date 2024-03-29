package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class DepartureEvent extends CCEvent {

    /**
     * Departure event which indicates that the customer leaves the store.
     * @param time The time of departure.
     */
    public DepartureEvent(double time) {
        super(time);
    }

    @Override
    public double getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(double time) {
        super.setTime(time);
    }

    @Override
    public Customer getBelongsTo() {
        return super.getBelongsTo();
    }

    @Override
    public void setBelongsTo(Customer belongsTo) {
        super.setBelongsTo(belongsTo);
    }
}

package edu.kth.iv1200.model;

/**
 *
 * @author cuneyt
 */
public class ArrivalEvent extends CCEvent {

    /**
     * Arrival event which indicates that the customer arrives to store.
     * @param time Time of arrival.
     */
    public ArrivalEvent(double time) {
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

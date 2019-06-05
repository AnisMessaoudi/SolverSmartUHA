package ortools.routing.model;

public class Demand {
    
    public enum Request {
	smallLetter,
	largeLetter,
	cargo1,
	cargo3,
	cargo6;
    }

    public enum State {
	waitingConfirmation,
	pendingOrigin,
	pendingdestination,
	inProgress,
	pending, 
	cancelled,
	served;
    }
    
    private String id;
    private int isUrgent;
    private Request request;
    private String originId;
    private String destinationId;
    private long deliveryTime;
    private State state;
    
    public Demand(String id, int isUrgent, Request request, String originId, String destinationId, long deliveryTime,
	    State state) {
	super();
	this.id = id;
	this.isUrgent = isUrgent;
	this.request = request;
	this.originId = originId;
	this.destinationId = destinationId;
	this.deliveryTime = deliveryTime;
	this.state = state;
    }

    @Override
    public String toString() {
	return "Demand [id=" + id + ", isUrgent=" + isUrgent + ", request=" + request + ", originId=" + originId
		+ ", destinationId=" + destinationId + ", deliveryTime=" + deliveryTime + ", state=" + state + "]";
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }
    
    public long getDeliveryTime() {
	return deliveryTime;
    }
    
    

}

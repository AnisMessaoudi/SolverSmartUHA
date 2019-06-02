package ortools.routing.model;

public class Demand {
    
    private String id;
    private int isUrgent;
    private String request;
    private String originId;
    private String destinationId;
    private long deliveryTime;
    private String state;
    
    public Demand(String id, int isUrgent, String request, String originId, String destinationId, long deliveryTime,
	    String state) {
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
    
    

}

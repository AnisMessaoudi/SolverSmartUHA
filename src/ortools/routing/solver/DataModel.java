package ortools.routing.solver;

public class DataModel {
    
    public int nodeNumber;

    public long[][] distanceMatrix;
    public long[][] energyMatrix;
    public long[][] timeMatrix;
    
    public int [][] requests;
    public long [] deliveryTimes;
    
    public int vehicleNumber;
    
    public int[] vehicleStarts;
    public int[] vehicleEnds;
    
    public long[] vehicleLetterCapacity;
    public long[] vehicleCargoCapacity;
    public long[] letterDemands;
    public long[] cargoDemands;
    
    
    public DataModel(int nodeNumber, long[][] distanceMatrix, long[][] timeMatrix,
	    int[][] requests, long[] deliveryTimes, int vehicleNumber, int[] vehicleStarts,
	    int[] vehicleEnds) {
	super();
	this.nodeNumber = nodeNumber;
	this.distanceMatrix = distanceMatrix;
	this.timeMatrix = timeMatrix;
	this.requests = requests;
	this.deliveryTimes = deliveryTimes;
	this.vehicleNumber = vehicleNumber;
	this.vehicleStarts = vehicleStarts;
	this.vehicleEnds = vehicleEnds;
    }
    
}

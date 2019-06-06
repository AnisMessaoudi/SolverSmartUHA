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
    
    public long[] vehicleSmallLetterCapacity;
    public long[] vehicleLargeLetterCapacity;
    public long[] vehicleCargoCapacity;
    public long[] smallLetterDemands;
    public long[] largeLetterDemands;
    public long[] cargoDemands;
    
    
    public DataModel() {
	super();
    }
    
}

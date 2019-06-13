package fr.uha.ensisa.project.smartUHA.solver;

public class DataModel {
    /*** RoutingModel data's ***/
    
    /* number of points in solver  */
    public int nodeNumber;

    /* distance, energy and time between solver points */
    public long[][] distanceMatrix; 
    public long[][] energyMatrix;
    public long[][] timeMatrix;
    
    /* pairs of point corresponding to pickup and delivery locations */
    public int [][] requests;
    
    /* time (in long) vehicles need to respect (0 means no time constraint) */
    public long [] deliveryTimes;
    
    /* number of available vehicle */
    public int vehicleNumber;
    
    /* starting point for each vehicle */
    public int[] vehicleStarts;
    /*abstract ending point since it's needed for the model but not necessary for us */
    public int[] vehicleEnds;
    
    /* remaining energy for each vehicle */
    public long[] vehicleEnergy;
    
    /* capacities for each vehicle */
    public long[] vehicleSmallLetterCapacity;
    public long[] vehicleLargeLetterCapacity;
    public long[] vehicleCargoCapacity;
    
    /* weight of load for each point (0 = no load and negative = delivery point) */
    public long[] smallLetterDemands;
    public long[] largeLetterDemands;
    public long[] cargoDemands;
    
    
    public DataModel() {
	super();
    }
    
}

package ortools.routing.solver;

public class DataModel {
    
    int nodeNumber;

    long[][] distanceMatrix;
    long[][] energyMatrix;
    long[][] timeMatrix;
    
    int [][] requests;
    long [][] timeWindows;
    
    int vehicleNumber;
    
    int[] vehicleStarts;
    int[] vehicleEnds;
    
    long[] vehicleLetterCapacity;
    long[] vehicleCargoCapacity;
    long[] letterDemands;
    long[] cargoDemands;
    

    

}

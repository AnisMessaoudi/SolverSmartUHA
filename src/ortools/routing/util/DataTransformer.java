package ortools.routing.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ortools.routing.model.Demand;
import ortools.routing.model.Vehicle;
import ortools.routing.solver.DataModel;

public class DataTransformer {
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Demand> demands = new ArrayList<>();
    
    private Map<Integer, String> nodeTable = new HashMap<>();


    public DataTransformer(List<Vehicle> vehicles, List<Demand> demands) {
	super();
	this.vehicles = vehicles;
	this.demands = demands;
	initMap();
    }
    
    private void initMap() {
	int i = 0, j=0;
	for(; i < vehicles.size(); i++) {
	    nodeTable.put(i, vehicles.get(i).getDestinationId());
	}
	for (; j < demands.size(); j++) {
	    nodeTable.put(i+ 2*j, demands.get(j).getOriginId());
	    nodeTable.put(i+ 2*j+1, demands.get(j).getDestinationId());
	}
//	
//	Set<Entry<Integer, String>> setHm = nodeTable.entrySet();
//	Iterator<Entry<Integer, String>> it = setHm.iterator();
//	while(it.hasNext()){
//	    Entry<Integer, String> e = it.next();
//	    System.out.println(e.getKey() + " : " + e.getValue());
//	}
    }
    
    
    public void getData(DataModel data) {
	//initialize arrays
	data.distanceMatrix = new long[this.getNodeNumber()][this.getNodeNumber()];
	data.timeMatrix = new long[this.getNodeNumber()][this.getNodeNumber()];
	data.energyMatrix = new long[this.getNodeNumber()][this.getNodeNumber()]; 
	data.vehicleStarts = new int[this.vehicles.size()];
	data.vehicleEnds = new int[this.getVehicleNumber()];
	
	data.nodeNumber = this.getNodeNumber();
	this.getMatrices(data.distanceMatrix, data.timeMatrix, data.energyMatrix);
	data.requests = this.getRequests();
	data.deliveryTimes = this.getDeliveryTime();
	data.vehicleNumber = this.getVehicleNumber();
	this.getVehicleStartsAndEnds(data.vehicleStarts, data.vehicleEnds);
    }

    public int getVehicleNumber() {
	return vehicles.size();
    }
    
    public int getNodeNumber() {
	return vehicles.size() + 2 * demands.size() + 1;
    }
    
    public int[][] getRequests(){
	int[][] requests = new int[demands.size()][2];
	for(int i=0; i < demands.size(); i++) {
	    requests[i][0]= 2*i + getVehicleNumber();
	    requests[i][1]= 2*i + getVehicleNumber() + 1;
	}
	return requests;
    }
    
    public long [] getDeliveryTime() {
	long[] deliveryTimes = new long[getNodeNumber()];
	int n = this.getVehicleNumber();
	for(int i=0; i < n; i++) {
	    deliveryTimes[i] = 1559329300285L;
	}
	for(int j=0; j < demands.size(); j++) {
	    deliveryTimes[2*j + n] = demands.get(j).getDeliveryTime();
	    deliveryTimes[2*j + n + 1] = 0;
	}
	return deliveryTimes;
    }

    
    public void getVehicleStartsAndEnds(int[] vehicleStarts, int[] vehicleEnds) {
	for (int i = 0; i < vehicles.size(); i++) {
	    System.out.println("coucou" + i);
	    vehicleStarts[i] = i;
	    vehicleEnds[i] = this.getNodeNumber() - 1;
	}
    }
    
    public void getMatrices(long[][] distMatrix, long[][] timeMatrix, long[][] energyMatrix){
	for (int i = 0; i < getNodeNumber()-1; i++) {
	    String iId = nodeTable.get(i);
	    int iIdx = -1;
	    for (Entry<Integer, String> entry : Utils.globalNodeTable.entrySet()) {
		if (entry.getValue().equals(iId)) { 
		    iIdx = entry.getKey();
		    break;
		}
	    }
	    for (int j = 0; j < getNodeNumber()-1; j++) {
		String jId= nodeTable.get(j);
		for (Entry<Integer, String> entry1 : Utils.globalNodeTable.entrySet()) {
		    if (entry1.getValue().equals(jId)) {
			//System.out.println("(" +i+","+j+")  " +iIdx + " : " + entry1.getKey());
			distMatrix[i][j] = Utils.globalDistanceMatrix[iIdx][entry1.getKey()];
			timeMatrix[i][j] = Utils.globalTimeMatrix[iIdx][entry1.getKey()];
			break;
		    }
		}
	    }
	}
    }
    
    

}

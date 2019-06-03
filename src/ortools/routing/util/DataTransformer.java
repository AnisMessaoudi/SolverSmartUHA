package ortools.routing.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ortools.routing.model.Demand;
import ortools.routing.model.Vehicle;

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
	
	Set<Entry<Integer, String>> setHm = nodeTable.entrySet();
	Iterator<Entry<Integer, String>> it = setHm.iterator();
	while(it.hasNext()){
	    Entry<Integer, String> e = it.next();
	    System.out.println(e.getKey() + " : " + e.getValue());
	}
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
    
    public int[] getVehicleStarts() {
	int [] vehicleStarts = new int[vehicles.size()];
	for (int i = 0; i < vehicleStarts.length; i++) {
	    vehicleStarts[i] = i;
	}
	return vehicleStarts;
    }
    
    public int[] getVehicleEnds() {
	int [] vehicleEnds = new int[vehicles.size()];
	for (int i = 0; i < vehicleEnds.length; i++) {
	    vehicleEnds[i] = this.getNodeNumber() - 1;
	}
	return vehicleEnds;
    }
    
    public int[][] getDistMatrix(){
	int[][] distMatrix = new int[getNodeNumber()][getNodeNumber()];
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
			break;
		    }
		}
	    }
	}
	return distMatrix;
    }

}

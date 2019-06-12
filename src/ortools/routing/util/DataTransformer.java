package ortools.routing.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ortools.routing.model.Demand;
import ortools.routing.model.Demand.Request;
import ortools.routing.model.Mailbox;
import ortools.routing.model.Vehicle;
import ortools.routing.solver.DataModel;

public class DataTransformer {
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Demand> demands = new ArrayList<>();
    
    //map entre l'index et l'id d'un point 
    private Map<Integer, String> nodeTable = new HashMap<>();


    public DataTransformer(List<Vehicle> vehicles, List<Demand> demands) {
	super();
	this.vehicles = vehicles;
	this.demands = demands;
	initMap();
    }
    
    private void initMap() {
	int i=0;
	for(; i < vehicles.size(); i++) {
	    nodeTable.put(i, vehicles.get(i).getDestinationId());
	}
	for (int j=0; j < demands.size(); j++) {
	    nodeTable.put(i+ 2*j, demands.get(j).getOriginId());
	    nodeTable.put(i+ 2*j+1, demands.get(j).getDestinationId());
	}
    }
    
    
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Demand> getDemands() {
        return demands;
    }
    
    public Map<Integer, String> getNodeTable() {
        return nodeTable;
    }

    public void getData(DataModel data) {
	//initialize arrays
	data.distanceMatrix = new long[this.getNodeNumber()][this.getNodeNumber()];
	data.timeMatrix = new long[this.getNodeNumber()][this.getNodeNumber()];
	data.energyMatrix = new long[this.getNodeNumber()][this.getNodeNumber()]; 
	data.vehicleStarts = new int[this.getVehicleNumber()];
	data.vehicleEnds = new int[this.getVehicleNumber()];
	data.vehicleSmallLetterCapacity = new long[this.getVehicleNumber()];
	data.vehicleLargeLetterCapacity = new long[this.getVehicleNumber()];
	data.vehicleCargoCapacity = new long[this.getVehicleNumber()];
	data.smallLetterDemands = new long[this.getNodeNumber()];
	data.largeLetterDemands = new long[this.getNodeNumber()];
	data.cargoDemands = new long[this.getNodeNumber()];
	
	data.nodeNumber = this.getNodeNumber();
	this.getMatrices(data.distanceMatrix, data.timeMatrix, data.energyMatrix);
	data.requests = this.getRequests();
	data.deliveryTimes = this.getDeliveryTime();
	data.vehicleNumber = this.getVehicleNumber();
	this.getVehicleStartsAndEnds(data.vehicleStarts, data.vehicleEnds);
	this.getVehicleCapacities(data.vehicleSmallLetterCapacity,
		data.vehicleLargeLetterCapacity,
		data.vehicleCargoCapacity);
	this.getDemands(data.smallLetterDemands,
		data.largeLetterDemands,
		data.cargoDemands);
    }

    public int getVehicleNumber() {
	return vehicles.size();
    }
    
    public int getNodeNumber() {
	return vehicles.size() + 2 * demands.size() + 1;
    }
    
    private int[][] getRequests(){
	int[][] requests = new int[demands.size()][2];
	for(int i=0; i < demands.size(); i++) {
	    requests[i][0]= 2*i + getVehicleNumber();
	    requests[i][1]= 2*i + getVehicleNumber() + 1;
	}
	return requests;
    }
    
    private long [] getDeliveryTime() {
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

    
    private void getVehicleStartsAndEnds(int[] vehicleStarts, int[] vehicleEnds) {
	for (int i = 0; i < vehicles.size(); i++) {
	    vehicleStarts[i] = i;
	    vehicleEnds[i] = this.getNodeNumber() - 1;
	}
    }
    
    private void getMatrices(long[][] distMatrix, long[][] timeMatrix, long[][] energyMatrix){
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
    
    private void getVehicleCapacities(long[] smallLetterCap, long[] largeLetterCap, long[] cargoCap) {
	for (int i = 0; i < this.getVehicleNumber(); i++) {
	    Vehicle v = vehicles.get(i);
	    for (Mailbox m : v.getMailboxes()) {
		String name = m.getName();
		if (name.equals("smallBox"))
		    smallLetterCap[i] += 1;
		if (name.equals("largeBox"))
		    largeLetterCap[i] += 1;
		if (name.equals("cargo"))
		    cargoCap[i] += 6;
	    }
	}
    }
    
    private void getDemands(long[] smallLetterDemands, long[] largeLetterDemands, long[] cargoDemands) {
	int n = this.getVehicleNumber();
	for (int i = 0; i < demands.size(); i++) {
	    Demand d = demands.get(i);
	    switch(d.getRequest()) {
	    	case smallLetter : 
	    	    smallLetterDemands[2*i + n] = 1;
	    	    smallLetterDemands[2*i + n + 1] = -1;
	    	    break;
	    	case largeLetter : 
	    	    largeLetterDemands[2*i + n] = 1;
	    	    largeLetterDemands[2*i + n + 1] = -1;
	    	    break;  
	    	case cargo1 :
	    	    cargoDemands[2*i + n] = 1;
	    	    cargoDemands[2*i + n + 1] = -1;
	    	    break;
	    	case cargo3 :
	    	    cargoDemands[2*i + n] = 3;
	    	    cargoDemands[2*i + n + 1] = -3;
	    	    break;
	    	case cargo6 :
	    	    cargoDemands[2*i + n] = 6;
	    	    cargoDemands[2*i + n + 1] = -6;
	    	    break;
	    	default :
		    break;
	    }
	}
    }
    

}

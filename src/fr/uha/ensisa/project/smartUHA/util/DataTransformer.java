package fr.uha.ensisa.project.smartUHA.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.uha.ensisa.project.smartUHA.model.Demand;
import fr.uha.ensisa.project.smartUHA.model.Mailbox;
import fr.uha.ensisa.project.smartUHA.model.Vehicle;
import fr.uha.ensisa.project.smartUHA.solver.DataModel;

public class DataTransformer {
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Demand> demands = new ArrayList<>();
    
    //map between index and id location
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

    /* put data in the argument DataModel */
    public void getData(DataModel data) {
	/* instance arrays */
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
	data.vehicleEnergy = new long[this.getVehicleNumber()];
	
	/* transform data from vehicles and demands lists */
	data.nodeNumber = this.getNodeNumber();
	this.getMatrices(data.distanceMatrix, data.timeMatrix, data.energyMatrix);
	data.requests = this.getRequests();
	data.deliveryTimes = this.getDeliveryTime();
	data.vehicleNumber = this.getVehicleNumber();
	data.vehicleEnergy = this.getVehicleEnergy();
	this.getVehicleStartsAndEnds(data.vehicleStarts, data.vehicleEnds);
	this.getVehicleCapacities(data.vehicleSmallLetterCapacity,
		data.vehicleLargeLetterCapacity,
		data.vehicleCargoCapacity);
	this.getDemands(data.smallLetterDemands,
		data.largeLetterDemands,
		data.cargoDemands);
	
    }


    /* return number of available vehicles */
    public int getVehicleNumber() {
	return vehicles.size();
    }
    
    /* return number of location in the intern model */
    public int getNodeNumber() {
	return vehicles.size() + 2 * demands.size() + 1;
    }
    
    /* return locations requests from demands */
    private int[][] getRequests(){
	int[][] requests = new int[demands.size()][2];
	for(int i=0; i < demands.size(); i++) {
	    requests[i][0]= 2*i + getVehicleNumber();
	    requests[i][1]= 2*i + getVehicleNumber() + 1;
	}
	return requests;
    }
    
    /* return when the vehicle must be for each location */
    private long [] getDeliveryTime() {
	long actualtime = 1259329320285L; //put Sys.currentTime mais le temps des demandes doivent etre > currentTime(pas le cas chez nous)
	long[] deliveryTimes = new long[getNodeNumber()];
	int n = this.getVehicleNumber();
	for(int i=0; i < n; i++) {
	    deliveryTimes[i] = actualtime;
	}
	for(int j=0; j < demands.size(); j++) {
	    deliveryTimes[2*j + n] = demands.get(j).getDeliveryTime();
	    deliveryTimes[2*j + n + 1] = 0;
	}
	return deliveryTimes;
    }

    /* return remaining energy for each vehicle */
    private long[] getVehicleEnergy() {
	long vehicleEnergy[] = new long[getVehicleNumber()];
	for (int i = 0; i < vehicleEnergy.length; i++) {
	    vehicleEnergy[i] = (long) vehicles.get(i).getEnergy();
	}
	return vehicleEnergy;
    }
    
    /* return starting location for each vehicle and init. abstract location for solver */
    private void getVehicleStartsAndEnds(int[] vehicleStarts, int[] vehicleEnds) {
	for (int i = 0; i < vehicles.size(); i++) {
	    vehicleStarts[i] = i;
	    vehicleEnds[i] = this.getNodeNumber() - 1;
	}
    }
    
    /* generate local matrices matching with internal index's node locations from global matrices in Utils */
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
			distMatrix[i][j] = Utils.globalDistanceMatrix[iIdx][entry1.getKey()];
			timeMatrix[i][j] = Utils.globalTimeMatrix[iIdx][entry1.getKey()];
			break;
		    }
		}
	    }
	}
    }
    
    /* return capacities to carry small, large letter and cargo for each vehicle */
    private void getVehicleCapacities(long[] smallLetterCap, long[] largeLetterCap, long[] cargoCap) {
	for (int i = 0; i < this.getVehicleNumber(); i++) {
	    Vehicle v = vehicles.get(i);
	    for (Mailbox m : v.getMailboxes()) {
		String name = m.getName();
		if (name.equals("smallLetter"))
		    smallLetterCap[i] += (long) m.getIsEmpty();
		if (name.equals("largeLetter"))
		    largeLetterCap[i] += (long) m.getIsEmpty();
		if (name.equals("cargo"))
		    cargoCap[i] += 6;
	    }
	}
    }
    
    /* return the weights of each cargo for each vehicle */
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

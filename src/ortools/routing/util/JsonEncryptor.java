package ortools.routing.util;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonEncryptor {

    private DataTransformer dt;
    private long[][] solution;



    public JsonEncryptor(DataTransformer dt, long[][] solution) {
	super();
	this.dt = dt;
	this.solution = solution;
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void writePlaning() throws FileNotFoundException {

	JSONObject planning = new JSONObject();
	
	JSONObject planningId = new JSONObject();
	planningId.put("$oid", "....");
	planning.put("_id",planningId); 
	
	JSONArray vehicles = new JSONArray();
	for (int i = 0; i < dt.getVehicleNumber(); i++) {
	    JSONObject vehicle = new JSONObject();
	    JSONObject vehicleId = new JSONObject();
	    vehicleId.put("$oid", dt.getVehicles().get(i).getId());
	    vehicle.put("vehicleId", vehicleId);
	    
	    JSONArray destinations = new JSONArray();
	    for (int j = 1; j < dt.getNodeNumber() - 1; j++) {
		JSONObject destination = new JSONObject();
		JSONObject destinationId = new JSONObject();
		destinationId.put("$oid", dt.getNodeTable().get((int)solution[i][j]));
		destination.put("destinationId", destinationId);
		
		JSONArray demands = new JSONArray();
		JSONObject demand = new JSONObject();
		demand.put("$oid", dt.getDemands().get((j+1)/2-1).getId());
		demands.add(demand);
		
		String actualDestId = dt.getNodeTable().get((int)solution[i][j]);
		if(j < dt.getNodeNumber() - 2 ) {
        		while(actualDestId.equals(dt.getNodeTable().get((int)solution[i][j+1]))) {
        		    JSONObject demand2 = new JSONObject();
        		    demand2.put("$oid", dt.getNodeTable().get((int)solution[i][j+1]));
        		    demands.add(demand2);
        		    j++;
        		}
		}
		destination.put("demandId", demands);
		
		destinations.add(destination);
	    }
	    vehicle.put("destination", destinations);
	    
	    vehicles.add(vehicle);
	}
	
	planning.put("vehicle", vehicles);
	
	PrintWriter pw = new PrintWriter("jsonFiles/test.json"); 
	pw.write(planning.toJSONString()); 		
	pw.flush(); 
	pw.close(); 
	System.out.println("OK: Fichier encrypt� !"); 
    } 
}
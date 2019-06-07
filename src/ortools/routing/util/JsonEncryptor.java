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



    @SuppressWarnings({ "unchecked", "rawtypes", "unlikely-arg-type" })
    public void writePlaning() throws FileNotFoundException {

	JSONObject jo = new JSONObject(); 
	Map m = new LinkedHashMap(1); 
	m.put("$oid", "....");
	jo.put("_id",m); 


	JSONArray vehicle = new JSONArray();

	for (int i = 0; i < dt.getVehicleNumber(); ++i) {
	    JSONObject jo1=new JSONObject(); 
	    Map m1 = new LinkedHashMap(1); 
	    m1.put("$oid",dt.getVehicles().get(i).getId()); 

	    
	    JSONArray destination = new JSONArray(); 
	    JSONObject jo2 = new JSONObject();
	    for (int j = 0; j < dt.getNodeNumber()-1; j++) {
		Map m2 = new LinkedHashMap(1); 
		m2.put("$oid", dt.getNodeTable().get((int)solution[i][j]));
		jo2.put("destinationId", m2);
	    }  

	    for(int k = 0; k < dt.getVehicleNumber(); ++k) {
		JSONArray demand = new JSONArray();
		Map m3 = new LinkedHashMap(1); 
		m3.put("$oid", dt.getDemands().get(k).getId());
		demand.add(m3);		
		jo2.put("demanId", m3);
	    }

	    jo1.put("vehicleId", m1);
	    jo1.put("destination",destination);

	    destination.add(jo2);			
	    vehicle.add(jo1);
	}


	jo.put("vehicle", vehicle); 		
	PrintWriter pw = new PrintWriter("jsonFiles/test.json"); 
	pw.write(jo.toJSONString()); 		
	pw.flush(); 
	pw.close(); 
	System.out.println(" OK: "); 
    } 
}

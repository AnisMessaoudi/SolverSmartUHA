package ortools.routing;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonEncryptor {
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void JsonEncryptor() throws FileNotFoundException {
    	
    	JSONObject jo = new JSONObject(); 
		Map m = new LinkedHashMap(1); 
		m.put("$oid", "....");
		jo.put("_id",m); 

					
		JSONArray vehicle = new JSONArray();
		
	//	for (int i = 0; i < data.vehicleNumber; ++i) {
			JSONObject jo1=new JSONObject(); 
			Map m1 = new LinkedHashMap(1); 
			m1.put("$oid","..."); 
			
			//for() {}
			JSONArray destination = new JSONArray(); 
			JSONObject jo2=new JSONObject();
			Map m2 = new LinkedHashMap(1); 
			m2.put("$oid", "..");
			jo2.put("destinationId", m2);
			
			//for() {}
			JSONArray demand = new JSONArray();
			Map m3 = new LinkedHashMap(1); 
			m3.put("$oid", "..");
			demand.add(m3);		
			jo2.put("demanId", m3);
			
			jo1.put("vehicleId", m1);
			jo1.put("destination",destination);
			
			destination.add(jo2);			
			vehicle.add(jo1);
		//}
		
		
		jo.put("vehicle", vehicle); 		
		PrintWriter pw = new PrintWriter("JSONExample.json"); 
		pw.write(jo.toJSONString()); 		
		pw.flush(); 
		pw.close(); 
		System.out.println(" OK: "); 
	} 
}
package ortools.routing.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ortools.routing.model.Demand;
import ortools.routing.model.Demand.Request;
import ortools.routing.model.Demand.State;
import ortools.routing.model.Mailbox;
import ortools.routing.model.Vehicle;
import ortools.routing.solver.DataModel;
import ortools.routing.solver.RoutingSolver;


public class JsonDecryptor {
    
    private String vehicleFile;
    private String demandFile;
    
    public JsonDecryptor(String vehicleFile, String demandFile) {
	super();
	this.vehicleFile = vehicleFile;
	this.demandFile = demandFile;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Vehicle> readVehcileFile() {
	List<Vehicle> vehicles = new ArrayList<>();
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader(vehicleFile));
	    JSONObject jsonObject = (JSONObject) obj;
	    
	    Map map;
	    Iterator<Map.Entry> iterator;
	    
	    
	    String id = "";
	    map = (Map) jsonObject.get("_id");
	    iterator = map.entrySet().iterator(); 
	    while (iterator.hasNext()) { 
		Map.Entry pair = iterator.next(); 
		id=(String) pair.getValue();
	    }
	    
	    
	    String name = (String) jsonObject.get("name");
	    
	    
	    int energy = 0;
	    map = (Map) jsonObject.get("energy");
	    iterator = map.entrySet().iterator(); 
	    while (iterator.hasNext()) { 
		Map.Entry pair = iterator.next(); 
		energy=  Integer.parseInt((String) pair.getValue());
	    }
	    
	    String destinationId="";
	    JSONObject jsonObject2 = (JSONObject) parser.parse(jsonObject.get("destination").toString());
	    for(Iterator iterator2 = jsonObject2.keySet().iterator(); iterator2.hasNext();) {
		String key = (String) iterator2.next();
		if ( key.equals("id")) {
		    JSONObject jsonObject3 = (JSONObject) parser.parse(jsonObject2.get(key).toString());
		    destinationId=jsonObject3.get("$oid").toString();
		}
	    }
	    
	    List<Mailbox> mailboxes = new ArrayList<Mailbox>();
	    JSONArray jsonArray = (JSONArray) jsonObject.get("mailboxes"); 
	    for(int i=0;i<jsonArray.size();i++) {
		JSONObject job2=(JSONObject) jsonArray.get(i);
		mailboxes.add(new Mailbox((String)job2.get("name"), (Boolean)job2.get("isEmpty")));
	    }
	    vehicles.add(new Vehicle(id, name, energy, destinationId, mailboxes));
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return vehicles;
    }
    
    
    @SuppressWarnings("rawtypes")
    public List<Demand> readDemandFile() {
	JSONParser parser = new JSONParser();
	    List<Demand> demands = new ArrayList<>();
	try {
	    Object obj = parser.parse(new FileReader(demandFile));
	    JSONObject jsonObject = (JSONObject) obj;
	    
	    String id= "";
	    int isUrgent=0;
	    Request request = null;
	    String originId="";
	    String destinationId="";
	    long deliveryTime=0;
	    State state= null;
	    JSONArray jsonArray = (JSONArray) parser.parse(jsonObject.get("Demands").toString());
	    
	    for(int i=0;i<jsonArray.size();i++) {
		JSONObject job2=(JSONObject) jsonArray.get(i);
		for(Iterator iterator2 = job2.keySet().iterator(); iterator2.hasNext();) {
		    String key = (String) iterator2.next();
		    if ( key.equals("_id")) {
			JSONObject jsonObject3 = (JSONObject) parser.parse(job2.get(key).toString());
			id=jsonObject3.get("$oid").toString();
		    }
		    if ( key.equals("isUrgent")) {
			JSONObject jsonObject3 = (JSONObject) parser.parse(job2.get(key).toString());
			isUrgent=Integer.parseInt(jsonObject3.get("$numberInt").toString());
		    }
		    if ( key.equals("request")) {
			request=  Request.valueOf(job2.get(key).toString());
		    }
		    if ( key.equals("origin")) {
			JSONObject jsonObject3 = (JSONObject) parser.parse(job2.get(key).toString());
			JSONObject job3 =(JSONObject) jsonObject3.get("_id");
			originId = job3.get("$oid").toString();
		    }
		    if ( key.equals("destination")) {
			JSONObject jsonObject3 = (JSONObject) parser.parse(job2.get(key).toString());
			JSONObject job3 =(JSONObject) jsonObject3.get("_id");
			destinationId = job3.get("$oid").toString();
		    }
		    if ( key.equals("deliveryTime")) {
			JSONObject jsonObject3 = (JSONObject) parser.parse(job2.get(key).toString());
			JSONObject job3 =(JSONObject) jsonObject3.get("$date");
			deliveryTime = Long.parseLong(job3.get("$numberLong").toString());
		    }
		    if ( key.equals("state")) {
			state= State.valueOf(job2.get(key).toString());
		    }
		}
		Demand d = new Demand(id, isUrgent, request, originId, destinationId, deliveryTime, state);
		//System.out.println(d.toString());
		demands.add(d);	    
		}
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
	return demands;
    }
    

    public static void main(String[] args) throws Exception {
	JsonDecryptor dec = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	
	DataTransformer dt = new DataTransformer(dec.readVehcileFile(), dec.readDemandFile());
	

	
//	System.out.println(dt.getNodeNumber());
//	System.out.println(dt.getVehicleNumber());
//	int [][] tab1 = dt.getRequests();
//	for (int i = 0; i < 4; i++) {
//	    System.out.println(tab1[i][0] + "-->" + tab1[i][1]);
//	}
//	int [] tab11 = dt.getVehicleEnds();
//	for (int i = 0; i < 1; i++) {
//	    System.out.println(tab11[i]);
//	}
//	long[][] tab2 = dt.getDistMatrix();
//	for (int i = 0; i < 10; i++) {
//	    for (int j = 0; j < 10; j++) {
//		System.out.print(tab2[i][j] + "\t|");
//	    }
//	    System.out.println("");
//	}
	
	DataModel data = dt.getData(); 
	
	System.out.println(data.nodeNumber);
	System.out.println(data.vehicleNumber);
	for (int i = 0; i < 4; i++) {
	    System.out.println(data.requests[i][0] + "-->" + data.requests[i][1]);
	}
	for (int i = 0; i < 1; i++) {
	    System.out.println(data.vehicleStarts[0]);
	    System.out.println(data.vehicleEnds[0]);
	}
	for (int i = 0; i < 10; i++) {
	    for (int j = 0; j < 10; j++) {
		System.out.print(data.distanceMatrix[i][j] + "\t|");
	    }
	    System.out.println("");
	}
	System.out.println("");
	for (int i = 0; i < 10; i++) {
	    for (int j = 0; j < 10; j++) {
		System.out.print(data.timeMatrix[i][j] + "\t|");
	    }
	    System.out.println("");
	}
	
	for (int j = 0; j < 10; j++) {
	    System.out.println(data.deliveryTimes[j]);
	}
	
	
	
	RoutingSolver rs = new RoutingSolver(data);
	
	rs.solve();
	rs.printSolution();
	
	Date d = new Date(1559329320285L);
	System.out.println(d.toString() + " "+ d.getTime());


	

	
	    

	   
	    
    }

}

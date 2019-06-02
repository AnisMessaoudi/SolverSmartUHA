package ortools.routing.util;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ortools.routing.model.Demand;
import ortools.routing.model.Mailbox;
import ortools.routing.model.Vehicle;


public class JsonDecryptor {
    
    private String vehicleFile;
    private String demandFile;
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Demand> demands = new ArrayList<>();
    
    public JsonDecryptor(String vehicleFile, String demandFile) {
	super();
	this.vehicleFile = vehicleFile;
	this.demandFile = demandFile;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void readVehcileFile() {
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader("jsonFiles/vehicles.json"));
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
	    System.out.println(jsonObject2);
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
    }
    
    
    @SuppressWarnings("rawtypes")
    public void readDemandFile() {
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader("jsonFiles/demands.json"));
	    JSONObject jsonObject = (JSONObject) obj;
	    
	    String id= "";
	    int isUrgent=0;
	    String request="";
	    String originId="";
	    String destinationId="";
	    long deliveryTime=0;
	    String state="";
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
			request= job2.get(key).toString();
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
			state= job2.get(key).toString();
		    }
		}
		Demand d = new Demand(id, isUrgent, request, originId, destinationId, deliveryTime, state);
		System.out.println(d.toString());
		demands.add(d);	    
		}
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
    

    public static void main(String[] args) {
	JsonDecryptor decryptor = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	
	//decryptor.readVehcileFile();
	decryptor.readDemandFile();
	    

	   
	    
    }

}

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
		System.out.println( job2.get("name")+ " : " + job2.get("isEmpty"));

	    }
	    
	    vehicles.add(new Vehicle(id, name, energy, destinationId, mailboxes));

	    	   
	    
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
    
    
    public void readDemandFile() {
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader("jsonFiles/demands.json"));
	    JSONObject jsonObject = (JSONObject) obj;
	}
	catch(Exception e) {
	    e.printStackTrace();
	}
    }
    

    public static void main(String[] args) {
	JsonDecryptor decryptor = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	
	decryptor.readVehcileFile();
	//decryptor.readDemandFile();
	    

	   
	    
    }

}

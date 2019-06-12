package ortools.routing;

import ortools.routing.solver.DataModel;
import ortools.routing.solver.RoutingSolver;
import ortools.routing.util.DataTransformer;
import ortools.routing.util.JsonDecryptor;

public class Main {

    public static void main(String[] args) throws Exception {
	JsonDecryptor dec = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	
	DataTransformer dt = new DataTransformer(dec.readVehcileFile(), dec.readDemandFile());
	DataModel data = new DataModel();
	
	dt.getData(data); 
	
	RoutingSolver rs = new RoutingSolver(data);
	
	rs.solve();
	rs.printSolution();

    }

}

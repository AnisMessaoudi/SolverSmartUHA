package ortools.routing;

import ortools.routing.solver.DataModel;
import ortools.routing.solver.Solver2;
import ortools.routing.util.DataTransformer;
import ortools.routing.util.JsonDecryptor;

public class Main {

    public static void main(String[] args) throws Exception {
	JsonDecryptor dec = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	
	DataTransformer dt = new DataTransformer(dec.readVehcileFile(), dec.readDemandFile());
	
	DataModel data = dt.getData(); 
	
	Solver2 rs = new Solver2(data);
	
	rs.solve();
	rs.printSolution();

    }

}

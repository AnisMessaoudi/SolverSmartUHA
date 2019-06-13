package fr.uha.ensisa.project.smartUHA;

import fr.uha.ensisa.project.smartUHA.solver.DataModel;
import fr.uha.ensisa.project.smartUHA.solver.RoutingSolver;
import fr.uha.ensisa.project.smartUHA.util.DataTransformer;
import fr.uha.ensisa.project.smartUHA.util.JsonDecryptor;

public class SmartUHA {

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

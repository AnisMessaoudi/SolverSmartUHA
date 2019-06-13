package fr.uha.ensisa.project.smartUHA;

import fr.uha.ensisa.project.smartUHA.solver.DataModel;
import fr.uha.ensisa.project.smartUHA.solver.RoutingSolver;
import fr.uha.ensisa.project.smartUHA.util.DataTransformer;
import fr.uha.ensisa.project.smartUHA.util.JsonDecryptor;
import fr.uha.ensisa.project.smartUHA.util.JsonEncryptor;

public class SmartUHA {

    public static void main(String[] args) throws Exception {
	
	//decrypt json vehicles and demands files
	JsonDecryptor dec = new JsonDecryptor("jsonFiles/vehicles.json", "jsonFiles/demands.json");
	DataTransformer dt = new DataTransformer(dec.readVehcileFile(), dec.readDemandFile());
	
	//transform data for the solver
	DataModel data = new DataModel();
	dt.getData(data); 
	
	//resolve the problem and print the intern solution
	RoutingSolver rs = new RoutingSolver(data);
	rs.solve();
	rs.printSolution();
	
	//transform the intern solution and put it in output planing.json
	JsonEncryptor enc = new JsonEncryptor(dt, rs.getSolution());
	enc.writePlaning();

    }

}

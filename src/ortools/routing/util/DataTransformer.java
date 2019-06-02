package ortools.routing.util;

import java.util.ArrayList;
import java.util.List;

import ortools.routing.model.Demand;
import ortools.routing.model.Vehicle;

public class DataTransformer {
    
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Demand> demands = new ArrayList<>();
    
    public int getVehicleNumber() {
	return vehicles.size();
    }
    
    

}

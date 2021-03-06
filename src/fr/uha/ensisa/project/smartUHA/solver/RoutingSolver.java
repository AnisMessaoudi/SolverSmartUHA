package fr.uha.ensisa.project.smartUHA.solver;

import java.util.Date;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.IntVar;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.Solver;
import com.google.ortools.constraintsolver.main;

import fr.uha.ensisa.project.smartUHA.util.DataTransformer;
import fr.uha.ensisa.project.smartUHA.util.JsonDecryptor;
import fr.uha.ensisa.project.smartUHA.util.Utils;

public class RoutingSolver {

static {System.loadLibrary("jniortools");}
    
    private final DataModel data;
    private RoutingIndexManager manager;
    private RoutingModel model;
    private Assignment solution;
    
    public RoutingSolver(DataModel data) {
	this.data = data;
    }
    
    
    public void solve() throws Exception {
	
	// Create Routing Index Manager
	manager = new RoutingIndexManager(data.distanceMatrix.length, data.vehicleNumber,
		data.vehicleStarts, data.vehicleEnds);

	// Create Routing Model.
	model = new RoutingModel(manager);
	
	// Create and register a transit callback.
	final int transitCallbackIndex =
		model.registerTransitCallback((long fromIndex, long toIndex) -> {
		    // Convert from routing variable Index to user NodeIndex.
		    int fromNode = manager.indexToNode(fromIndex);
		    int toNode = manager.indexToNode(toIndex);
		    return data.distanceMatrix[fromNode][toNode];
		});
	
	// Define cost of each arc.
	model.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
	
	// Add Distance constraint.
	model.addDimension(transitCallbackIndex, // transit callback index
		0, // no slack
		Utils.MaxDistance, // vehicle maximum travel distance
		true, // start cumul to zero
		"Distance");
	RoutingDimension distanceDimension = model.getMutableDimension("Distance");
	distanceDimension.setGlobalSpanCostCoefficient(100); // prioritize distance dimension
	
	// Define Transportation Requests.
	Solver solver = model.solver();
	for (int[] request : data.requests) {
	    long pickupIndex = manager.nodeToIndex(request[0]);
	    long deliveryIndex = manager.nodeToIndex(request[1]);
	    model.addPickupAndDelivery(pickupIndex, deliveryIndex);
	    
	    // pickup and delivery location need to be reached by the same vehicle
	    solver.addConstraint(solver.makeEquality(model.vehicleVar(pickupIndex),
		    model.vehicleVar(deliveryIndex))); 
	    // pickup location need to be explore before the delivery one
	    solver.addConstraint(solver.makeLessOrEqual(distanceDimension.cumulVar(pickupIndex),
		    distanceDimension.cumulVar(deliveryIndex)));
	}
	
	
	// Create and register a time callback.
	final int timeCallbackIndex =
		model.registerTransitCallback((long fromIndex, long toIndex) -> {
		    // Convert from routing variable Index to user NodeIndex.
		    int fromNode = manager.indexToNode(fromIndex);
		    int toNode = manager.indexToNode(toIndex);
		    return data.timeMatrix[fromNode][toNode];
		});
	//Create time dimension
	model.addDimension(timeCallbackIndex,
		Utils.MaxWaitingTime, // max waiting time on a location for a vehicle
		Utils.MaxTime, // max time for each vehicle's route
		false, // dont start to 0
		"Time");
	RoutingDimension timeDimension = model.getMutableDimension("Time");
	// Add time window constraints for each pickup location.
	for (int i = 0; i < data.vehicleNumber; ++i) {
	    long index = model.start(i);
	    timeDimension.cumulVar(index).setValue(data.deliveryTimes[i]);
	}
	for (int i = data.vehicleNumber; i < data.deliveryTimes.length; ++i) {
	    long index = manager.nodeToIndex(i);
	    if (data.deliveryTimes[i] != 0)
		timeDimension.cumulVar(index).setValue(data.deliveryTimes[i]);
	}
	
	// Add Capacities constraint.
	// for small letters
	final int smallLetterCallbackIndex = model.registerUnaryTransitCallback((long fromIndex) -> {
	    int fromNode = manager.indexToNode(fromIndex);
	    return data.smallLetterDemands[fromNode];
	});
	model.addDimensionWithVehicleCapacity(smallLetterCallbackIndex, 0, // null capacity slack
		data.vehicleSmallLetterCapacity, // vehicle maximum capacities
		true, // start cumul to zero
		"smallLetterCapacity");
	//for large letter
	final int largeLetterCallbackIndex = model.registerUnaryTransitCallback((long fromIndex) -> {
	    // Convert from routing variable Index to user NodeIndex.
	    int fromNode = manager.indexToNode(fromIndex);
	    return data.largeLetterDemands[fromNode];
	});
	model.addDimensionWithVehicleCapacity(largeLetterCallbackIndex, 0, // null capacity slack
		data.vehicleLargeLetterCapacity, // vehicle maximum capacities
		true, // start cumul to zero
		"largeLetterCapacity");
	//for cargo
	final int cargoCallbackIndex = model.registerUnaryTransitCallback((long fromIndex) -> {
	    // Convert from routing variable Index to user NodeIndex.
	    int fromNode = manager.indexToNode(fromIndex);
	    return data.cargoDemands[fromNode];
	});
	model.addDimensionWithVehicleCapacity(cargoCallbackIndex, 0, // null capacity slack
		data.vehicleCargoCapacity, // vehicle maximum capacities
		true, // start cumul to zero
		"cargoCapacity");
	
	
	//Add energy constraint
	final int energyCallbackIndex =
		model.registerTransitCallback((long fromIndex, long toIndex) -> {
		    // Convert from routing variable Index to user NodeIndex.
		    int fromNode = manager.indexToNode(fromIndex);
		    int toNode = manager.indexToNode(toIndex);
		    return data.energyMatrix[fromNode][toNode];
		});
	model.addDimension(energyCallbackIndex, 0, // no slack
		Utils.MaxEnergy, // maximum energy's vehicle
		true, // start to 0
		"Energy");
	RoutingDimension energyDimension = model.getMutableDimension("Energy");
	for (int i = 0; i < data.vehicleNumber; i++) {
	    long index = model.start(i);
	    // for each vehicle, journey cumul energy should be less than the remaining energy
	    energyDimension.cumulVar(index).isLessOrEqual(data.vehicleEnergy[i]);
	}
	
	
	
	// Setting first solution heuristic.
	RoutingSearchParameters searchParameters =
		main.defaultRoutingSearchParameters()
		.toBuilder()
		.setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
		.build();

	// Solve the problem.
	solution = model.solveWithParameters(searchParameters);
    }
    
    /* return path (succession of location) for each vehicle  */
    public long[][] getSolution() throws Exception {
	long[][] sol = new long[data.vehicleNumber][data.nodeNumber - 1];
	if (solution != null) {
	    for (int i = 0; i < data.vehicleNumber; i++) {
		long index = model.start(i);
		int j=0;
		while (!model.isEnd(index)) {
		    long nodeIndex = manager.indexToNode(index);
		    sol[i][j]= nodeIndex;
		    index = solution.value(model.nextVar(index));
		    j++;
		}
	    }
	    return sol;
	}
	else 
	    throw new Exception("notFoundSolution");
	
	
    }
    
    /* print the succession of location with some info (date, capacity) for each vehicle */
    public void printSolution() throws Exception {
	if (solution != null) {
	    RoutingDimension timeDimension = model.getMutableDimension("Time");
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < data.vehicleNumber; ++i) {
		long index = model.start(i);
		long routeDistance = 0;
		long smallLoad  = 0;
		long largeLoad = 0;
		long cargoLoad = 0;
		sb.append("Route for Vehicle " + i + ":\n");
		while (!model.isEnd(index)) {
		    long nodeIndex = manager.indexToNode(index);
		    IntVar timeVar = timeDimension.cumulVar(index);
		    Date date = new Date(solution.value(timeVar));
		    smallLoad+= data.smallLetterDemands[(int) nodeIndex];
		    largeLoad+= data.largeLetterDemands[(int) nodeIndex];
		    cargoLoad+= data.cargoDemands[(int) nodeIndex];
		    sb.append(nodeIndex);
		    sb.append(" : date(" + date + "),");
		    sb.append(" \n\t smallCapacite(" + smallLoad + "/" + data.vehicleSmallLetterCapacity[i] + "),");
		    sb.append(" \n\t largeCapacite(" + largeLoad + "/" + data.vehicleLargeLetterCapacity[i] + "),");
		    sb.append(" \n\t cargoCapacite(" + cargoLoad + "/" + data.vehicleCargoCapacity[i] + ")");
		    sb.append("  ->\n");
		    long previousIndex = index;
		    index = solution.value(model.nextVar(index));
		    routeDistance += model.getArcCostForVehicle(previousIndex, index, i);
		}
		sb.append("\nDistance of the route: " + routeDistance + "m\n");
	    }
	    System.out.println(sb.toString());
	}
	else {
	    throw new Exception("notFoundSolution");
	}
    }
}

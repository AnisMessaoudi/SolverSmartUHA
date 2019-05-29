package ortools.routing.solver;

import com.google.ortools.constraintsolver.Assignment;
import com.google.ortools.constraintsolver.FirstSolutionStrategy;
import com.google.ortools.constraintsolver.RoutingDimension;
import com.google.ortools.constraintsolver.RoutingIndexManager;
import com.google.ortools.constraintsolver.RoutingModel;
import com.google.ortools.constraintsolver.RoutingSearchParameters;
import com.google.ortools.constraintsolver.Solver;
import com.google.ortools.constraintsolver.main;

public class Solver2 {

static {System.loadLibrary("jniortools");}
    
    private final DataModel data;
    private RoutingIndexManager manager;
    private RoutingModel model;
    private Assignment solution;
    
    public Solver2(DataModel data) {
	this.data = data;
    }
    
    
    public void solve() throws Exception {
	// Create Routing Index Manager
	manager = new RoutingIndexManager(data.nodeNumber, data.vehicleNumber, data.vehicleStarts, data.vehicleEnds);

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
		3000, // vehicle maximum travel distance
		true, // start cumul to zero
		"Distance");
	RoutingDimension distanceDimension = model.getMutableDimension("Distance");
	distanceDimension.setGlobalSpanCostCoefficient(100);
	
	// Add Capacity constraint for letters.
	final int letterDemandCallbackIndex = model.registerUnaryTransitCallback((long fromIndex) -> {
	    // Convert from routing variable Index to user NodeIndex.
	    int fromNode = manager.indexToNode(fromIndex);
	    return data.letterDemands[fromNode];
	});
	model.addDimensionWithVehicleCapacity(letterDemandCallbackIndex, 0, // null capacity slack
		data.vehicleLetterCapacity, // vehicle maximum capacities
		true, // start cumul to zero
		"LetterCapacity");
	
	// Add Capacity constraint for letters.
	final int cargoDemandCallbackIndex = model.registerUnaryTransitCallback((long fromIndex) -> {
	    // Convert from routing variable Index to user NodeIndex.
	    int fromNode = manager.indexToNode(fromIndex);
	    return data.cargoDemands[fromNode];
	});
	model.addDimensionWithVehicleCapacity(cargoDemandCallbackIndex, 0, // null capacity slack
		data.vehicleLetterCapacity, // vehicle maximum capacities
		true, // start cumul to zero
		"CargoCapacity");

	// Define Transportation Requests.
	Solver solver = model.solver();
	for (int[] request : data.requests) {
	    long pickupIndex = manager.nodeToIndex(request[0]);
	    long deliveryIndex = manager.nodeToIndex(request[1]);
	    model.addPickupAndDelivery(pickupIndex, deliveryIndex);
	    solver.addConstraint(
		    solver.makeEquality(model.vehicleVar(pickupIndex), model.vehicleVar(deliveryIndex)));
	    solver.addConstraint(solver.makeLessOrEqual(
		    distanceDimension.cumulVar(pickupIndex), distanceDimension.cumulVar(deliveryIndex)));
	}

	// Setting first solution heuristic.
	RoutingSearchParameters searchParameters =
		main.defaultRoutingSearchParameters()
		.toBuilder()
		.setFirstSolutionStrategy(FirstSolutionStrategy.Value.PARALLEL_CHEAPEST_INSERTION)
		.build();

	// Solve the problem.
	solution = model.solveWithParameters(searchParameters);
	
    }
    
    
//    private void printSolution() {
//	long totalDistance = 0;
//	for (int i = 0; i < data.vehicleNumber; ++i) {
//	    long index = model.start(i);
//	    System.out.println("Route for Vehicle " + i + ":");
//	    long routeDistance = 0;
//	    long routeLoad = 0;
//	    String route = "";
//	    while (!model.isEnd(index)) {
//		long nodeIndex = manager.indexToNode(index);
//		routeLoad += data.demands[(int) nodeIndex];
//		route += nodeIndex + " Load(" + routeLoad + ") -> ";
//		long previousIndex = index;
//		index = solution.value(model.nextVar(index));
//		routeDistance += model.getArcCostForVehicle(previousIndex, index, i);
//	    }
//	    System.out.println(route + manager.indexToNode(index));
//	    System.out.println("Distance of the route: " + routeDistance + "m\n");
//	    totalDistance += routeDistance;
//	}
//	System.out.println("Total Distance of all routes: " + totalDistance + "m");
//    }
    

}

package fr.uha.ensisa.project.smartUHA.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


public class Utils {
    /*** All of following informations will be probably different in the project,
     *   we used this due to a lack of data ***/
    
    /* maximum energy a vehicle can have (battery full charge) */
    public static long MaxEnergy = Long.MAX_VALUE;
    
    /* maximum distance a vehicle can travel */
    public static long MaxDistance = Long.MAX_VALUE;
    
    /* maximum duration time of a route */
    public static long MaxTime = Long.MAX_VALUE;
    
    /* maximum time a vehicle can wait at a location */
    public static long MaxWaitingTime = Long.MAX_VALUE;

    /* distance, energy consumed and time between positions (index node are on the globalNodeTable */
    public static long [][] globalDistanceMatrix = {
	    {0, 160, 287, 264},
	    {160, 0, 133, 116},
	    {287, 133, 0, 33},
	    {264, 116, 33, 0},
    };
    public static long [][] globalEnergyMatrix= {
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
    };
    public static long [][] globalTimeMatrix= {
	    {0, 57, 103, 95},
	    {57, 0, 48, 42},
	    {103, 48, 0, 12},
	    {95, 42, 12, 0},
    };
    
    /* map between index's and id's location */
    public static Map<Integer, String> globalNodeTable = new HashMap<>();
    
    static {
	globalNodeTable.put(0, "5ce80b22293b8439240d18d4"); //Lumiere
	globalNodeTable.put(1, "5ce80b26293b8439240d18d6"); //Werner
	globalNodeTable.put(2, "5cec2a33309a532ebc59dbb6"); //FLSH
	globalNodeTable.put(3, "5cec2a4edaa0821eec2d9802"); //BU
    }
    

}

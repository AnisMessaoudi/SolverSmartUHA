package ortools.routing.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Utils {

    public static int [][] globalDistanceMatrix = {
	    {0, 160, 287, 264},
	    {160, 0, 133, 116},
	    {287, 133, 0, 33},
	    {264, 116, 33, 0},
    };
    public static int [][] globalEnergyMatrix= {
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
	    {0, 0, 0, 0},
    };
    public static double [][] globalTimeMatrix= {
	    {0, 57.55, 103.26, 94.96},
	    {57.55, 0, 47.84, 41.72},
	    {103.26, 47.84, 0, 11.87},
	    {94.96, 41.72, 11.87, 0},
    };
    
    public static Map<Integer, String> globalNodeTable = new HashMap<>();
    static {
	globalNodeTable.put(0, "5ce80b22293b8439240d18d4"); //Lumiere
	globalNodeTable.put(1, "5ce80b26293b8439240d18d6"); //Werner
	globalNodeTable.put(2, "5cec2a33309a532ebc59dbb6"); //FLSH
	globalNodeTable.put(3, "5cec2a4edaa0821eec2d9802"); //BU
    }
    
    public static int getIndex(String id) {
	for(Entry<Integer, String> entry : globalNodeTable.entrySet()) {
	    if(entry.getValue() .equals(id))
		return entry.getKey();
	}
	return -1;
    
    }
    
    public static String getId(int index) {
	if (globalNodeTable.containsKey(index)){
	    return globalNodeTable.get(index);
	}
	return null;
    }
}

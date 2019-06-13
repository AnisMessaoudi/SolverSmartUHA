package fr.uha.ensisa.project.smartUHA.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;


public class Utils {
    
    public static long MaxEnergy = Long.MAX_VALUE;
    public static long MaxDistance = Long.MAX_VALUE;
    public static long MaxTime = Long.MAX_VALUE;
    public static long MaxWaitingTime = Long.MAX_VALUE;

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
    
    public static String durationTime(long milliseconds) {
	long uptime = milliseconds;
	long days = TimeUnit.MILLISECONDS.toDays(uptime);
	uptime -= TimeUnit.DAYS.toMillis(days);

	long hours = TimeUnit.MILLISECONDS.toHours(uptime);
	uptime -= TimeUnit.HOURS.toMillis(hours);

	long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
	uptime -= TimeUnit.MINUTES.toMillis(minutes);

	long seconds = TimeUnit.MILLISECONDS.toSeconds(uptime);

	StringBuilder sb = new StringBuilder();
	sb.append(days);
	sb.append("d ");
	sb.append(minutes);
	sb.append("min ");
	sb.append(seconds);
	sb.append("sec ");
	return sb.toString();
    }
}

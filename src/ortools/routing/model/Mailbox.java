package ortools.routing.model;

public class Mailbox {

    private String name;
    private boolean isEmpty ;
    
    public Mailbox(String name, Boolean isEmpty) {
	this.name = name;
	this.isEmpty = isEmpty;
    }

    @Override
    public String toString() {
	return "Mailbox [name=" + name + ", isEmpty=" + isEmpty + "]";
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
    
    
    

}

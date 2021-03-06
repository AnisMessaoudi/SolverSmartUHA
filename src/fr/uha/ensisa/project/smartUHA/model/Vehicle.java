package fr.uha.ensisa.project.smartUHA.model;

import java.util.List;


public class Vehicle {
	private String id;
	private String name;
	private int energy;
	private String destinationId;
	private List<Mailbox> mailboxes;
	
	public Vehicle(String id, String name, int energy, String destinationId, List<Mailbox> mailboxes) {
	    super();
	    this.id = id;
	    this.name = name;
	    this.energy = energy;
	    this.destinationId = destinationId;
	    this.mailboxes = mailboxes;
	}

	@Override
	public String toString() {
	    return "Vehicle [id=" + id + ", name=" + name + ", energy=" + energy + ", destinationId=" + destinationId
		    + ", mailboxes=" + mailboxes + "]";
	}

	public String getId() {
	    return id;
	}

	public String getDestinationId() {
	    return destinationId;
	}

	public List<Mailbox> getMailboxes() {
	    return mailboxes;
	}

	public int getEnergy() {
	    return energy;
	    
	}
}
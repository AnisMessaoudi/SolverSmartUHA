package fr.uha.ensisa.project.smartUHA.model;

public class Mailbox {

	private String name;
	private int isEmpty ;

	public Mailbox(String name, int isEmpty) {
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

	public int isEmpty() {
		return isEmpty;
	}

	public int getIsEmpty() {
		return isEmpty;
	}




}

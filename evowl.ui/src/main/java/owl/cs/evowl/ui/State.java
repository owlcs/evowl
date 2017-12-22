package owl.cs.evowl.ui;

public enum State {
	
	ADDURL("add"), ALLONTOLOGIES("ontologies"), EVOWLUATION("metrics"), GROUPS("groups");

	private String name;
	
	State(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}

package com.elevator;

public abstract class Component {

	private String id;
	
	public Component(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
}

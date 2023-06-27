// **************************************************
// Class: Storage
// Author: Gabriel Lindo
// Created: 04/04/2023
// Modified:
//
// Purpose: Retain values between user inputs from the GUI
//
// Attributes: -storedComponent: retain a component object between the selection of the first node of a wire and the second node
//
// Methods: 
//
// **************************************************
package circuitCreator;


public class Storage {
	private Component storedComponent;
	private double[] startCoors;

	public Component getStoredComponent() {
		return storedComponent;
	}

	public void setStoredComponent(Component storedComponent) {
		this.storedComponent = storedComponent;
	}

	public double[] getStartCoors() {
		return startCoors;
	}

	public void setStartCoors(double[] startCoors) {
		this.startCoors = startCoors;
	}
}

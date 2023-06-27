// **************************************************
// Class: Component
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified: 04/03/2023 Gabriel Lindo - adjusted constructor to assign type without user input, and replaced icon attribute with iconLocation
//			 04/04/2023 Gabriel Lindo - added printComponent method
//
// Purpose: Act as the parent class for electrical components in Circuit Creator
//
// Attributes: #resistance: int - the component's resistance to the flow of electricity
//			   #voltage: int - the electromotive force experienced by a component
//			   #current: int - the measure of electrical flow through a component
//			   #type: String - the type of component
//			   #iconLocation: String - the relative path of the image file associated with that component
//
// Methods: #calcResistance(): int - calculate the resistance using voltage and current
//			#calcVoltage(): int - calculate the voltage using the resistance and current
//			#calcCurrent(): int - calculate the current using the voltage and the resistance
//			#printComponent(): void - print attributes of the object, except iconLocation
//
// **************************************************
package circuitCreator;


public class Component {
	protected double resistance;
	protected double voltage;
	protected double current;
	protected String type;
	protected String iconLocation;
	
	protected Component() {
		type = "Generic";
		iconLocation = "/assets/empty.png";
	}
	
	protected void calcResistance() {
		resistance = roundUp(voltage / current);
	}
	
	protected void calcVoltage() {
		voltage = roundUp(resistance * current);
	}
	
	protected void calcCurrent() {
		current = roundUp(voltage / resistance);
	}
	
	protected double roundUp(double value) {
		value *= 10;
		value = Math.round(value);
		value /= 10;
		
		return value;
	}
	
	protected void isActive() {
	}
	
	protected String printComponent() {
		return type + ": " + resistance + "R, " + voltage + "V, " + current + "A";
	}
	
	//Setters and Getters
	protected double getResistance() {
		return resistance;
	}

	protected void setResistance(double resistance) {
		this.resistance = roundUp(resistance);
	}

	protected double getVoltage() {
		return voltage;
	}

	protected void setVoltage(double voltage) {
		this.voltage = roundUp(voltage);
	}

	protected double getCurrent() {
		return current;
	}

	protected void setCurrent(double current) {
		this.current = roundUp(current);
	}
	
	protected void setRVC(double resistance, double voltage, double current) {
		this.resistance = roundUp(resistance);
		this.voltage = roundUp(voltage);
		this.current = roundUp(current);
	}

	protected String getType() {
		return type.toUpperCase();
	}

	protected String getIconLocation() {
		return iconLocation;
	}
}

// **************************************************
// Class: Resistor
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Extend the Component class to create the resistor object which acts as an electrical load.
//			Also acts the parent class for the Light class
//
// Attributes: #isActive: boolean - tracks whether the Resistor has current flow and if the component is correctly biased
//
// Methods: #verifyIsActive(): void - checks the current and voltage directions of the component and updates isActive as necessary
//
// **************************************************
package circuitCreator;

public class Resistor extends Component {
	protected boolean isActive;
	
	
	public Resistor(double resistance) {
		type = "resistor";
		iconLocation = "/assets/resistor.png";
		this.resistance = resistance;
	}
	
	@Override
	protected void isActive() {
		if ((current > 0) && (voltage > 0)) {
			iconLocation = "/assets/active resistor.png";
		} else {
			iconLocation = "/assets/resistor.png";
		}
		
	}
}

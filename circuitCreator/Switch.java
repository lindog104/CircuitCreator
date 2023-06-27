// **************************************************
// Class: Switch
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Extends the Component class to create a way to turn on or off the flow of current
//
// Attributes: -defaultState: String - tracks whether the switch is defaultly OPEN or CLOSED
//
// Methods: +switchState(): void - changes the resistance of the Switch to represent its state, 1 if CLOSED, inf if OPEN
//			+convertStateToBool(): boolean - converts the state from a String to a boolean, true if CLOSED, false if OPEN
//
// **************************************************
package circuitCreator;

public class Switch extends Component {
	private String currentState;

	public Switch(String state) {
		type = "switch";
		currentState = state.toUpperCase();
		assignIcon();
		initializeResistance();
	}

	private void initializeResistance() {

		if (currentState.equals("CLOSED")) {

			resistance = 0.001;
		} else {

			resistance = 1000;
		}
	}

	public void switchState(String newState) {

		if (newState.equals("CLOSED")) {

			resistance = 0.001;
			currentState = "CLOSED";
		} else {

			resistance = 1000;
			currentState = "OPEN";
		}
		assignIcon();
	}

	@Override
	public String printComponent() {
		return type + ": " + currentState;
	}
	
	public boolean isOpen() {
		if (currentState.equals("OPEN")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void assignIcon() {
		if (currentState.equals("OPEN")) {
			iconLocation = "/assets/switch open.png";
		} else {
			iconLocation = "/assets/switch closed.png";
		}
	}
}

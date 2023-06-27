// **************************************************
// Class: Wire
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Extends the Component class to create the wire class which will act as a linked list
//			to show the relationship between the components in the circuit
//
// Attributes: -nodes: Component[] - contains the component objects that this wire connects
//			   -potential: String - tracks whether this wire is POSITIVE or NEGATIVE potential
//
// Methods: 
//
// **************************************************
package circuitCreator;

import javafx.scene.shape.Line;

public class Wire extends Component {
	private Component[] nodes;
	private String potential;
	private Line line;
	
	public Wire(Component[] nodes) {
		type = "WIRE";
		voltage = -1.0;
		this.nodes = nodes;
	}
	
	public Wire(Component nodeOne, Component nodeTwo) {
		type = "WIRE";
		voltage = -1.0;
		Component[] newNodes = {nodeOne, nodeTwo};
		
		nodes = newNodes;
	}
	
	public void swapNodes() {
		Component temp;
		
		temp = nodes[0];
		nodes[0] = nodes[1];
		nodes[1] = temp;
	}
	
	public boolean containsSource() {
		boolean result = false;
		
		if ( (nodes[0] instanceof Source) || (nodes[1] instanceof Source) ) {
			result = true;
		}
		
		return result;
	}
	
	public boolean containsGround() {
		boolean result = false;
		
		if ( (nodes[0] instanceof Ground) || (nodes[1] instanceof Ground) ) {
			result = true;
		}
		
		return result;
	}
	
	@Override
	public String printComponent() {
		return type +" connecting "+ nodes[0].getType() +"&"+ nodes[1].getType() + ": " + voltage;
	}

	//Setters and Getters
	public Component[] getNodes() {
		return nodes;
	}

	public void setNodes(Component[] nodes) {
		this.nodes = nodes;
	}
	
	public Component getNode(int index) {
		return nodes[index];
	}
	
	public void setNode(int index, Component component) {
		nodes[index] = component;
	}

	public String getPotential() {
		return potential;
	}

	public void setPotential(String potential) {
		this.potential = potential;
	}
	
	public Line getLine() {
		return line;
	}
	
	public void setLine(Line line) {
		this.line = line;
	}
	
}

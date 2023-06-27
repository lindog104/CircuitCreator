// **************************************************
// Class: Source
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Extends the Component class to create the Source object. It is identical to the Component class.
//
// Attributes: 
//
// Methods: 
//
// **************************************************
package circuitCreator;


public class Source extends Component {

	public Source(double voltage) {
		type = "source";
		iconLocation = "/assets/source.png";
		this.voltage = voltage;
	}

	
}

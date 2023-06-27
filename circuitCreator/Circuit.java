// **************************************************
// Class: Circuit
// Author: Gabwiel Lindo
// Created: 03/24/2023
// Modified: 04/04/2023 Gabwiel Lindo - added Wire functionality and started series circuit computational functionality
//
// Purpose: Handle the mathematical operations of the electrical circuit, store and update the Component array that
//			represents the component positions in the circuit. Read and write .csv files based on the circuit array.
//
// Attributes: -circuit: Component[][] - track the position of each component in the workspace
//			   -size: int - track the size of the circuit array (will be used if I can incoporate different sizes of workspace)
//			   -defaultResistance: int - the resistance provided to all loads upon initial creation
//			   -sourceVoltage: int - the voltage of the source which will be used in almost all calculations
//
// Methods: +computePotentials(): void - perform the operations to determine and assign the POSITIVE or NEGATIVE charge of each wire
//			+addComponent(int, int, String): void - create the corresponding object and place it in the circuit array, updating other components as necessary
//			+removeComponent(int, int): void - remove the corresponding object from the circuit array, updating other objects as necessary
//			+updateComponent(): void - adjust the values of an object from the circuit array, updating other objects as necessary
//			+createCircuitFile(): File - writes the circuit array to a .csv file
//			+readCircuitFile(File): void - read a given .csv file and creates a circuit array that corresponds
//			+checkAdjacentComponents(): void - update the relevant components after a component has been changed
//			+isCircuitComplete(): boolean - returns true if starting at the source there is a complete path back to the source
//
// **************************************************
package circuitCreator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Line;

public class Circuit {
	protected int size = 5;
	protected Component[][] circuit;
	protected double sourceVoltage = 24.0;
	protected double defaultResistance = 10.0;
	protected String defaultColor = "YELLOW";
	protected String defaultState = "OPEN";
	protected WireEvaluator we;

	protected Circuit() {
		circuit = new Component[size][size];
	}

	protected void computePotentials() {
		we.assignPotentials();
	}

	protected void addComponent(int[] position, String componentType) {
		if (componentType.equals("SOURCE")) {
			removeOldSource();
		} else if (componentType.equals("GROUND")) {
			removeOldGround();
		}
		circuit[position[0]][position[1]] = readComponentType(componentType);
	}

	protected ArrayList<Line> removeComponent(int[] position) {
		ArrayList<Line> linesToRemove = we.removeWires(circuit[position[0]][position[1]]);

		circuit[position[0]][position[1]] = null;
		computeCircuit();

		return linesToRemove;
	}

	protected ArrayList<Line> getLines() {
		ArrayList<Line> lines = new ArrayList<>();

		for (int i = 0; i < we.getWires().size(); i++) {
			lines.add(we.getWires().get(i).getLine());
		}

		return lines;
	}

	protected boolean addWire(Wire wire) {
		return we.addWire(wire);
	}

	protected boolean computeCircuit() {

		System.out.println("Parent Called");
		return false;
	}

	protected boolean createCircuitFile(String filename) {
		try {
			File file = new File(filename + ".csv");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			bw.write("" + circuit.length);
			bw.newLine();
			bw.write(sourceVoltage + "," + defaultResistance + "," + defaultState + "," + defaultColor);
			bw.newLine();

			for (int r = 0; r < circuit.length; r++) {
				for (int c = 0; c < circuit[0].length; c++) {
					if (circuit[r][c] != null) {
						bw.write(r + "," + c + "," + circuit[r][c].getType() + "," + circuit[r][c].getResistance() + ","
								+ circuit[r][c].getVoltage() + "," + circuit[r][c].getCurrent());
						bw.newLine();
					} else {
						bw.write(r + "," + c + "," + "EMPTY");
						bw.newLine();
					}
				}
			}

			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	protected boolean readCircuitFile(String filename) {
		try {
			File file = new File(filename + ".csv");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String row = br.readLine();
			String[] cols = row.split(",", 4);
			this.size = Integer.parseInt(cols[0]);
			this.circuit = new Component[size][size];

			row = br.readLine();
			cols = row.split(",", 4);
			this.sourceVoltage = Double.parseDouble(cols[0]);
			this.defaultResistance = Double.parseDouble(cols[1]);
			this.defaultState = cols[2];
			this.defaultColor = cols[3];

			while ((row = br.readLine()) != null) {
				cols = row.split(",", 6);
				if (!(cols[2].equals("EMPTY"))) {
					int r = Integer.parseInt(cols[0]);
					int c = Integer.parseInt(cols[1]);
					Component readComponent = readComponentType(cols[2]);
					readComponent.setRVC(Double.parseDouble(cols[3]), Double.parseDouble(cols[4]),
							Double.parseDouble(cols[5]));
					circuit[r][c] = readComponent;
					System.out.println(r + "," + c + ": " + readComponent.printComponent());
				}
			}

			computeCircuit();

			br.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected Component readComponentType(String type) {
		switch (type) {
		case "SOURCE":

			return new Source(sourceVoltage);
		case "RESISTOR":

			return new Resistor(defaultResistance);
		case "LIGHT":

			return new Light(defaultResistance, defaultColor);
		case "SWITCH":

			return new Switch(defaultState);
		case "GROUND":
			
			return new Ground();
		default:

			return new Component();
		}

	}

	protected void resetComponent(Component component) {
		if (component instanceof Source) {
			component.setVoltage(sourceVoltage);
			component.setCurrent(0);
			component.setResistance(0);
		} else if (component instanceof Light) {
			((Light) component).setColor(defaultColor);
			component.setVoltage(0);
			component.setCurrent(0);
			component.setResistance(defaultResistance);
		} else if (component instanceof Resistor) {
			component.setVoltage(0);
			component.setCurrent(0);
			component.setResistance(defaultResistance);
		} else if (component instanceof Switch) {
			component.setVoltage(0);
			component.setCurrent(0);
			((Switch) component).switchState(defaultState);
		}
	}

	protected void removeOldSource() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (circuit[i][j] instanceof Source) {
					circuit[i][j] = null;
				}
			}
		}
	}
	
	protected void removeOldGround() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (circuit[i][j] instanceof Ground) {
					circuit[i][j] = null;
				}
			}
		}
	}

	// Setters and Getters
	protected Component getComponent(int[] position) {
		return circuit[position[0]][position[1]];
	}

	protected Component getComponent(int row, int col) {
		return circuit[row][col];
	}

	protected int getSize() {
		return size;
	}

	protected void setSize(int size) {
		this.size = size;
	}

	protected double getDefaultResistance() {
		return defaultResistance;
	}

	protected void setDefaultResistance(double defaultResistance) {
		this.defaultResistance = defaultResistance;
	}

	protected double getSourceVoltage() {
		return sourceVoltage;
	}

	protected void setSourceVoltage(double sourceVoltage) {
		this.sourceVoltage = sourceVoltage;
	}

	protected String getDefaultColor() {
		return defaultColor;
	}

	protected void setDefaultColor(String defaultColor) {
		this.defaultColor = defaultColor;
	}

	protected String getDefaultState() {
		return defaultState;
	}

	protected void setDefaultState(String defaultState) {
		this.defaultState = defaultState;
	}
}

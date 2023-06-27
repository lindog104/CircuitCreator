// **************************************************
// Class: Controller
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified: 03/27/2023 Gabriel Lindo - Implemented the selection pane and appearance of objects on the workspace as images
//			 04/04/2023 Gabriel Lindo - Implemented Wire functionality
//
// Purpose: Act as the GUI controller class, respond to the events from the Workspace stage and update it accordingly
//
// Attributes: -circuit: Circuit - store an instance of the Circuit class to act as the backend for the GUI
//			   -chosenComponent: Component - store an instance of a Component child class to represent the user's choice
//			   -store: Storage - store an instance of the Storage class
//			   -firstNode: boolean - track whether the next selected component will be the first node of a new wire
//
// Methods: +launchMenu(ActionEvent): void - create the Menu stage when the user clicks the Options button
//			+selectSource(): void - assign chosenComponent to a Source object when the user clicks on Source in the selection pane
//			+selectResistor(): void - assign chosenComponent to a Resistor object when the user clicks on Resistor in the selection pane
//			+selectLight(): void - assign chosenComponent to a Light object when the user clicks on Light in the selection pane
//			+selectSwitch(): void - assign chosenComponent to a Switch object when the user clicks on Switch in the selection pane
//			+placeWire(MouseEvent): void - assemble a Wire object based on the user's next two left click selected components if they have toggled the wire creation switch
//			+updateTile(MouseEvent): void - handle the tile events to either change an existing component, generate a new one, or construct a Wire
//			-newComponent(MouseEvent): void - update the button with the image of the new component and inform the Circuit class to generate a component at a designated position when the user right clicks on a tile
//			-changeComponent(MouseEvent): void - present the user with the options to update a component's values when the user left clicks on an existing component
//			+presentPotentials(ActionEvent): void - change the color of each wire to indicate its potential, RED for POSITIVE and GREEN for NEGATIVE, when the user clicks the Potential button
//			+presentCircuit(ActionEvent): void 
//			+updateDefaults(ActionEvent): void - update the default resistance and/or source voltage values when the user chooses the option on the Menu stage
//			+printCircuitToFile(ActionEvent): void - alert the Circuit class to write the circuit to a file when the user clicks the Save button
//			+loadCircuitFromFile(ActionEvent): void - alert the Circuit class to read the circuit from a file when the user clicks the Load button
//			+printTile(ActionEvent): void - print the selected tile's position in the GridPane array when clicked
//			-getTilePosition(MouseEvent): int[] - convert the GridPane indices of the given event target into an integer array
//
// **************************************************
package circuitCreator;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class Controller implements Initializable {

	// The use of FXML and Initializable adapted from Bro Code JavaFX Event Handling Tutorial:
	// https://youtu.be/N3LZM4WFHBY

	@FXML
	private ToggleButton toggle, delete;
	@FXML
	private ToggleGroup positions, colors;
	@FXML
	private RadioMenuItem yellow, open;
	@FXML
	private GridPane grid;
	@FXML
	private AnchorPane pane;
	@FXML
	private Text componentName, infoText, circuitType;
	@FXML
	private TextField ohmBox, ampBox, voltBox, fileBox;
	@FXML
	private ChoiceBox<String> valueBox;
	@FXML
	private ImageView switchIcon;
	

	private Circuit circuit;
	private String chosenComponent;
	private Component selectedComponent;
	private Storage store = new Storage();
	private PopUp popup = new PopUp("defaults.fxml");
	private boolean firstNode = true;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.circuit = new SeriesCircuit();
		valueBox.setOnAction(this::changeStateOrColor);
		presentCircuit();
	}

	public void selectSource() {
		chosenComponent = "SOURCE";
	}

	public void selectResistor() {
		chosenComponent = "RESISTOR";
	}

	public void selectLight() {
		chosenComponent = "LIGHT";
	}

	public void selectSwitch() {
		chosenComponent = "SWITCH";
	}
	
	public void selectGround() {
		chosenComponent = "GROUND";
	}

	public void placeWire(MouseEvent event) {
		int[] position = getTilePosition(event);

		if (firstNode) {
			store.setStoredComponent(circuit.getComponent(position[0], position[1]));
			store.setStartCoors(getClickPosition(event));
			firstNode = false;

		} else {
			Line line = new Line(store.getStartCoors()[0], store.getStartCoors()[1], getClickPosition(event)[0],
					getClickPosition(event)[1]);
			line.setStrokeWidth(3.0);
			pane.getChildren().add(line);

			Wire wire = new Wire(store.getStoredComponent(), circuit.getComponent(position[0], position[1]));
			wire.setLine(line);
			if (circuit.addWire(wire)) {
				infoText.setText(wire.printComponent() + " successfully created");
			} else {
				pane.getChildren().remove(line);
				infoText.setText("Invalid Wire! Not created");
			}

			toggle.setSelected(false);
			toggle.setOpacity(1);
			firstNode = true;
		}
	}

	public void updateTile(MouseEvent event) {
		if ((event.getButton() == MouseButton.PRIMARY) && delete.isSelected()) {
			removeComponent(event);
		} else if ((event.getButton() == MouseButton.PRIMARY) && toggle.isSelected()) {
			placeWire(event);
		} else if (event.getButton() == MouseButton.PRIMARY) {
			changeComponent(event);
		} else {
			newComponent(event);
		}
		presentCircuit();
	}

	public void changeResistance() {
		System.out.println(ohmBox.getText());
		if (isValidEntry(ohmBox.getText())) {
			double newResistance = Double.parseDouble(ohmBox.getText());
			selectedComponent.setResistance(newResistance);
		}
	}

	public void changeVoltage() {
		System.out.println(voltBox.getText());
		if (isValidEntry(voltBox.getText())) {
			double newVoltage = Double.parseDouble(voltBox.getText());
			selectedComponent.setResistance(newVoltage);
		}
	}

	public void computeCircuit() {
		if (circuit.computeCircuit()) {
			infoText.setText("Circuit successfully computed");
			presentCircuit();
			circuit.computePotentials();
		} else {
			infoText.setText("Circuit Not Complete!");
		}
	}

	public void presentPotentials(ActionEvent event) {

	}

	public void presentCircuit() {
		ImageView view;

		int count = 0;
		for (int j = 0; j < circuit.getSize(); j++) {
			for (int k = 0; k < circuit.getSize(); k++) {
				if (circuit.getComponent(j, k) != null) {
					view = new ImageView(new Image(circuit.getComponent(j, k).getIconLocation()));
					view.setFitHeight(40);
					view.setPreserveRatio(true);

				} else {
					view = new ImageView(new Image("/assets/empty.png"));
					view.setFitHeight(40);
					view.setPreserveRatio(true);
				}

				((Button) grid.getChildren().get(count)).setGraphic(view);
				count++;
			}
		}

	}

	public void swapCircuitType() {
		if (circuitType.getText().equalsIgnoreCase("Series")) {
			circuitType.setText("Parallel");
			resetCircuit();
		} else {
			circuitType.setText("Series");
			resetCircuit();
		}
	}

	public void updateDefaultResistance() {
		try {
			popup.display();
			circuit.setDefaultResistance(popup.getValue());
			infoText.setText("Default resistance set to: " + popup.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSourceVoltage() {
		try {
			popup.display();
			circuit.setSourceVoltage(popup.getValue());
			infoText.setText("Default source voltage set to: " + popup.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateDefaultColor() {
		String color = ((RadioMenuItem) colors.getSelectedToggle()).getText().toUpperCase();
		circuit.setDefaultColor(color);
		infoText.setText("Default light color set to: " + color);
	}

	public void updateDefaultState() {
		String state = ((RadioMenuItem) positions.getSelectedToggle()).getText().toUpperCase();
		circuit.setDefaultState(state);
		infoText.setText("Default switch state set to: " + state);
		if (state.equals("OPEN")) {
			switchIcon.setImage(new Image("/assets/switch open.png"));
		} else {
			switchIcon.setImage(new Image("/assets/switch closed.png"));
		}

	}

	public void changeStateOrColor(ActionEvent event) {
		if (valueBox.getValue() != null) {
			String choice = valueBox.getValue().toUpperCase();
			if (choice.equals("OPEN") || choice.equals("CLOSED")) {
				((Switch) selectedComponent).switchState(choice);
			} else {
				((Light) selectedComponent).changeColor(choice);
			}
			computeCircuit();
		}
	}

	public void toggleToggle(ActionEvent event) {
		if (((ToggleButton) event.getSource()).isSelected()) {

			((Node) event.getSource()).setOpacity(.7);
		} else {
			((Node) event.getSource()).setOpacity(1);
		}
	}

	public void printCircuitToFile() throws Exception {
		String filename = fileBox.getText();

		if (filename.equals("")) {
			infoText.setText("File name cannot be empty");

		} else if (circuit.createCircuitFile(filename)) {
			infoText.setText("Circuit saved successfully");
		} else {
			infoText.setText("Failed to save circuit");
		}
	}

	public void loadCircuitFromFile() {
		String filename = fileBox.getText();

		if (filename.equals("")) {
			infoText.setText("File name cannot be empty");

		} else if (circuit.readCircuitFile(filename)) {
			infoText.setText("Circuit read successfully");
		} else {
			infoText.setText("File not found");
		}
		presentCircuit();
	}

	public void resetCircuit() {
		pane.getChildren().removeAll(circuit.getLines());
		if (circuitType.getText().equalsIgnoreCase("Series")) {
			this.circuit = new SeriesCircuit();
		} else {
			this.circuit = new ParallelCircuit();
		}
		colors.selectToggle(yellow);
		positions.selectToggle(open);
		updateDefaultState();
		presentCircuit();
		resetDisplays();
	}

	public void resetDisplays() {
		infoText.setText("");
		ohmBox.setText("");
		ampBox.setText("");
		voltBox.setText("");
		componentName.setText("");
		valueBox.setValue(null);
	}

	private void newComponent(MouseEvent event) {
		int[] position = getTilePosition(event);

		if (circuit.getComponent(position) != null) {
			removeComponent(event);
		}

		if (chosenComponent != null) {
			ImageView view = new ImageView(new Image(circuit.readComponentType(chosenComponent).getIconLocation()));

			view.setFitHeight(40);
			view.setPreserveRatio(true);
			((Button) event.getSource()).setGraphic(view);

			infoText.setText(chosenComponent + " successfully added.");
			circuit.addComponent(position, chosenComponent);
		} else {

			infoText.setText("No component selected!");
		}

	}

	private void changeComponent(MouseEvent event) {
		int[] position = getTilePosition(event);
		selectedComponent = circuit.getComponent(position[0], position[1]);

		if (selectedComponent != null) {
			componentName.setText(selectedComponent.getType());
			ohmBox.setText("" + selectedComponent.getResistance());
			voltBox.setText("" + selectedComponent.getVoltage());
			ampBox.setText("" + selectedComponent.getCurrent());
			if (selectedComponent instanceof Light) {
				valueBox.getItems().setAll("Blue", "Green", "Purple", "Red", "Yellow");
			} else if (selectedComponent instanceof Switch) {
				valueBox.getItems().setAll("Open", "Closed");
			}
		} else {
			infoText.setText("No component placed");
		}
	}

	private void removeComponent(MouseEvent event) {
		int[] position = getTilePosition(event);
		if (circuit.getComponent(position[0], position[1]) != null) {
			pane.getChildren().removeAll(circuit.removeComponent(position));

			delete.setSelected(false);
			delete.setOpacity(1);
		} else {
			infoText.setText("No component placed");
		}
	}

	private boolean isValidEntry(String string) {
		boolean result = false;
		try {
			double value = Double.parseDouble(string);
			if (value > 0) {
				result = true;
			}

		} catch (Exception e) {
			infoText.setText("That was an invalid input!");
		}

		return result;
	}

	private int[] getTilePosition(MouseEvent event) {
		int[] position = new int[2];

		try {
			position[0] = GridPane.getRowIndex((Node) event.getTarget());
		} catch (NullPointerException e) {
		}

		try {
			position[1] = GridPane.getColumnIndex((Node) event.getTarget());
		} catch (NullPointerException e) {
		}

		return position;
	}

	private double[] getClickPosition(MouseEvent event) {
		double[] position = new double[2];

		position[0] = event.getX() + ((Node) event.getSource()).getLayoutX() + grid.getLayoutX();
		position[1] = event.getY() + ((Node) event.getSource()).getLayoutY() + grid.getLayoutY();

		return position;
	}

}

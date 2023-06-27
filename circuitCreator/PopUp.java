package circuitCreator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PopUp {
	@FXML
	Text infoText;
	
	private Stage stage = new Stage();
	private String filename;
	private double value;
	
	public PopUp(String filename) {
		this.filename = filename;
	}
	
	public void display() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
		loader.setController(this);
//		Parent root = FXMLLoader.load(getClass().getResource(filename));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		
		stage.getIcons().add(new Image("/assets/logo.png"));
		stage.setTitle("Change Window");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.showAndWait();
	}
	
	public void updateValue(ActionEvent event) {
		String string = ((TextField) event.getSource()).getText();
		if (isValidEntry(string)) {
			value = Double.parseDouble(string);
			infoText.setText("Close window to proceed");
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
	
	//Setters and Getters
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
}

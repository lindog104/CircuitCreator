// **************************************************
// Class: Workspace
// Author: Gabriel Lindo
// Created: 03/10/2023
// Modified: 
//
// Purpose: Generate the stage for the main Circuit Creator window where the user builds and edits circuits
//
// Attributes: 
//
// Methods: main(String[]): void - launch the application
//			start(Stage): void - Construct the stage that will be presented to the user
//
// **************************************************

package circuitCreator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Workspace extends Application {
	
	// The following code adapted from Bro Code JavaFx SceneBuilder Tutorial: https://youtu.be/-Obxf6NjnbQ
	
	public static void main(String[] args) {
		
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("workspace.fxml"));
		Scene scene = new Scene(root);
		
		stage.getIcons().add(new Image("/assets/logo.png"));
		stage.setTitle("Circuit Creator");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		
	}
	
	

}

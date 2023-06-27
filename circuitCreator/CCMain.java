// **************************************************
// Class: CCMain
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Run the Circuit Creator Program
//
// Attributes: 
//
// Methods: +main(String[]): void
//			-launchWorkspace(String[]): void - launch the Workspace stage
//
// **************************************************
package circuitCreator;

import javafx.application.Application;

public class CCMain {

	public static void main(String[] args) {
		CCMain me = new CCMain();

		me.launchWorkspace(args);

	}

	private void launchWorkspace(String[] args) {
		Application.launch(Workspace.class, args);
	}

}

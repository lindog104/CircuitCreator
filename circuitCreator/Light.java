// **************************************************
// Class: Light
// Author: Gabriel Lindo
// Created: 03/24/2023
// Modified:
//
// Purpose: Extends the Resistor class to create an electrical load that can give off colored light
//
// Attributes: -color: String - the color of light the Light will give off
//
// Methods: 
//
// **************************************************
package circuitCreator;

public class Light extends Resistor {
	private String color;
	
	public Light(double resistance, String color) {
		super(resistance);
		type = "light";
		iconLocation = "/assets/light.png";
		this.color = color.toUpperCase();
	}
	
	public void changeColor(String newColor) {
		color = newColor;
		
		
	}
	
	@Override
	protected void isActive() {
		if ((current > 0) && (voltage > 0)) {
			switch (color) {
			
			case "BLUE":
				iconLocation = "/assets/blue light.png";
				break;
			case "GREEN":
				iconLocation = "/assets/green light.png";
				break;
			case "PURPLE":
				iconLocation = "/assets/purple light.png";
				break;
			case "RED":
				iconLocation = "/assets/red light.png";
				break;
			case "YELLOW":
				iconLocation = "/assets/light illuminated.png";
				break;
			}
		} else {
			iconLocation = "/assets/light.png";
		}
		
	}
	
	@Override
	public String printComponent() {
		return color + " " + type + ": " + resistance + "R, " + voltage + "V, " + current + "A";
	}
	
	//Setters and Getters
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color.toUpperCase();
	}
}

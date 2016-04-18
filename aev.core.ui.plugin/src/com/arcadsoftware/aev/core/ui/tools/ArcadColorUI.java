package com.arcadsoftware.aev.core.ui.tools;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;


public class ArcadColorUI {

	private RGB color;
	
	public ArcadColorUI(ArcadColor color) {
		this.color = new RGB(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public ArcadColorUI(RGB color) {
		this.color = color;
	}

	public ArcadColorUI(int red, int green, int blue) {
		color = new RGB(red, green, blue);
	}

	public ArcadColor getArcadColor() {
		if (color != null)
			return new ArcadColor(color.red, color.green, color.blue);
		return null;
	}

	public RGB getColor() {
		return color;
	}

	public void setColor(RGB color) {
		this.color = color;
	}
	
	public Color getSWTColor(Device device){
		return new Color(device, color);
	}
}

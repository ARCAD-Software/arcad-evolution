package com.arcadsoftware.aev.core.ui.tools;

public class ArcadColor {
	
	private int red;
	private int green;
	private int blue;
	
	public ArcadColor(String color) {
		if (color.length()==6) {
			red = Integer.parseInt(color.substring(0, 2), 16);
			green = Integer.parseInt(color.substring(2, 4), 16);
			blue = Integer.parseInt(color.substring(4, 6), 16);
		}
	}	
	
	public ArcadColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	@Override
	public String toString() {
		return ((Integer.toHexString(red).equals("0")) ? Integer.toHexString(red) + 0 : Integer.toHexString(red)) + //$NON-NLS-1$
				((Integer.toHexString(green).equals("0")) ? Integer.toHexString(green) + 0 : Integer.toHexString(green)) + //$NON-NLS-1$
				((Integer.toHexString(blue).equals("0"))?Integer.toHexString(blue)+0:Integer.toHexString(blue)); //$NON-NLS-1$
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ArcadColor) {
			ArcadColor color = (ArcadColor)o;
			return (color.getBlue()==blue &&
					color.getGreen()==green &&
					color.getRed()==red);
		}
		return super.equals(o);
	}
	
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}

}

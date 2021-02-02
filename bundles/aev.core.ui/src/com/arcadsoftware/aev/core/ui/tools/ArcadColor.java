package com.arcadsoftware.aev.core.ui.tools;

import java.util.Objects;

public class ArcadColor {

	private int blue;
	private int green;
	private int red;

	public ArcadColor(final int red, final int green, final int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public ArcadColor(final String color) {
		if (color.length() == 6) {
			red = Integer.parseInt(color.substring(0, 2), 16);
			green = Integer.parseInt(color.substring(2, 4), 16);
			blue = Integer.parseInt(color.substring(4, 6), 16);
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof ArcadColor) {
			final ArcadColor color = (ArcadColor) o;
			return color.getBlue() == blue &&
					color.getGreen() == green &&
					color.getRed() == red;
		}
		return super.equals(o);
	}

	@Override
	public int hashCode() {
		return Objects.hash(red, green, blue);
	}
	
	public int getBlue() {
		return blue;
	}

	public int getGreen() {
		return green;
	}

	public int getRed() {
		return red;
	}

	public void setBlue(final int blue) {
		this.blue = blue;
	}

	public void setGreen(final int green) {
		this.green = green;
	}

	public void setRed(final int red) {
		this.red = red;
	}

	@Override
	public String toString() {
		return (Integer.toHexString(red).equals("0") ? Integer.toHexString(red) + 0 : Integer.toHexString(red)) + //$NON-NLS-1$
				(Integer.toHexString(green).equals("0") ? Integer.toHexString(green) + 0 : Integer.toHexString(green)) + //$NON-NLS-1$
				(Integer.toHexString(blue).equals("0") ? Integer.toHexString(blue) + 0 : Integer.toHexString(blue)); //$NON-NLS-1$
	}

}

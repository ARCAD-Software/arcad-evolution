package com.arcadsoftware.aev.core.ui.tools;

public class ArcadFont {

	private String fontName;
	private boolean italic=false;
	private boolean bold=false;
//	private boolean strike;
//	private boolean underline;
	private int fontSize;
	private ArcadColor fontColor;
//	private String script;
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ArcadFont) {
			ArcadFont font = (ArcadFont)o;
			return (font.getFontColor().equals(fontColor) &&
					font.getFontName().equals(fontName) &&
					font.getFontSize()==fontSize &&
					font.isBold()==bold &&
					font.isItalic()==italic);
		}
		return super.equals(o);
	}
	
	public boolean isBold() {
		return bold;
	}
	public void setBold(boolean bold) {
		this.bold = bold;
	}
	public ArcadColor getFontColor() {
		return fontColor;
	}
	public void setFontColor(ArcadColor fontColor) {
		this.fontColor = fontColor;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public boolean isItalic() {
		return italic;
	}
	public void setItalic(boolean italic) {
		this.italic = italic;
	}
//	public String getScript() {
//		return script;
//	}
//	public void setScript(String script) {
//		this.script = script;
//	}
//	public boolean isStrike() {
//		return strike;
//	}
//	public void setStrike(boolean strike) {
//		this.strike = strike;
//	}
//	public boolean isUnderline() {
//		return underline;
//	}
//	public void setUnderline(boolean underline) {
//		this.underline = underline;
//	}
		
}
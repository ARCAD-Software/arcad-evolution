package com.arcadsoftware.ae.core.utils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
		t.execute(args);
	}

	public  void execute(String[] args) {
		FileUtils.duplicate("c:\\test\\install3", "c:\\test\\duplicate", false);
	}
	
}

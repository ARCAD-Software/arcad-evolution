package com.arcadsoftware.aev.core.model;

import java.io.IOException;

public interface IArcadPlugin {
	public String getPluginPath() throws IOException;
	public String getVersion();
}

package com.arcadsoftware.aev.core.model;

import java.io.IOException;

public interface IArcadPlugin {
	String getPluginPath() throws IOException;

	String getVersion();
}

package me.mrletsplay.mrcore.http.server;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;

public interface HttpPage {

	public HttpOpenPage build(HttpPageBuildEvent event);
	
}

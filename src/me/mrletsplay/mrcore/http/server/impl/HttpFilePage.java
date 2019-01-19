package me.mrletsplay.mrcore.http.server.impl;

import java.io.File;
import java.io.FileInputStream;

import me.mrletsplay.mrcore.http.event.HttpPageBuildEvent;
import me.mrletsplay.mrcore.http.server.HttpOpenPage;
import me.mrletsplay.mrcore.http.server.HttpPage;
import me.mrletsplay.mrcore.io.IOUtils;
import me.mrletsplay.mrcore.misc.MiscUtils;

public class HttpFilePage implements HttpPage {

	private File file;
	
	public HttpFilePage(File file) {
		this.file = file;
	}
	
	@Override
	public HttpOpenPage build(HttpPageBuildEvent event) {
		FileInputStream in = MiscUtils.callSafely(() ->new FileInputStream(file)).get();
		return new HttpOpenPage(event.getConnection(), event.getConnectionSocket(), event.getConnection().newPageID(), this, event.getHeader(), IOUtils.readAllBytesSafely(in));
	}
	
}

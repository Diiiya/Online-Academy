package com.academy.onlineAcademy.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

public class InputSource implements StreamSource {

	private byte[] photo;
	
	public InputSource(byte[] inputPhoto) {
		photo = inputPhoto;
	}
	
	public void getCoverPhoto(byte[] inputPhoto) {
		// TODO Auto-generated method stub
		photo = inputPhoto;
//		return this.coverPhoto;
	}

	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		InputStream targetStream = new ByteArrayInputStream(photo);
		return targetStream;
	}

}

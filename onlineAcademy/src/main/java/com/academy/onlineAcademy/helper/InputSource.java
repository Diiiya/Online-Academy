package com.academy.onlineAcademy.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

public class InputSource implements StreamSource {

	private byte[] photo;
	
	/**
	 * Class constructor
	 * @param inputPhoto
	 */
	public InputSource(byte[] inputPhoto) {
		photo = inputPhoto;
	}
	
//	public void getCoverPhoto(byte[] inputPhoto) {
//		photo = inputPhoto;
//	}

	@Override
	public InputStream getStream() {
		InputStream targetStream = new ByteArrayInputStream(photo);
		return targetStream;
	}

}

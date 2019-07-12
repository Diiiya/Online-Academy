package com.academy.onlineAcademy.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.vaadin.server.StreamResource.StreamSource;

public class InputSource implements StreamSource {

	private byte[] coverPhoto;
	
	public InputSource(byte[] coverPhotoI) {
		coverPhoto = coverPhotoI;
	}
	
	public void getCoverPhoto(byte[] coverPhotoI) {
		// TODO Auto-generated method stub
		coverPhoto = coverPhotoI;
//		return this.coverPhoto;
	}

	@Override
	public InputStream getStream() {
		// TODO Auto-generated method stub
		InputStream targetStream = new ByteArrayInputStream(coverPhoto);
		return targetStream;
	}

}

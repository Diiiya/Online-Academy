package com.academy.onlineAcademy.helper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class ImageUploader implements Receiver, SucceededListener {
	public File file;
	private FileOutputStream outputFile;
	
	private final Image image;
	
	public ImageUploader(Image image) {
		this.image = image;
	}

	public File getFile() {
		return file;
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// TODO Auto-generated method stub
		this.image.setSource(new FileResource(file));
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		// TODO Auto-generated method stub
		file = new File("cover.jpg");
		try {
			file.createNewFile(); 
			outputFile = new FileOutputStream(file, false); 
			return outputFile;
		}
		catch (Exception ex) {
			
		}
		
		return null;
	}
}


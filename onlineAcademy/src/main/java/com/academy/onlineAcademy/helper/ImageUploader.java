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
	
	/**
	 * Class constructor
	 * @param image
	 */
	public ImageUploader(Image image) {
		this.image = image;
	}

	/**
	 * Returns the converted to file image
	 * @return File 
	 */
	public File getFile() {
		return file;
	}
	
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		this.image.setSource(new FileResource(file));
	}
	
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
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


package com.zemiak.movies.ui.view;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageUploader implements Receiver, SucceededListener {
    private File file;
    private Embedded image;
    
    public ImageUploader() {
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setImage(Embedded image) {
        this.image = image;
    }
    
    @Override
    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        // Create upload stream
        FileOutputStream fos = null; // Stream to write to
        try {
            // Open the file for writing.
            file = new File("/tmp/uploads/" + filename);
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                             e.getMessage(),
                             Notification.Type.ERROR_MESSAGE)
                .show(Page.getCurrent());
            return null;
        }
        return fos; // Return the output stream to write to
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        // Show the uploaded file in the image viewer
        image.setVisible(true);
        image.setSource(new FileResource(file));
    }

    public File getFile() {
        return file;
    }

    public Embedded getImage() {
        return image;
    }
};

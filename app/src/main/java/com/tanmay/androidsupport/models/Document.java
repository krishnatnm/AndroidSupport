package com.tanmay.androidsupport.models;

import java.io.File;

/**
 * Created by tanma on 5/24/2016.
 */
public class Document {

    String title;
    File fileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getFileName() {
        return fileName;
    }

    public void setFileName(File fileName) {
        this.fileName = fileName;
    }
}

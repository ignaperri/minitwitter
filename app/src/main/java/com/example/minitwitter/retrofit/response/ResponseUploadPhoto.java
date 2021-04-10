package com.example.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUploadPhoto {

    @SerializedName("fieldname")
    @Expose
    private String fieldname;
    @SerializedName("originalname")
    @Expose
    private String originalname;
    @SerializedName("enconding")
    @Expose
    private String enconding;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private String size;

    /**
     * No args constructor for use in serialization
     *
     */
    public ResponseUploadPhoto() {
    }

    /**
     *
     * @param fieldname
     * @param path
     * @param filename
     * @param size
     * @param originalname
     * @param enconding
     * @param destination
     * @param mimetype
     */
    public ResponseUploadPhoto(String fieldname, String originalname, String enconding, String mimetype, String destination, String filename, String path, String size) {
        super();
        this.fieldname = fieldname;
        this.originalname = originalname;
        this.enconding = enconding;
        this.mimetype = mimetype;
        this.destination = destination;
        this.filename = filename;
        this.path = path;
        this.size = size;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }

    public String getEnconding() {
        return enconding;
    }

    public void setEnconding(String enconding) {
        this.enconding = enconding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
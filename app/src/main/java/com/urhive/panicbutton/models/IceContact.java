package com.urhive.panicbutton.models;

/**
 * Created by Chirag Bhatia on 16-04-2017.
 */

public class IceContact {

    private long id;
    private String imageURI;
    private String contactName;
    private String contactNumber;

    // constructors
    public IceContact() {
    }

    public IceContact(long id, String imageURI, String contactName, String contactNumber) {
        this.id = id;
        this.imageURI = imageURI;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    // getter and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "IceContact{" + "id=" + id + ", imageURI='" + imageURI + '\'' + ", contactName='"
                + contactName + '\'' + ", contactNumber='" + contactNumber + '\'' + '}';
    }
}

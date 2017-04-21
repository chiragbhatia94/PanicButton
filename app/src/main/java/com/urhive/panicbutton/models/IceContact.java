package com.urhive.panicbutton.models;

/**
 * Created by Chirag Bhatia on 16-04-2017.
 */

public class IceContact {

    public String imageURI;
    public String contactName;
    public String contactNumber;

    // constructors
    public IceContact() {
    }

    public IceContact(String imageURI, String contactName, String contactNumber) {
        this.imageURI = imageURI;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    // getter and setters
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
        return "IceContact{" + "imageURI='" + imageURI + '\'' + ", contactName='" + contactName +
                '\'' + ", contactNumber='" + contactNumber + '\'' + '}';
    }
}

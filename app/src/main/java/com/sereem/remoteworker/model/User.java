package com.sereem.remoteworker.model;

import android.net.Uri;

public class User {
    private int id;
    private String companyID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String birthday;
    private String emFirstName;
    private String emLastName;
    private String emPhone;
    private String emRelation;
    private String medicalConsiderations;
    private Uri iconUri;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmFirstName() {
        return emFirstName;
    }

    public void setEmFirstName(String emFirstName) {
        this.emFirstName = emFirstName;
    }

    public String getEmLastName() {
        return emLastName;
    }

    public void setEmLastName(String emLastName) {
        this.emLastName = emLastName;
    }

    public String getEmPhone() {
        return emPhone;
    }

    public void setEmPhone(String emPhone) {
        this.emPhone = emPhone;
    }

    public String getEmRelation() {
        return emRelation;
    }

    public void setEmRelation(String emRelation) {
        this.emRelation = emRelation;
    }

    public String getMedicalConsiderations() {
        return medicalConsiderations;
    }

    public void setMedicalConsiderations(String medicalConsiderations) {
        this.medicalConsiderations = medicalConsiderations;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    public User(String companyID, String firstName, String lastName, String email, String password, String phone, String birthday, String emFirstName, String emLastName, String emPhone, String emRelation, String medicalConsiderations, byte[] iconRes) {
        this.companyID = companyID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthday = birthday;
        this.emFirstName = emFirstName;
        this.emLastName = emLastName;
        this.emPhone = emPhone;
        this.emRelation = emRelation;
        this.medicalConsiderations = medicalConsiderations;
    }
}

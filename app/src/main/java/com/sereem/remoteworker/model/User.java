package com.sereem.remoteworker.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

public class User {
    private String UID;
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
    private String iconUri;
//    private GeoPoint geo_point;
//    private @ServerTimestamp Date timestamp;


    private List<Object> worksites;
    private static User instance;

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static User createNewInstance(User user) {
        instance = user;
        return instance;
    }

    public User() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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

//    public GeoPoint getGeo_point() {
//        return geo_point;
//    }
//
//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setGeo_point(GeoPoint geo_point) {
//        this.geo_point = geo_point;
//    }

//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }

    public List<Object> getWorksites() {
        return worksites;
    }

    public void setWorksites(List<Object> list) {
        this.worksites = list;
    }

    public static void setInstance(User instance) {
        User.instance = instance;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public static User createUserForSaving(String id, String companyID, String firstName, String lastName, String email, String phone, String birthday, String emFirstName, String emLastName, String emPhone, String emRelation, String medicalConsiderations, String iconUri, List<Object> worksites) {

        return new User(id, companyID, firstName, lastName, email, phone,
                birthday, emFirstName, emLastName, emPhone,emRelation, medicalConsiderations, iconUri, worksites);
    }

    public static boolean isNull() {
        return instance == null;
    }

    public static User createUserForSavingGPS(String id, String companyID, String firstName, String lastName, String email, String phone, String birthday, String emFirstName, String emLastName, String emPhone, String emRelation, String medicalConsiderations, String iconUri, List<Object> worksites) {

        return new User(id, companyID, firstName, lastName, email, phone,
                birthday, emFirstName, emLastName, emPhone,emRelation, medicalConsiderations, iconUri, worksites);
    }


    private User(String UID, String companyID, String firstName, String lastName, String email, String phone, String birthday, String emFirstName, String emLastName, String emPhone, String emRelation, String medicalConsiderations, String iconUri, List<Object> worksites) {
        this.UID = UID;
        this.companyID = companyID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.emFirstName = emFirstName;
        this.emLastName = emLastName;
        this.emPhone = emPhone;
        this.emRelation = emRelation;
        this.medicalConsiderations = medicalConsiderations;
        this.iconUri = iconUri;
        this.worksites = worksites;
//        this.geo_point = geo_point;
//        this.timestamp = timestamp;
    }

}

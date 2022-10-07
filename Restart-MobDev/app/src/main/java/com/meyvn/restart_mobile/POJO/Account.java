package com.meyvn.restart_mobile.POJO;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class Account {
    private Integer ID;
    private String contact;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nickname;
    private String role;
    private String sex;
    private String lastAssessment;
    private String FCM;
    private List<String> connectedUser;
    private String substanceUsed;
    private String birthDay;
    private Date lastSuspensionDay;
    private boolean activated;
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLastAssessment() {
        return lastAssessment;
    }

    public void setLastAssessment(String lastAssessment) {
        this.lastAssessment = lastAssessment;
    }

    public String getFCM() {
        return FCM;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }

    public String getSubstanceUsed() {
        return substanceUsed;
    }

    public void setSubstanceUsed(String substanceUsed) {
        this.substanceUsed = substanceUsed;
    }

    public List<String> getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(List<String> connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Date getLastSuspensionDay() {
        return lastSuspensionDay;
    }

    public void setLastSuspensionDay(Date lastSuspensionDay) {
        this.lastSuspensionDay = lastSuspensionDay;
    }
}

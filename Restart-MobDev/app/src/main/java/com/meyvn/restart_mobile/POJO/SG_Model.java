package com.meyvn.restart_mobile.POJO;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;

@IgnoreExtraProperties
public class SG_Model {
    private String Title,Description,creatorEmail,sgID;
    private Date dateCreated;
    private List<String> Members;
    private List<String> newmem;
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<String> getMembers() {
        return Members;
    }

    public void setMembers(List<String> members) {
        Members = members;
    }

    public String getSgID() {
        return sgID;
    }

    public void setSgID(String sgID) {
        this.sgID = sgID;
    }

    public List<String> getNewmem() {
        return newmem;
    }

    public void setNewmem(List<String> newmem) {
        this.newmem = newmem;
    }
}

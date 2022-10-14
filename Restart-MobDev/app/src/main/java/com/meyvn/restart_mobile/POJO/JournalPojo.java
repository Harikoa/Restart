package com.meyvn.restart_mobile.POJO;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class JournalPojo {
    private int substanceNumber;
    private int substanceFrequency;
    private int substanceIntensity;
    private int substanceLength;
    private String date;
    private String journalEntry;
    private String mood;
    private boolean important;
    public int getSubstanceNumber() {
        return substanceNumber;
    }

    public void setSubstanceNumber(int substanceNumber) {
        this.substanceNumber = substanceNumber;
    }

    public int getSubstanceFrequency() {
        return substanceFrequency;
    }

    public void setSubstanceFrequency(int substanceFrequency) {
        this.substanceFrequency = substanceFrequency;
    }

    public int getSubstanceIntensity() {
        return substanceIntensity;
    }

    public void setSubstanceIntensity(int substanceIntensity) {
        this.substanceIntensity = substanceIntensity;
    }

    public int getSubstanceLength() {
        return substanceLength;
    }

    public void setSubstanceLength(int substanceLength) {
        this.substanceLength = substanceLength;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(String journalEntry) {
        this.journalEntry = journalEntry;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}

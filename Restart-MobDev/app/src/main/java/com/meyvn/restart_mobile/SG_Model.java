package com.meyvn.restart_mobile;

public class SG_Model {
    String id, title, description, name;

    public SG_Model() {
    }

    public SG_Model(String id, String title, String description, String name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

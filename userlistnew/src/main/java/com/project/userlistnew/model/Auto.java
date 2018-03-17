package com.project.userlistnew.model;

public class Auto {

    Integer id;

    String name;

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Auto(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Auto() {
    }
}

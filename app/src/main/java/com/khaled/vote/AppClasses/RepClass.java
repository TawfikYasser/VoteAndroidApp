package com.khaled.vote.AppClasses;

public class RepClass {
    private  String image,name,type,number;

    public RepClass(String image, String name, String type, String number) {
        this.image = image;
        this.name = name;
        this.type = type;
        this.number = number;
    }

    public RepClass() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

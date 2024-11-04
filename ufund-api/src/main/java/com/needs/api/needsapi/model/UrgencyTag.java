package com.needs.api.needsapi.model;

public enum UrgencyTag {
    HIGH("assets/images/urgent.jpg"),
    LOW("assets/images/blank.jpg");

    private String image;

    private UrgencyTag(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }
}

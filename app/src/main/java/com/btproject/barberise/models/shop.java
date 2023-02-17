package com.btproject.barberise.models;

public class shop {

    private final String name;
    private final String rating;
    private final Integer image;

    public shop(
        final String name,
        final String rating,
        final Integer image
    ){
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public String getName(){
        return this.name;
    }

    public String getRating(){
        return this.rating;
    }

    public Integer getImage(){
        return this.image;
    }

}

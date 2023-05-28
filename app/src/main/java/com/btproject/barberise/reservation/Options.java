package com.btproject.barberise.reservation;

import android.graphics.Path;

import java.io.Serializable;

public abstract class Options implements Serializable {

    protected String name;

    protected Options(String name)
    {
        this.name = name;
    }

    protected Options(){}

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}

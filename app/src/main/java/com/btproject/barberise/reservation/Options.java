package com.btproject.barberise.reservation;

import android.graphics.Path;

public abstract class Options {

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

}

package com.ugosmoothie.ugovendingapp.Data;

import com.orm.SugarRecord;

/**
 * Created by Michelle on 3/15/2016.
 */
public class Liquid extends SugarRecord {

    String Name;
    float Price;

    public Liquid() {

    }

    public Liquid(String name, float price) {
        this.Name = name;
        this.Price = price;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}

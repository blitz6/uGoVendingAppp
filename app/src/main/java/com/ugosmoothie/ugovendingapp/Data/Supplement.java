package com.ugosmoothie.ugovendingapp.Data;

import com.orm.SugarRecord;

/**
 * Created by Michelle on 3/15/2016.
 */
public class Supplement extends SugarRecord {

    String Name;
    float Price;

    public Supplement() {

    }

    public Supplement(String name, float price) {
        this.Name = name;
        this.Price = price;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}

package com.ugosmoothie.ugovendingapp.Data;

import com.orm.SugarRecord;

/**
 * Created by Michelle on 3/15/2016.
 */
public class Smoothie extends SugarRecord {

    String Name;
    Long LiquidId;
    Long SupplementId;
    float Price;

    public Smoothie() {

    }

    public Smoothie(String name, Long liquidId, Long supplementId, float price) {
        this.Name = name;
        this.LiquidId = liquidId;
        this.SupplementId = supplementId;
        this.Price= price;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}

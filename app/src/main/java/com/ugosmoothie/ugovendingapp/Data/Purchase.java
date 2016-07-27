package com.ugosmoothie.ugovendingapp.Data;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.ugosmoothie.ugovendingapp.EventTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Michelle on 3/15/2016.
 */
public class Purchase extends SugarRecord {

    Long SmoothieId;
    Long LiquidId;
    Long SupplementId;
    double Amount;
    Date Timestamp;
    boolean Refunded;

    @Ignore
    boolean completed;

    public Purchase() {
    }

    public Purchase(Long smoothieId, Long liquidId, Long supplementId, boolean refunded, double amount) {
        this.SmoothieId = smoothieId;
        this.LiquidId = liquidId;
        this.SupplementId = supplementId;
        this.Refunded = refunded;
        this.Amount = amount;
        this.Timestamp = new Date();
        this.completed = false;
    }

    //Sending JSON Object to Client App
    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("eventType", EventTypes.SmoothieEvent.Purchase.name());
            obj.put("orderId", this.getId());
            obj.put("smoothieName", CurrentSelection.getInstance().getCurrentSmoothie());
            obj.put("liquidName", CurrentSelection.getInstance().getCurrentLiquid());
            obj.put("supplementName", CurrentSelection.getInstance().getCurrentSupplement());
        } catch (JSONException jsonEx) {
            Log.e("JSON", jsonEx.getMessage());
        }
        return obj;
    }

    public void Completed() {
        this.completed = true;
        //save(); //SA13062016
    }

    //Format to display previous orders on Admin portal
    public String getDisplayString() {
        String retString;

        retString = "Smoothie: " + String.valueOf(this.SmoothieId) +
                " Liquid: " + String.valueOf(this.LiquidId) +
                " Supplement: " + String.valueOf(this.SupplementId);
        return retString;
    }

    //Format to export previous orders to Internal memory -> Downloads
    public String getExportString() {
        String retString;

        retString = "\n" + "OrderId: " + String.valueOf(this.getId()) + ", " +
                "Smoothie: " + String.valueOf(this.SmoothieId) + ", " +
                "Liquid: " + String.valueOf(this.LiquidId) + ", " +
                "Supplement: " + String.valueOf(this.SupplementId) + ", " +
                "Date/Time: " + String.valueOf(this.Timestamp);
        return retString;
    }

}

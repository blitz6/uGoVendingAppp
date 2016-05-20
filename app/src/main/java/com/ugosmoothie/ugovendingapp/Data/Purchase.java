package com.ugosmoothie.ugovendingapp.Data;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.ugosmoothie.ugovendingapp.EventTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Michelle on 3/15/2016.
 */
public class Purchase extends SugarRecord {

    Long SmoothieId;
    Long LiquidId;
    Long SupplementId;
    float Amount;
    boolean Refunded;

    @Ignore
    boolean completed;

    public Purchase() {
    }

    public Purchase(Long smoothieId, Long liquidId, Long supplementId, boolean refunded, float amount) {
        this.SmoothieId = smoothieId;
        this.LiquidId = liquidId;
        this.SupplementId = supplementId;
        this.Refunded = refunded;
        this.Amount = amount;
        this.completed = false;

    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("eventType", EventTypes.SmoothieEvent.Purchase.name());
            obj.put("orderId", this.getId());
            if (CurrentSelection.getInstance().getCurrentSmoothie() != null) {
                obj.put("smoothieName", CurrentSelection.getInstance().getCurrentSmoothie().Name);
            }
            if (CurrentSelection.getInstance().getCurrentLiquid() != null) {
                obj.put("liquidName", CurrentSelection.getInstance().getCurrentLiquid().Name);
            }
            if (CurrentSelection.getInstance().getCurrentSmoothie() != null) {
                obj.put("supplementName", CurrentSelection.getInstance().getCurrentSupplement().Name);
            }
        } catch (JSONException jsonEx) {
            Log.e("JSON", jsonEx.getMessage());
        }
        return obj;
    }


    public void Completed() {
        this.completed = true;
    }
}

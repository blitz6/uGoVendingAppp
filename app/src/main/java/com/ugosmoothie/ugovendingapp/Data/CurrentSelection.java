package com.ugosmoothie.ugovendingapp.Data;

/**
 * Created by Michelle on 3/14/2016.
 */
public class CurrentSelection {

    private static CurrentSelection ourInstance = new CurrentSelection();

    public static CurrentSelection getInstance() {
        return ourInstance;
    }

    private Smoothie currentSmoothie;
    private Liquid currentLiquid;
    private Supplement currentSupplement;
    private float total;
    private CurrentSelection() {
        currentSmoothie = null;
        currentLiquid = null;
        currentSupplement = null;
        total = 0;
    }

    public void setCurrentSmoothie(Smoothie smoothie) {
        this.currentSmoothie = smoothie;
        calculateTotal();
    }

    public void setCurrentLiquid(Liquid liquid) {
        this.currentLiquid = liquid;
        calculateTotal();
    }

    public void setCurrentSupplement(Supplement supplement) {
        this.currentSupplement = supplement;
        calculateTotal();
    }

    public Smoothie getCurrentSmoothie() {
        return this.currentSmoothie;
    }

    public Liquid getCurrentLiquid() {
        return this.currentLiquid;
    }

    public Supplement getCurrentSupplement() {
        return this.currentSupplement;
    }

    public float getTotal() {
        return this.total;
    }

    private void calculateTotal() {
        if (this.currentSmoothie != null) {
            total = this.currentSmoothie.Price;
        }

        if (this.currentLiquid != null) {
            total += this.currentLiquid.Price;
        }

        if (this.currentSupplement != null) {
            total += this.currentSupplement.Price;
        }
    }
}

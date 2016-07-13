package com.ugosmoothie.ugovendingapp.Data;

/**
 * Created by Michelle on 3/14/2016
 */
public class CurrentSelection {

    private static CurrentSelection ourInstance = new CurrentSelection();

    public static CurrentSelection getInstance() {
        return ourInstance;
    }

    private int currentSmoothieId;
    private int currentLiquidId;
    private int currentSupplementId;
    private double total;
    private CurrentSelection() {
        currentSmoothieId = -1;
        currentLiquidId = -1;
        currentSupplementId = -1;
        total = 0.00;
    }

    public void setCurrentSmoothie(int smoothie) {
        this.currentSmoothieId = smoothie;
        calculateTotal();
    }

    public void setCurrentLiquid(int liquid) {
        this.currentLiquidId = liquid;
        calculateTotal();
    }

    public void setCurrentSupplement(int supplement) {
        this.currentSupplementId = supplement;
        calculateTotal();
    }

    public int getCurrentSmoothie() {

        return this.currentSmoothieId;
    }

    public int getCurrentLiquid() {

        return this.currentLiquidId;
    }

    public int getCurrentSupplement() {

        return this.currentSupplementId;
    }

    public double getTotal() {

        return this.total;
    }

     private void calculateTotal() {
         if (this.currentLiquidId == 0 || this.currentSupplementId == 0){
             total += 0;
         }
         if (this.currentSmoothieId >= 0 && this.currentSmoothieId <= 2) {
            total = 5;
         }

         if (this.currentLiquidId >= 1 && this.currentLiquidId <= 2) {
            total++;
         }

         if (this.currentSupplementId >= 1 && this.currentSupplementId <= 4) {
            total++;
         }
    }

}

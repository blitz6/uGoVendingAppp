package com.ugosmoothie.ugovendingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.payments.core.AndroidTerminal;
import com.payments.core.CoreAPIListener;
import com.payments.core.CoreDeviceError;
import com.payments.core.CoreMessage;
import com.payments.core.CoreRefundResponse;
import com.payments.core.CoreSaleKeyed;
import com.payments.core.CoreSaleResponse;
import com.payments.core.CoreSettings;
import com.payments.core.CoreSignature;
import com.payments.core.CoreTransactionSummary;
import com.payments.core.CoreTransactions;
import com.payments.core.DeviceEnum;

import com.payments.core.CoreError;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SachinApiah on 7/23/2016.
 */
public class PaymentProcessing implements CoreAPIListener {

    private static PaymentProcessing ourInstance = new PaymentProcessing();
    public static PaymentProcessing getInstance() {
        return ourInstance;
    }

    Context ctx;
    AndroidTerminal terminal;

    void Initialize(Context ctx) {
        this.ctx = ctx;
        terminal = new AndroidTerminal(this);
        terminal.initWithConfiguration(ctx, "33002", "SandboxSecret002");
        terminal.initDevice(DeviceEnum.NODEVICE);
    }

    @Override
    public void onMessage(CoreMessage message) {

    }

    @Override
    public void onSaleResponse(CoreSaleResponse response) {
        Log.e("uGo", response.toString());
        // payment success
        Intent intent = new Intent("paymentCompleted");
        ctx.sendBroadcast(intent);
    }

    @Override
    public void onRefundResponse(CoreRefundResponse response) {
        Log.e("uGo", response.toString());
    }

    @Override
    public void onTransactionListResponse(CoreTransactions response) {
        ArrayList<CoreTransactionSummary> list = response.getTransactionSummary();
        for(CoreTransactionSummary tr : list){
            //...
        }
    }

    @Override
    public void onLoginUrlRetrieved(String url) {
        // start browser to log in
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);
    }

    @Override
    public void onSignatureRequired(final CoreSignature signature) {
//        //create canvas to draw a signature and pass the signature object
//        signatureCanvas.setSignature(signature);
//        signatureCanvas.submitSignature();
    }

    @Override
    public void onError(CoreError error, String message) {
        Log.e("uGo", error.toString());
    }

    @Override
    public void onDeviceError(CoreDeviceError error, String message){
        //...
    }

    @Override
    public void onSettingsRetrieved(CoreSettings settings) {
        ArrayList tax = settings.getTaxList();
        ArrayList tip = settings.getTipList();
       //...
    }

    @Override
    public void onDeviceConnected(DeviceEnum type, HashMap deviceInfo){
//        String address = deviceInfo.get("bluetoothID");
//        String supportNFC = deviceInfo.get("isSupportedNfc");
//        //...
    }

    @Override
    public void onDeviceDisconnected(DeviceEnum type){
        //...
    }

    @Override
    public void onSelectApplication(ArrayList applications){
        terminal.submitApplication(0);
    }

    public void ProcessPaymentRequest(Double price) {
        CoreSaleKeyed sale = new CoreSaleKeyed(BigDecimal.valueOf(price));
        sale.setCardHolderName("Test User");
        sale.setCardNumber("4005510000000013");
        sale.setCardCvv("123");
        sale.setCardType("VISA");
        sale.setExpiryDate("1215");

        terminal.processSale(sale);
    }
}

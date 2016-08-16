package com.ugosmoothie.ugovendingapp.MachineCommunication;

/**
 * Created by Michelle on 8/13/2016.
 */
public interface IArduinoHandler {
    void onUsbStopped();

    void onErrorLooperRunningAlready();

    void onDeviceNotFound();
}

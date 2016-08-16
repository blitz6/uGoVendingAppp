package com.ugosmoothie.ugovendingapp.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Michelle on 7/6/2016.
 */
public class AdministratorActivity extends AppCompatActivity {
    private final String TAG = AdministratorActivity.class.getSimpleName();
    private final long StartOfMessage = 0x557E;
    private final long EndOfMessage = 0x557F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_activity);

        final Button quit = (Button) findViewById((R.id.quit_button));
        final Button close = (Button) findViewById((R.id.close_button));
        final Button export = (Button) findViewById((R.id.export_button));
        final Button filter = (Button) findViewById((R.id.filter_list_tag));
        final EditText start_Date = (EditText) findViewById((R.id.start_tag));
        final EditText end_Date = (EditText) findViewById((R.id.end_tag));

        //Quit the Admin portal as well as App
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDeviceList();
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result", "quit");
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
            }
        });

        //Quit the Admin Portal
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 0);
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        //Exporting Data into Internal Memory
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_Date.getText() != null && end_Date.getText() != null){
                    String start = start_Date.getText().toString();
                    String end = end_Date.getText().toString();
                    export_list(start, end);
                }
            }
        });

        //Display Previous orders based on set date filter
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start_Date.getText() != null && end_Date.getText() != null){
                    String start = start_Date.getText().toString();
                    String end = end_Date.getText().toString();
                    filter_list(start, end);
                }
            }
        });


        // Arduino work
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        refreshDeviceList();
    }

    public void export_list(String Start_date, String End_date){
        try {
            FileOutputStream fos = new FileOutputStream(getStorageDir());

            Date startDate = new Date(Start_date);
            Date endDate = new Date(End_date);

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                startDate = formatter.parse(Start_date);
                endDate = formatter.parse(End_date);
            } catch (ParseException ex) {
                Log.e("Ugo", ex.getMessage());
            }

            List<Purchase> purchases = Purchase.find(Purchase.class, "Timestamp >= ? and Timestamp <= ?", startDate.getTime() + " ", endDate.getTime() + " ");
            fos.write("uGoPurchase Log \n".getBytes());
            for (Purchase value : purchases) {
                fos.write(value.getExportString().getBytes());
            }

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filter_list(String Start_date, String End_date){

        Date startDate = new Date(Start_date);
        Date endDate = new Date(End_date);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            startDate = formatter.parse(Start_date);
            endDate = formatter.parse(End_date);
        } catch (ParseException ex) {
            Log.e("Ugo", ex.getMessage());
        }

        List<Purchase> purchases = Purchase.find(Purchase.class, "Timestamp >= ? and Timestamp <= ?", startDate.getTime() + " ", endDate.getTime() + " ");

        String[] array = new String[purchases.size()];
        int index = 0;
        for (
                Purchase value
                : purchases)
        {
            array[index] = value.getDisplayString();
            index++;
        }
        final ListView listview = (ListView) findViewById(R.id.purchase_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, array);
        listview.setAdapter(adapter);
    }

    public File getStorageDir() {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Purchases.txt");
        try {
            file.createNewFile();
        } catch (IOException ioe) {
            Log.e("uGoSmoothie", "Directory not created");
        }

        return file;
    }


    // ARDUINO COMMUNICATION

    private static UsbSerialPort sPort = null;
    private UsbManager mUsbManager;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    AdministratorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AdministratorActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private byte[] inBuffer = new byte[255];
    private int read = 0;

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        Log.i(TAG, message);

        // read it to in buffer
        for (int i = 0; i < data.length; i++) {
            Log.i(TAG, String.format("READ: %02X", data[i]));
            inBuffer[read++]= data[i];
        }

        checkForCompleteMessage();
        //mDumpTextView.append(message);
        //mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }

    private final int MinimumPacketSize = 12;

    private void checkForCompleteMessage() {
        if (read >= MinimumPacketSize) { // 12 is the minimum message size
            long sof = (inBuffer[1] << 8) + inBuffer[0];

            if (sof == StartOfMessage) {
                // valid message, good start
                Log.v(TAG, "PASSED START OF MESSAGE");
                int messageLength = (inBuffer[3] << 8) + inBuffer[2];
                // TODO: check CRC16 to make sure pack valid
                if (read == messageLength) {
                    // print entire in buffer
                    for (int i = 0; i < read; i++) {
                        Log.i(TAG, String.format("InBuffer: %02X", inBuffer[i]));
                    }

                    int messageIntId = ((inBuffer[7] << 8) + inBuffer[6]);
                    Log.i(TAG, "Message id is equal to:" + messageIntId);
                    ArduinoMessageId messageId = ArduinoMessageId.find(messageIntId);
                    switch (messageId) {
                        case GetFirmwareVersionReply:
                            Log.i(TAG, "Got the firmware version reply");
                            read = 0;
                            break;
                        case AutoCycleReply:
                            Log.i(TAG, "Get AutoCycle reply - liquid: " + inBuffer[8] + " protien: " + inBuffer[9]);
                        default:
                            read = 0;
                            break;
                    }
                }
            } else {
                Log.i(TAG, "Not an SOF, resetting read to 0");
                read = 0;
            }
        } else if (read > 0) {
            if (inBuffer[0] != 0x7E) {
                Log.i(TAG, "First byte not 0x7E, resetting read to 0");
                read = 0;
            }
        }
    }

    private void refreshDeviceList() {

        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.d(TAG, "Refreshing device list ...");
                SystemClock.sleep(1000);

                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.d(TAG, String.format("+ %s: %s port%s",
                            driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);
                }

                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                if (result.size() > 0) {
                    // we have a device connected. let's control it.
                    fireUpTheArduino(result.get(0));

                    stopIoManager();
                    startIoManager();
                }
                Log.d(TAG, "Done refreshing, " + result.size() + " entries found.");
            }

        }.execute((Void) null);
    }

    private void fireUpTheArduino(UsbSerialPort port) {
        if (port != null) {
            sPort = port;
            final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

            UsbDeviceConnection connection = usbManager.openDevice(sPort.getDriver().getDevice());
            if (connection == null) {
                //mTitleTextView.setText("Opening device failed");
                return;
            }

            try {
                sPort.open(connection);
                sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                Log.i(TAG, "We got a connection with the arduino!!!!");
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                //mTitleTextView.setText("Error opening device: " + e.getMessage());
                try {
                    sPort.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sPort = null;
            }
        }
    }

    private enum ArduinoMessageId {
        AutoCycle(0x0001),
        MachineError(0x0002),
        GetMachineState(0x0003),
        GetFirmwareVersion(0x0004),
        GetFirmwareVersionReply(0x0A04),
        GetSensorState(0x0005),
        GetActuatorState(0x0006),
        AutoCycleReply(0x0A01);

        private int value;

        private ArduinoMessageId(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static ArduinoMessageId find (int value) {
            ArduinoMessageId stuff[] = ArduinoMessageId.values();
            for (int i =  0; i < stuff.length; i++) {
                if (stuff[i].getValue() == value) {
                    return stuff[i];
                }
            }
            return null;
        }
    }

    private void WriteMessageToArduino(short message_id, byte[] message, short length) {
        byte toSend[];
        toSend = new byte[MinimumPacketSize + message.length];

        int i = 0;

        toSend[i++] = (byte)(StartOfMessage & 0xFF);
        toSend[i++] = (byte)((StartOfMessage >> 0x8) & 0xFF);
        toSend[i++] = (byte)(toSend.length & 0xFF);
        toSend[i++] = (byte)((toSend.length >> 0x8) & 0xFF);
        toSend[i++] = 0; // source address
        toSend[i++] = 0; // destination address
        toSend[i++] = (byte)(message_id & 0xFF);
        toSend[i++] = (byte)((message_id >> 0x8) & 0xFF);

        for (int j = 0; j < message.length; j++) {
            toSend[i++] = message[j];
        }

        toSend[i++] = 0; // CRC16
        toSend[i++] = 0; // CRC16

        toSend[i++] = (byte)(EndOfMessage & 0xFF);
        toSend[i++] = (byte)((EndOfMessage >> 0x8) & 0xFF);

        // send the message to Arduino
        if (sPort != null) {
            try {
                sPort.write(toSend, 500);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        } else {
            Log.e(TAG, "THE PORT IS NOT OPEN!!!!");
        }
    }

}

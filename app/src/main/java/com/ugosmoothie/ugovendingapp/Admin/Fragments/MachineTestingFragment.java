package com.ugosmoothie.ugovendingapp.Admin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.ugosmoothie.ugovendingapp.Admin.AdministratorActivity;
import com.ugosmoothie.ugovendingapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Michelle on 8/15/2016.
 */
public class MachineTestingFragment extends Fragment {
    private final String TAG = MachineTestingFragment.class.getSimpleName();
    private final long StartOfMessage = 0x557E;
    private final long EndOfMessage = 0x557F;


    private TextView loggerTextView;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.administrator_machine_testing, container, false);

        // Arduino work
        mUsbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);
        refreshDeviceList();

        final Button connect = (Button) rootView.findViewById((R.id.connect_button));
        final Button blend = (Button) rootView.findViewById((R.id.blend_button));
        final Button clean = (Button) rootView.findViewById((R.id.clean_button));
        final Button initialize = (Button) rootView.findViewById((R.id.initialize_button));
        final Button stop = (Button) rootView.findViewById((R.id.stop_button));
        //final Button togglePump = (Button) rootView.findViewById((R.id.toggle_pump_button));
        final Button moveUp = (Button) rootView.findViewById((R.id.move_up_button));
        final Button moveDown = (Button) rootView.findViewById((R.id.move_down_button));
        final Button jogTop = (Button) rootView.findViewById((R.id.jog_top_button));
        final Button jogBottom = (Button) rootView.findViewById((R.id.jog_bottom_button));
        final Button poke = (Button) rootView.findViewById((R.id.poke_button));
        loggerTextView = (TextView) rootView.findViewById(R.id.logger);
        scrollView = (ScrollView)rootView.findViewById(R.id.ScrollView01);

        writeMessage("Started logger");

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDeviceList();
                writeMessage("Refreshing device list");

            }
        });

        blend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Sending auto cycle");
                SendAutoCycleMessage();
            }
        });

        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCleanCycleMessage();
                writeMessage("Sending clean cycle");
            }
        });

        initialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Sending initialize");
                SendInitializeMessage();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Sending STOP");
                SendStopMessage();
            }
        });

        moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Moving Up");
                MoveUpMessage();
            }
        });

        moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Moving Down");
                MoveDownMessage();
            }
        });


        jogTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Jogging Top Liquid Dispenser");
                MoveDownMessage();
            }
        });

        jogBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeMessage("Jogging Bottom Liquid Dispenser");
                MoveDownMessage();
            }
        });


        poke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return rootView;
    }

    private void writeMessage(String message) {
        Log.v(TAG, message);
        loggerTextView.append(message);
        loggerTextView.append("\n");
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void SendAutoCycleMessage() {
        byte autoCycle[] = new byte[3];
        autoCycle[0] = 0;//(byte)CurrentSelection.getInstance().getCurrentSmoothie();
        autoCycle[1] = 0;//(byte)CurrentSelection.getInstance().getCurrentLiquid();
        autoCycle[2] = 0;//(byte)CurrentSelection.getInstance().getCurrentSupplement();
        WriteMessageToArduino((short) ArduinoMessageId.AutoCycle.getValue(), autoCycle, (short) autoCycle.length);
    }

    private void ToggleOutputMessage() {
        byte toggleOutput[] = new byte[1];
        toggleOutput[0] = 13; // pin to toggle
        WriteMessageToArduino((short) ArduinoMessageId.ToggleActuatorState.getValue(), toggleOutput, (short) toggleOutput.length);

    }

    private void SendCleanCycleMessage() {
        byte cleanCycle[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.Sanitize.getValue(), cleanCycle, (short)cleanCycle.length);
    }

    private void SendInitializeMessage() {
        byte initialize[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.Initialize.getValue(), initialize, (short)initialize.length);
    }

    private void SendStopMessage() {
        byte stop[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.Stop.getValue(), stop, (short)stop.length);

    }

    private void MoveUpMessage() {
        byte moveUp[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.MoveUp.getValue(), moveUp, (short) moveUp.length);

    }

    private void MoveDownMessage() {
        byte moveDown[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.MoveDown.getValue(), moveDown, (short)moveDown.length);

    }

    private void JogTopMessage() {
        byte jogTop[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.JogTop.getValue(), jogTop, (short)jogTop.length);

    }

    private void JogBottomMessage() {
        byte jogBottom[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.JogBottom.getValue(), jogBottom, (short)jogBottom.length);

    }


    private void PokeMessage() {
        byte poke[] = new byte[1];
        WriteMessageToArduino((short)ArduinoMessageId.Poke.getValue(), poke, (short)poke.length);

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
                    writeMessage("Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MachineTestingFragment.this.updateReceivedData(data);
                        }
                    });
                }
            };

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            writeMessage("Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            writeMessage("Starting io manager ..");
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

try {
    checkForCompleteMessage();
}
catch(Exception e) {
    writeMessage(e.getMessage());
}
        //mDumpTextView.append(message);
        //mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
    }

    private final int MinimumPacketSize = 12;

    private void checkForCompleteMessage() {
        for (int i = 0; i < read;i++) {
            // check for start
            if (inBuffer[i] == 0x7E) {
                // possible message start
                if (read - i > 1) {
                    if (inBuffer[i + 1] == 0x55)
                    System.arraycopy(inBuffer, i, inBuffer, 0, read - i);
                    read = read - i;
                }
            }
        }
        if (read >= MinimumPacketSize) { // 12 is the minimum message size
            long sof = (inBuffer[1] << 8) + inBuffer[0];

            if (sof == StartOfMessage) {
                // valid message, good start
                int messageLength = ((inBuffer[3] << 8) & 0xFF) + (inBuffer[2] & 0xFF);
                // TODO: check CRC16 to make sure pack valid
                if (read > messageLength) {
                    // print entire in buffer
                    for (int i = 0; i < read; i++) {
                        Log.i(TAG, String.format("InBuffer: %02X", inBuffer[i]));
                    }

                    int messageIntId = ((inBuffer[7] << 8) + inBuffer[6]);
                    ArduinoMessageId messageId = ArduinoMessageId.find(messageIntId);
                    switch (messageId) {
                        case GetFirmwareVersionReply:
                            writeMessage("Got the firmware version reply");
                            read = 0;
                            break;

                        case AutoCycleReply:
                            writeMessage("Get AutoCycle reply - liquid: " + inBuffer[8] + " protien: " + inBuffer[9]);
                            break;
                        case Heartbeat:
                            writeMessage("Heartbeat");
                            break;
                        case log:
                            byte[] messageToPrint = new byte[messageLength - 12];
                            System.arraycopy(inBuffer, 8, messageToPrint, 0, messageLength - 13);
                            String print = new String(messageToPrint);
                            writeMessage(print);
                            break;
                        default:
                            break;
                    }

                    System.arraycopy(inBuffer, messageLength, inBuffer, 0, read - messageLength);
                    read = read - messageLength;
                }
            } else {
                writeMessage("Not an SOF, resetting read to 0");
            }
        }
    }

    private void refreshDeviceList() {

        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                writeMessage("Refreshing device list ...");
                SystemClock.sleep(1000);

                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.i(TAG, String.format("+ %s: %s port%s",
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
                writeMessage("Done refreshing, " + result.size() + " entries found.");
            }
        }.execute((Void) null);
    }

    private void fireUpTheArduino(UsbSerialPort port) {
        if (port != null) {
            sPort = port;
            final UsbManager usbManager = (UsbManager) getContext().getSystemService(Context.USB_SERVICE);

            UsbDeviceConnection connection = usbManager.openDevice(sPort.getDriver().getDevice());
            if (connection == null) {
                //mTitleTextView.setText("Opening device failed");
                return;
            }

            try {
                sPort.open(connection);
                sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                writeMessage("We got a connection with the arduino!!!!");
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
        Heartbeat(0x0000),
        AutoCycle(0x0001),
        MoveUp (0x00002),
        MoveDown (0x0003),
        JogTop (0x00004),
        JogBottom (0x00005),
        Poke (0x000C),
        AutoCycleReply(0x0A01),
        MachineError(0x0002),
        GetMachineState(0x0003),
        GetFirmwareVersion(0x0004),
        GetFirmwareVersionReply(0x0A04),
        GetSensorState(0x0005),
        GetActuatorState(0x0006),
        Sanitize(0x0007),
        log(0x0008),
        Initialize(0x009),
        Stop(0x00A),
        ToggleActuatorState(0x000B);

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
            writeMessage("THE PORT IS NOT OPEN!!!!");
        }
    }
}

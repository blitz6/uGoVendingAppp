package com.ugosmoothie.ugovendingapp.Pivotal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Michelle on 8/28/2016.
 */
public class FlexPointQSR {

    private final String TAG = FlexPointQSR.class.getName();

    private final byte STX = 0x02;
    private final byte ETX = 0x03;
    private final byte ACK = 0x06;
    private final byte NAK = 0x15;
    private final byte Delimiter = 0x5E;

    private final String IPAddress = "192.168.43.241";
    private final int Port = 9999;

    private boolean running = false;
    private boolean messageSent = false;
    DataOutputStream dos = null;
    BufferedReader reader = null;

    final byte[] inBuf = new byte[2048];
    byte[] lastMessage = null;

    private OnMessageReceived messageReceivedListener = null;

    /**
     * Constructor
     * @param listener callback for message received
     */
    public FlexPointQSR(OnMessageReceived listener) {
        messageReceivedListener = listener;
    }

    public void sendTransaction() {
        String Pkt = FlexPointMessageId.TransactionId.getValue() + "^1^" + FlexPointMessageId.TransactionAmount.getValue() + "^500^";

        int len = Pkt.length() + 4;   // ETX and LRC are mistakenly considered 2 bytes each. So we add 4, not 2
        String PktLen = "11111^" + String.format("%05d", len) + "^";

        StringBuilder sb = new StringBuilder();
        sb.append("S");
        sb.append(PktLen);
        sb.append(Pkt);
        sb.append("E");
        sb.append("L");

        byte[] bytearr = String.valueOf(sb).getBytes();

        bytearr[0] = STX;
        bytearr[bytearr.length - 2] = ETX;
        bytearr[bytearr.length - 1] = LRC(bytearr, bytearr.length - 1);     // LRC

        lastMessage = bytearr;

        try {
            dos.write(lastMessage);
            messageSent = true;
        } catch (IOException e) {

        }
    }

    public void run() {
        running = true;
        byte[] inBuf = new byte[2048];
        int readLength = 0;

        try {
            Socket socket = new Socket(IPAddress, Port);

            try {
                dos = new DataOutputStream(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                sendTransaction();
                while (running) {
                    // make sure we are okay....
                    assert readLength < inBuf.length;
                    if ((inBuf[readLength] = (byte) reader.read()) != -1) {
                        if (inBuf[readLength] == ETX) {
                            // end of message found, get the LRC
                            inBuf[++readLength] = (byte) reader.read();

                            // check the LRC
                            //if (LRC(inBuf, readLength - 1) == inBuf[readLength]) {
                                dos.write(ACK);

                                if (messageReceivedListener != null) {
                                    // find out what happened with the transaction
                                    messageReceivedListener.messageReceived(new String(inBuf, 1, readLength - 3));
                                }
                            //} else {
                            //    dos.write(NAK);
                            //}

                            readLength = 0;
                            inBuf[0] = 0x00;
                            running = false;
                        }

                        if (inBuf[0] == ACK) {
                            // passed acknowledgement
                        } else if (inBuf[0] == NAK) {
                            Log.e(TAG, "NAK received, not good");
                            //resend message
                            dos.write(lastMessage);
                            readLength = 0;
                            inBuf[0] = 0x00;
                        } else if (inBuf[0] == STX) {
                            // starting message
                            readLength++;
                        }
                    } else {
                        // end of stream reached, if message not complete something went wrong
                        readLength = 0;
                    }
                }
            } catch (IOException e) {

            } finally {
                socket.close();
            }
        } catch (IOException e) {

        }
    }


    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asyncTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }

    public enum FlexPointMessageId {
        MessageLength("11111"),
        ExternalAppName("10079"),
        TransactionId("10002"),
        TransactionAmount("10007"),
        OperatorId("10010"),
        MerRefNumber("10012"),
        PosTransactionDate("10027"),
        PosTrasactionTime("10028"),
        ApprovalCode("10030"),
        Invoice("90001"),
        PosResultCode("11011"),
        UserCanceled("11010");

        private String value;

        private FlexPointMessageId(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public static FlexPointMessageId find (String value) {
            FlexPointMessageId stuff[] = FlexPointMessageId.values();
            for (int i =  0; i < stuff.length; i++) {
                if (stuff[i].getValue() == value) {
                    return stuff[i];
                }
            }
            return null;
        }
    }

    private static byte LRC(byte[] bytes, int len) {
        byte LRC = 0;

        for (int i = 1; i < len; i++) {
            LRC ^= bytes[i];
        }
        return LRC;
    }
}

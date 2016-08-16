package com.ugosmoothie.ugovendingapp.Admin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ugosmoothie.ugovendingapp.R;

/**
 * Created by Michelle on 8/15/2016.
 */
public class MachineTestingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.administrator_machine_testing, container, false);

        return rootView;
    }

    private void SendAutoCycleMessage() {
        byte autoCycle[] = new byte[3];
        autoCycle[0] = 0;//(byte)CurrentSelection.getInstance().getCurrentSmoothie();
        autoCycle[1] = 0;//(byte)CurrentSelection.getInstance().getCurrentLiquid();
        autoCycle[2] = 0;//(byte)CurrentSelection.getInstance().getCurrentSupplement();
        //WriteMessageToArduino((short)ArduinoMessageId.AutoCycle.getValue(), autoCycle, (short)autoCycle.length);
    }
}

package com.ugosmoothie.ugovendingapp.Admin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Michelle on 8/15/2016.
 */
public class LogExportFragment extends Fragment {

    private ListView purchaseListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.administrator_log_export_view, container, false);

        final ListView purchaseListView = (ListView) rootView.findViewById(R.id.purchase_listview);

        final Button quit = (Button) rootView.findViewById((R.id.quit_button));
        final Button close = (Button) rootView.findViewById((R.id.close_button));
        final Button export = (Button) rootView.findViewById((R.id.export_button));
        final Button filter = (Button) rootView.findViewById((R.id.filter_list_tag));
        final EditText start_Date = (EditText) rootView.findViewById((R.id.start_tag));
        final EditText end_Date = (EditText) rootView.findViewById((R.id.end_tag));

        //Quit the Admin portal as well as App
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "quit");
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
        });

        //Quit the Admin Portal
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 0);
                getActivity().setResult(Activity.RESULT_CANCELED, returnIntent);
                getActivity().finish();
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

        return rootView;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, array);
        purchaseListView.setAdapter(adapter);
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
}

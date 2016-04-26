package com.ugosmoothie.ugovendingapp.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugosmoothie.ugovendingapp.PurchaseSmoothie;
import com.ugosmoothie.ugovendingapp.R;
import com.ugosmoothie.ugovendingapp.Survey.XmlGuiEditBox;
import com.ugosmoothie.ugovendingapp.Survey.XmlGuiForm;
import com.ugosmoothie.ugovendingapp.Survey.XmlGuiFormField;
import com.ugosmoothie.ugovendingapp.Survey.XmlGuiPickOne;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Michelle on 3/22/2016.
 */
public class SurveyFragment extends Fragment {

    TextView orderStatusTextView;

    Timer orderCompleteRestartTimer;
    OrderCompleteTimerTask orderCompleteTimerTask;

    String tag = SurveyFragment.class.getName();
    XmlGuiForm theForm;
    ProgressDialog progressDialog;
    Handler progressHandler;
    boolean surveyInProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.survey_view, container, false);
        orderStatusTextView = (TextView) rootView.findViewById(R.id.orderStatusTextView);
        if(GetFormData()) {
            DisplayForm();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("complete"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void startTimer(){
        this.orderCompleteRestartTimer = new Timer();
        this.orderCompleteTimerTask = new OrderCompleteTimerTask();

        this.orderCompleteRestartTimer.schedule(this.orderCompleteTimerTask, 5000);
    }

    private void stopTimer(View v) {
        if (this.orderCompleteRestartTimer != null) {
            this.orderCompleteRestartTimer.cancel();
            this.orderCompleteRestartTimer = null;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            orderStatusTextView.setText("ORDER COMPLETE!");
            startTimer();
        }
    };

    class OrderCompleteTimerTask extends TimerTask {
        @Override
        public void run() {
           getActivity().runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   ((PurchaseSmoothie) getActivity()).GetUGoViewPager().setCurrentItem(0);
               }
           });
        }
    }

    private boolean GetFormData() {
        try {
            Log.i(tag, "ProcessForm");
            InputStream is = getActivity().getAssets().open("survey.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document dom = db.parse(is);
            Element root = dom.getDocumentElement();
            NodeList forms = root.getElementsByTagName("form");

            if (forms.getLength() < 1) {
                // nothing here??
                Log.e(tag, "No form, let's bail");
                return false;
            }
            Node form = forms.item(0);
            theForm = new XmlGuiForm();

            // process form level
            NamedNodeMap map = form.getAttributes();
            theForm.setFormNumber(map.getNamedItem("id").getNodeValue());
            theForm.setFormName(map.getNamedItem("name").getNodeValue());
            if (map.getNamedItem("submitTo") != null)
                theForm.setSubmitTo(map.getNamedItem("submitTo").getNodeValue());
            else
                theForm.setSubmitTo("loopback");

            // now process the fields
            NodeList fields = root.getElementsByTagName("field");
            for (int i = 0; i < fields.getLength(); i++) {
                Node fieldNode = fields.item(i);
                NamedNodeMap attr = fieldNode.getAttributes();
                XmlGuiFormField tempField = new XmlGuiFormField();
                tempField.setName(attr.getNamedItem("name").getNodeValue());
                tempField.setLabel(attr.getNamedItem("label").getNodeValue());
                tempField.setType(attr.getNamedItem("type").getNodeValue());
                if (attr.getNamedItem("required").getNodeValue().equals("Y"))
                    tempField.setRequired(true);
                else
                    tempField.setRequired(false);
                tempField.setOptions(attr.getNamedItem("options").getNodeValue());
                theForm.getFields().add(tempField);
            }

            Log.i(tag, theForm.toString());
            return true;
        } catch (Exception e) {
            Log.e(tag, "Error occurred in ProcessForm:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean DisplayForm() {

        try {
            ScrollView sv = new ScrollView(getActivity());

            final LinearLayout ll = new LinearLayout(getActivity());
            sv.addView(ll);
            ll.setOrientation(android.widget.LinearLayout.VERTICAL);

            // walk through the form elements and dynamically create them,
            // leveraging the mini library of tools.
            int i;
            for (i = 0; i < theForm.fields.size(); i++) {
                if (theForm.fields.elementAt(i).getType().equals("text")) {
                    theForm.fields.elementAt(i).obj = new
                            XmlGuiEditBox(getActivity(), (theForm.fields.elementAt(i).isRequired()
                            ? "*" : "") + theForm.fields.elementAt(i).getLabel(), "");
                    ll.addView((View) theForm.fields.elementAt(i).obj);
                }
                if (theForm.fields.elementAt(i).getType().equals("numeric")) {
                    theForm.fields.elementAt(i).obj = new
                            XmlGuiEditBox(getActivity(), (theForm.fields.elementAt(i).isRequired()
                            ? "*" : "") + theForm.fields.elementAt(i).getLabel(), "");
                    ((XmlGuiEditBox) theForm.fields.elementAt(i).obj).makeNumeric();
                    ll.addView((View) theForm.fields.elementAt(i).obj);
                }
                if (theForm.fields.elementAt(i).getType().equals("choice")) {
                    theForm.fields.elementAt(i).obj = new
                            XmlGuiPickOne(getActivity(), (theForm.fields.elementAt(i).isRequired()
                            ? "*" : "") + theForm.fields.elementAt(i).getLabel(),
                            theForm.fields.elementAt(i).getOptions());
                    ll.addView((View) theForm.fields.elementAt(i).obj);
                }
            }


            Button btn = new Button(getActivity());
            btn.setLayoutParams(new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.
                            WRAP_CONTENT));

            ll.addView(btn);

            btn.setText("Submit");
            btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // check if this form is Valid
                    if (!CheckForm()) {
                        AlertDialog.Builder bd = new AlertDialog.Builder(ll.getContext());
                        AlertDialog ad = bd.create();
                        ad.setTitle("Error");
                        ad.setMessage("Please enter all required (*) fields");
                        ad.show();
                        return;

                    }
                    if (theForm.getSubmitTo().equals("loopback")) {
                        // just display the results to the screen
                        String formResults = theForm.getFormattedResults();
                        Log.i(tag, formResults);
                        AlertDialog.Builder bd = new AlertDialog.Builder(ll.getContext());
                        AlertDialog ad = bd.create();
                        ad.setTitle("Results");
                        ad.setMessage(formResults);
                        ad.show();
                        return;

                    } else {
                        if (!SubmitForm()) {
                            AlertDialog.Builder bd = new AlertDialog.Builder(ll.getContext());
                            AlertDialog ad = bd.create();
                            ad.setTitle("Error");
                            ad.setMessage("Error submitting form");
                            ad.show();
                            return;
                        }
                    }

                }
            });

            getActivity().setContentView(sv);
            getActivity().setTitle(theForm.getFormName());

            return true;

        } catch (Exception e) {
            Log.e(tag, "Error Displaying Form");
            return false;
        }
    }

    private boolean CheckForm()
    {
        try {
            int i;
            boolean good = true;


            for (i=0;i<theForm.fields.size();i++) {
                String fieldValue = (String)
                        theForm.fields.elementAt(i).getData();
                Log.i(tag,theForm.fields.elementAt(i)
                        .getName() + " is [" + fieldValue + "]");
                if (theForm.fields.elementAt(i).isRequired()) {
                    if (fieldValue == null) {
                        good = false;
                    } else {
                        if (fieldValue.trim().length() == 0) {
                            good = false;
                        }
                    }

                }
            }
            return good;
        } catch(Exception e) {
            Log.e(tag,"Error in CheckForm()::" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean SubmitForm()
    {
        try {
            boolean ok = true;
            this.progressDialog = ProgressDialog.show(getActivity(),
                    theForm.getFormName(), "Saving Form Data", true,false);
            this.progressHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    switch (msg.what) {
                        case 0:
                            // update progress bar
                            progressDialog.setMessage("" + (String) msg.obj);
                            break;
                        case 1:
                            progressDialog.cancel();
////                            finish();
                            break;
                        case 2:
                            progressDialog.cancel();
                            break;
                    }
                    super.handleMessage(msg);
                }
            };

////            Thread workthread = new Thread(new TransmitFormData(theForm));

////            workthread.start();

            return ok;
        } catch (Exception e) {
            Log.e(tag,"Error in SubmitForm()::" + e.getMessage());
            e.printStackTrace();
            // tell user that the submission failed....
            Message msg = new Message();
            msg.what = 1;
            this.progressHandler.sendMessage(msg);

            return false;
        }

    }
}
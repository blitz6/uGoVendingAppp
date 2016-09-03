package com.ugosmoothie.ugovendingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ugosmoothie.ugovendingapp.Admin.AdministratorActivity;
import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.ugosmoothie.ugovendingapp.MachineCommunication.Arduino;
import com.ugosmoothie.ugovendingapp.Pivotal.FlexPointQSR;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PurchaseSmoothie extends AppCompatActivity {

    private uGoViewPager m_uGoViewPage;
    private AsyncServer asyncServer;
    private Locale myLocale;
    private Boolean lang_french = true;
    private int number_of_clicks = 0;

    private Arduino arduino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arduino = new Arduino(this);

        //Initializing Kiosk Mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        lock.disableKeyguard();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_purchase_smoothie); //Bringing the Layout Live

        refresh_curr_frag();

        m_uGoViewPage.setSwipeEnabled(false); //Disabling swipe through Pages

        m_uGoViewPage.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {

                        TextView val1 = (TextView) findViewById(R.id.element_4_val_1);
                        TextView val2 = (TextView) findViewById(R.id.element_4_val_2);
                        TextView val3 = (TextView) findViewById(R.id.element_4_val_3);
                        final TextView selectedSmoothie = (TextView) findViewById(R.id.element_1_val);
                        final ImageView smoothie = (ImageView) findViewById(R.id.smoothie_tag);
                        final TextView selectedLiquid_1 = (TextView) findViewById(R.id.element_2_val_1);
                        final TextView selectedLiquid_2 = (TextView) findViewById(R.id.element_2_val_2);
                        final TextView selectedLiquid_3 = (TextView) findViewById(R.id.element_2_val_3);
                        final TextView selectedSupplement_1 = (TextView) findViewById(R.id.element_3_val_1);
                        final TextView selectedSupplement_2 = (TextView) findViewById(R.id.element_3_val_2);
                        final TextView selectedSupplement_3 = (TextView) findViewById(R.id.element_3_val_3);

                        //Updating Total Cost on Summary Bar in real-time
                        if (val1 != null)
                            val1.setText("$" + CurrentSelection.getInstance().getTotal() + "0");
                        if (val2 != null)
                            val2.setText("$" + CurrentSelection.getInstance().getTotal() + "0");
                        if (val3 != null)
                            val3.setText("$" + CurrentSelection.getInstance().getTotal() + "0");

                        //Updating Smoothie Image on Summary Bar in real-time
                        update_smoothie_text_and_image(selectedSmoothie, smoothie);

                        //Updating Liquid on Summary Barin real-time
                        update_liquid_text(selectedLiquid_1);
                        update_liquid_text(selectedLiquid_2);
                        update_liquid_text(selectedLiquid_3);

                        //Updating Supplement on Summary Bar in real-time
                        update_supplement_text(selectedSupplement_1);
                        update_supplement_text(selectedSupplement_2);
                        update_supplement_text(selectedSupplement_3);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );

        //Button for Admin Portal (Located Centred & Top of Layout)
        Button administrator_button = (Button) findViewById(R.id.admin_button);
        administrator_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
                        number_of_clicks++;
                        if (number_of_clicks > 6) {
                            Intent intent = new Intent(PurchaseSmoothie.this, AdministratorActivity.class);
                            startActivityForResult(intent, 1);
                        }
                    }
                }).start();
            }
        });

        //Initializing the Async Server
        asyncServer = AsyncServer.getInstance();
        //asyncServer.registerListener(this, "complete");
        this.registerReceiver(receiver, new IntentFilter("complete"));
        this.registerReceiver(paymentCompleteReceiver, new IntentFilter("paymentComplete"));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equals("quit")) {
                    finish();
                    System.exit(0);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void update_smoothie_text_and_image(TextView textView, ImageView imageView) {
        if (textView != null && imageView != null) {
            switch (CurrentSelection.getInstance().getCurrentSmoothie()) {
                case 0: {
                    textView.setText(getResources().getString(R.string.smoothie_1));
                    imageView.setBackground(getResources().getDrawable(R.drawable.green_smoothie));
                }
                break;
                case 1: {
                    textView.setText(getResources().getString(R.string.smoothie_2));
                    imageView.setBackground(getResources().getDrawable(R.drawable.pink_smoothie));
                }
                break;
                case 2: {
                    textView.setText(getResources().getString(R.string.smoothie_3));
                    imageView.setBackground(getResources().getDrawable(R.drawable.purple_smoothie));
                }
                break;
                default:
                    textView.setText("");
                    break;
            }
        }
    }

    public void update_liquid_text(TextView textView) {

        if (textView != null) {
            switch (CurrentSelection.getInstance().getCurrentLiquid()) {
                case 0: {
                    textView.setText(getResources().getString(R.string.liquid_1));
                }
                break;
                case 1: {
                    textView.setText(getResources().getString(R.string.liquid_2));
                }
                break;
                case 2: {
                    textView.setText(getResources().getString(R.string.liquid_3));
                }
                break;
                default:
                    textView.setText("");
                    break;
            }
        }
    }

    public void update_supplement_text(TextView textView) {
        if (textView != null) {
            switch (CurrentSelection.getInstance().getCurrentSupplement()) {
                case 1: {
                    textView.setText(getResources().getString(R.string.supplement_1));
                }
                break;
                case 2: {
                    textView.setText(getResources().getString(R.string.supplement_2));
                }
                break;
                case 3: {
                    textView.setText(getResources().getString(R.string.supplement_3));
                }
                break;
                case 4: {
                    textView.setText(getResources().getString(R.string.supplement_4));
                }
                break;
                default:
                    textView.setText("");
                    break;
            }
        }
    }

    //Refreshing the current fragment
    public void refresh_curr_frag() {
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager) findViewById(R.id.container);
        int currentItem = m_uGoViewPage.getCurrentItem();
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(currentItem);
    }

    //Refreshing the entire App & Clearing the Data of selected products
    public void refresh() {
        int na = -1;
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager) findViewById(R.id.container);
        CurrentSelection.getInstance().setCurrentSmoothie(na);
        CurrentSelection.getInstance().setCurrentLiquid(na);
        CurrentSelection.getInstance().setCurrentSupplement(na);
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(0);
    }

    public uGoViewPager GetUGoViewPager() {
        return m_uGoViewPage;
    }

    //Small pop-up that brings the App to the first fragment after receiving a "complete" message from client app
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String eventType = intent.getStringExtra("orderId");
            //if (eventType.equals(EventTypes.SmoothieEvent.Complete.getSmoothieEvent())) {
            //we finished a smoothie
            Toast.makeText(getApplicationContext(), "Your Order is ready!!", Toast.LENGTH_SHORT).show();
            m_uGoViewPage.setCurrentItem(0);
            //}
        }
    };

    //Disables the Volume Rockers
    private final List blockedKeys = new ArrayList(Arrays.asList(KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_VOLUME_UP));
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (blockedKeys.contains(event.getKeyCode())) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    //Language Switch in Locale
    public void setLocal(String language) {
        myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public void ToggleLanguage() {
        if (lang_french) {
            setLocal("en");
            lang_french = false;
        } else {
            setLocal("fr");
            lang_french = true;
        }

        refresh_curr_frag();
    }

    //Disable the back button of the tablet
    @Override
    public void onBackPressed() {
        // nothing to do here
        // … really
    }

    //Disable Long power button press
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            // Close every kind of system dialog
            Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            sendBroadcast(closeDialog);
        }
    }

    public void paymentComplete() {
        Purchase purchase = new Purchase(
                (long)CurrentSelection.getInstance().getCurrentSmoothie(),
                (long)CurrentSelection.getInstance().getCurrentLiquid(),
                (long)CurrentSelection.getInstance().getCurrentSupplement(),
                false,
                CurrentSelection.getInstance().getTotal()
        );
        purchase.save();
        // send the purchase to any listening clients
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                // SEND ARDUINO START

                GetUGoViewPager().setCurrentItem(4);
                arduino.SendAutoCycleMessage();
            }
        });
        builder.setMessage("Make sure the lid is removed and put the cup in the holder.\nClick OK when ready");
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Saving the final order into Sugar ORM and sending message to client App
    private BroadcastReceiver paymentCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Purchase purchase = new Purchase(
                    (long)CurrentSelection.getInstance().getCurrentSmoothie(),
                    (long)CurrentSelection.getInstance().getCurrentLiquid(),
                    (long)CurrentSelection.getInstance().getCurrentSupplement(),
                    false,
                    CurrentSelection.getInstance().getTotal()
            );
            purchase.save();
            // send the purchase to any listening clients
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
// Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    // SEND ARDUINO START
                }
            });
            builder.setMessage("Make sure the lid is removed and put the cup in the holder.\nClick OK when ready");
// Create the AlertDialog
            AlertDialog dialog = builder.create();
        }
    };

    FlexPointQSR fpQSR;

    public void paymentRequest() {
        // connect to the server
        new connectTask().execute("");

    }

    public class connectTask extends AsyncTask<String,String,FlexPointQSR> {

        @Override
        protected FlexPointQSR doInBackground(String... message) {

            //we create a TCPClient object and
            fpQSR = new FlexPointQSR(new FlexPointQSR.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            fpQSR.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String[] parsedMessage = values[0].split("\\^");

            for (int i = 0; i < parsedMessage.length; i = i + 2) {
                if (parsedMessage[i].equals(FlexPointQSR.FlexPointMessageId.PosResultCode.getValue())) {
                    if (parsedMessage[i + 1].equals("000")) {
                        // payment accepted
                        View paymentCompleteView = (View)findViewById(R.id.paymentCompleteInfo);
                        paymentCompleteView.setVisibility(View.VISIBLE);

                        final Button okay_button = (Button) findViewById((R.id.okay_ready_to_start));

                        okay_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                View paymentCompleteView = (View)findViewById(R.id.paymentCompleteInfo);
                                paymentCompleteView.setVisibility(View.INVISIBLE);
                                paymentComplete();

                            }
                        });

                    } else {
                        // payment declined or error
                        paymentComplete();
                    }
                    break;
                } else if (parsedMessage[i].equals(FlexPointQSR.FlexPointMessageId.UserCanceled.getValue())) {
                    Toast.makeText(PurchaseSmoothie.this, "canceled", Toast.LENGTH_LONG);
                    // payment canceled
                    paymentComplete();
                }
            }
        }
    }
}
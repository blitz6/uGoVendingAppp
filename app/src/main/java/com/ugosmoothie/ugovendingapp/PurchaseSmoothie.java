package com.ugosmoothie.ugovendingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

import java.util.Locale;

public class PurchaseSmoothie extends AppCompatActivity {
    // test change for reviewable.io
    private uGoViewPager m_uGoViewPage;
    private AsyncServer asyncServer;
    private Locale myLocale;
    private Boolean lang_french = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_smoothie);
        refresh();
        m_uGoViewPage.setSwipeEnabled(true);
        // start the AsyncServer
        asyncServer = AsyncServer.getInstance();
        asyncServer.registerListener(this, "complete");
        this.registerReceiver(receiver, new IntentFilter("complete"));
    }

    private void refresh() {
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager)findViewById(R.id.container);
        int currentItem = m_uGoViewPage.getCurrentItem();
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(currentItem);
    }

    public uGoViewPager GetUGoViewPager() { return m_uGoViewPage; }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String eventType = intent.getStringExtra("eventType");
            if (eventType.equals(EventTypes.SmoothieEvent.Complete.getSmoothieEvent())) {
                //we finished a smoothie
                Toast.makeText(getApplicationContext(), "Your Order is ready!!", Toast.LENGTH_SHORT).show();
                m_uGoViewPage.setCurrentItem(0);

            }
        }
    };

    //Set Language in Locale
    public void setLocal(String language)
    {
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

        refresh();
    }
}

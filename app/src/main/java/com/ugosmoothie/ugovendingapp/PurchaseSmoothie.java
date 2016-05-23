package com.ugosmoothie.ugovendingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

public class PurchaseSmoothie extends AppCompatActivity {
    // test change for reviewable.io
    private uGoViewPager m_uGoViewPage;
    private AsyncServer asyncServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_smoothie);
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());

        m_uGoViewPage = (uGoViewPager)findViewById(R.id.container);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          @Override
            public void onPageSelected(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        m_uGoViewPage.setSwipeEnabled(false);

        // start the AsyncServer
        asyncServer = AsyncServer.getInstance();
        asyncServer.registerListener(this, "complete");
        this.registerReceiver(receiver, new IntentFilter("complete"));
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
}

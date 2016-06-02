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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugosmoothie.ugovendingapp.Data.CurrentSelection;
import com.ugosmoothie.ugovendingapp.Fragments.SupplementSelectionFragment;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

import java.io.File;
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
        refresh_curr_frag();
        m_uGoViewPage.setSwipeEnabled(false);

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

                        if (val1 != null)
                            val1.setText("$" + CurrentSelection.getInstance().getTotal());
                        if (val2 != null)
                            val2.setText("$" + CurrentSelection.getInstance().getTotal());
                        if (val3 != null)
                            val3.setText("$" + CurrentSelection.getInstance().getTotal());


                        final TextView selectedSmoothie = (TextView) findViewById(R.id.element_1_val);
                        final ImageView smoothie = (ImageView) findViewById(R.id.smoothie_tag);
                        update_smoothie_text_and_image(selectedSmoothie, smoothie);


                        final TextView selectedLiquid_1 = (TextView) findViewById(R.id.element_2_val_1);
                        final TextView selectedLiquid_2 = (TextView) findViewById(R.id.element_2_val_2);
                        final TextView selectedLiquid_3 = (TextView) findViewById(R.id.element_2_val_3);
                        update_liquid_text(selectedLiquid_1);
                        update_liquid_text(selectedLiquid_2);
                        update_liquid_text(selectedLiquid_3);

                        final TextView selectedSupplement_1 = (TextView) findViewById(R.id.element_3_val_1);
                        final TextView selectedSupplement_2 = (TextView) findViewById(R.id.element_3_val_2);
                        final TextView selectedSupplement_3 = (TextView) findViewById(R.id.element_3_val_3);
                        update_supplement_text(selectedSupplement_1);
                        update_supplement_text(selectedSupplement_2);
                        update_supplement_text(selectedSupplement_3);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
        // start the AsyncServer
        asyncServer = AsyncServer.getInstance();
        asyncServer.registerListener(this, "complete");
        this.registerReceiver(receiver, new IntentFilter("complete"));
    }

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

    public void refresh_curr_frag() {
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager)findViewById(R.id.container);
        int currentItem = m_uGoViewPage.getCurrentItem();
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(currentItem);
    }

    public void refresh_text() {

    }

    public void refresh(){
        int na = -1;
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager)findViewById(R.id.container);
        CurrentSelection.getInstance().setCurrentSmoothie(na);
        CurrentSelection.getInstance().setCurrentLiquid(na);
        CurrentSelection.getInstance().setCurrentSupplement(na);
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(0);
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

        refresh_curr_frag();
    }

}

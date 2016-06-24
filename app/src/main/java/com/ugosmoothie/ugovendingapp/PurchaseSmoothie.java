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
import com.ugosmoothie.ugovendingapp.Data.Purchase;
import com.orm.SugarRecord;
import com.ugosmoothie.ugovendingapp.Fragments.SupplementSelectionFragment;
import com.ugosmoothie.ugovendingapp.WebServer.AsyncServer;

import java.io.File;
import java.util.Locale;

//Moneris Api import
/*
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.moneris.terminal.TerminalReceipt;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;

import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.ToggleButton;
import android.widget.EditText;

import com.moneris.terminal.TerminalService;
*/

public class PurchaseSmoothie extends AppCompatActivity {
    // test change for reviewable.io
    private uGoViewPager m_uGoViewPage;
    private AsyncServer asyncServer;
    private Locale myLocale;
    private Boolean lang_french = true;

    //VARIABLES FOR moneris BEGIN SA03062016
/*    private final static String TAG = "com.example.EtrinAndroid.MainActivity";
    private final static String monerisHost = "esqa.moneris.com";
    private final static String storeId = "store_ppi";
    private final static String apiToken = "ppiguy";

    private Button btnPurchase;
    private Button btnPreauth;
    private Button btnBatchClose;
    private Button btnRefund;
    private Button btnPurchaseCorrection;
    private Button btnCompletion;
    private Button btnInitialization;
    private Button btnForcePost;
    private Button btnUseLast;
    private Button btnIndRefund;
    private ToggleButton tglTip;
    private ToggleButton tglManualEntry;
    private TerminalReceipt localReceiptCopy = null;

    private TextView viewReceipt;

    private String lastOrderId = null;
    private String lastTxnNumber = null;*/
    //VARIABLES FOR moneris BEGIN END  SA03062016
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
                            val1.setText("$" + CurrentSelection.getInstance().getTotal() + "0");
                        if (val2 != null)
                            val2.setText("$" + CurrentSelection.getInstance().getTotal() + "0");
                        if (val3 != null)
                            val3.setText("$" + CurrentSelection.getInstance().getTotal() + "0");


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
            String eventType = intent.getStringExtra("orderId");
            //if (eventType.equals(EventTypes.SmoothieEvent.Complete.getSmoothieEvent())) {
                //we finished a smoothie
                Toast.makeText(getApplicationContext(), "Your Order is ready!!", Toast.LENGTH_SHORT).show();
                m_uGoViewPage.setCurrentItem(0);

            //}
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

    public void Infopopup(){
        SmoothiePagerAdapter mSmoothiePagerAdapter = new SmoothiePagerAdapter(getSupportFragmentManager());
        m_uGoViewPage = (uGoViewPager)findViewById(R.id.container);
        int setItem = 5;
        m_uGoViewPage.setAdapter(null);
        m_uGoViewPage.setAdapter(mSmoothiePagerAdapter);
        m_uGoViewPage.setCurrentItem(setItem);
    }


    //moneris API BEGIN SA03062016
/*
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        retrieveGuiIds();
        linkGuiActions();
        toggleButton(false);

        startTerminalService();
        bindTerminalService();
        //moneris API END SA03062016
    }
//Moneris Functions begin SA03062016
private void linkGuiActions()
{
    btnPurchase.setOnClickListener(financialButtonClicked);
    btnPreauth.setOnClickListener(financialButtonClicked);
    btnPurchaseCorrection.setOnClickListener(financialButtonClicked);
    btnCompletion.setOnClickListener(financialButtonClicked);
    btnRefund.setOnClickListener(financialButtonClicked);
    btnIndRefund.setOnClickListener(financialButtonClicked);
    btnBatchClose.setOnClickListener(financialButtonClicked);
    btnForcePost.setOnClickListener(financialButtonClicked);
    btnInitialization.setOnClickListener(financialButtonClicked);
    btnUseLast.setOnClickListener(useLastButtonClicked);

    tglTip.setOnCheckedChangeListener(tipToggled);


    viewReceipt.setMovementMethod(new ScrollingMovementMethod());
}

    private void retrieveGuiIds()
    {
        btnPurchase = (Button)findViewById(R.id.btnPurchase);
        btnPreauth = (Button)findViewById(R.id.btnPreauth);
        btnBatchClose = (Button)findViewById(R.id.btnBatchClose);
        btnPurchaseCorrection = (Button)findViewById(R.id.btnPurchaseCorrection);
        btnCompletion = (Button)findViewById(R.id.btnCompletion);
        btnInitialization = (Button)findViewById(R.id.btnInitialization);
        btnRefund = (Button)findViewById(R.id.btnRefund);
        btnIndRefund = (Button)findViewById(R.id.btnIndRefund);
        btnForcePost = (Button)findViewById(R.id.btnForcePost);
        btnUseLast = (Button)findViewById(R.id.btnUseLast);
        tglManualEntry = (ToggleButton)findViewById(R.id.tglManualEntry);
        tglTip = (ToggleButton)findViewById(R.id.tglTip);

        viewReceipt = (TextView)findViewById(R.id.viewReceipt);
    }


    private void toggleButton(boolean state)
    {
        Log.d(TAG, ((state) ? "Enabling " : "Disabling ") + "all buttons.");
        terminalReady = state;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Log.d(TAG, ("Terminal ready state is: " + terminalReady));
                btnPurchase.setEnabled(terminalReady);
                btnPreauth.setEnabled(terminalReady);
                btnPurchaseCorrection.setEnabled(terminalReady);
                btnRefund.setEnabled(terminalReady);
                btnIndRefund.setEnabled(terminalReady);
                btnCompletion.setEnabled(terminalReady);
                btnBatchClose.setEnabled(terminalReady);
                btnInitialization.setEnabled(terminalReady);
                btnForcePost.setEnabled(terminalReady);
                btnUseLast.setEnabled(terminalReady && (lastOrderId != null || lastTxnNumber != null));
                tglManualEntry.setEnabled(terminalReady);
                tglTip.setEnabled(terminalReady);
            }
        });

    }

    private OnClickListener useLastButtonClicked = new OnClickListener()
    {
        public void onClick(View buttonView)
        {
            Log.d(TAG, "Got a button click from ["+((Button)buttonView).getText()+"]");
            final String finalLastOrderId = lastOrderId;
            final String finalLastTxnNumber = lastTxnNumber;
            runOnUiThread(new Runnable() {
                public void run()
                {
                    ((EditText)findViewById(R.id.txtOrderId)).setText(finalLastOrderId);
                    ((EditText)findViewById(R.id.txtTxnNumber)).setText(finalLastTxnNumber);
                }
            });
        }
    };

    private OnClickListener financialButtonClicked = new OnClickListener()
    {
        public void onClick(View buttonView)
        {
            Log.d(TAG, "Got a button click from ["+((Button)buttonView).getText()+"]");
            handleFinanicalTransactions(buttonView.getId());
        }
    };

    private OnCheckedChangeListener tipToggled = new OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            handleTipToggle(isChecked);

        }
    };

    private String receiptText;

    @Override
    protected void onDestroy() {
        stopTerminalService();
        super.onDestroy();
    }

    protected void handleTipToggle(boolean isChecked)
    {
        try
        {
            sped.setTip(isChecked);
        }
        catch (Exception ex)
        {
            Log.d(TAG, "tip toggle failed: "+ ex.getMessage());
        }
    }

    protected void handleFinanicalTransactions(int id)
    {
        Log.d(TAG, "Handling financial transaction");
        String orderId = ((EditText)findViewById(R.id.txtOrderId)).getText().toString();
        String amount = ((EditText)findViewById(R.id.txtAmount)).getText().toString();
        String cc_num = null;
        String cc_expdate = null;

        Random rand = new Random();
        String rand_auth_code = "000000" + (rand.nextInt(1000000) % 1000000);
        String auth_code = rand_auth_code.substring(rand_auth_code.length() - 6);

        String ecom = ((EditText)findViewById(R.id.txtTxnNumber)).getText().toString();
        String txnNumber = ((EditText)findViewById(R.id.txtTxnNumber)).getText().toString();
        String cust_id = ((EditText)findViewById(R.id.txtCustId)).getText().toString();

        toggleButton(false);
        setReceiptText("");

        if (((ToggleButton)findViewById(R.id.tglManualEntry)).isChecked())
        {
            cc_num = "M";
        }

        try
        {
            switch (id)
            {
                case R.id.btnPurchase:
                    sped.purchase(orderId, amount, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnPreauth:
                    sped.creditPreauth(orderId, amount, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnCompletion:
                    //sped.creditPreauthCompletion(orderId, amount, txnNumber, cc_num, cc_expdate, ecom);
                    sped.creditPreauthCompletion(orderId, amount, txnNumber, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnRefund:
                    //sped.refund(orderId, amount, txnNumber, cc_num, cc_expdate, ecom);
                    sped.refund(orderId, amount, txnNumber, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnIndRefund:
                    //sped.independentRefund(orderId, amount, cc_num, cc_expdate, ecom);
                    sped.independentRefund(orderId, amount, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnForcePost:
                    //sped.forcePost(orderId, amount, cc_num, cc_expdate, auth_code, ecom);
                    sped.forcePost(orderId, amount, cc_num, cc_expdate, auth_code, cust_id, ecom);
                    break;
                case R.id.btnPurchaseCorrection:
                    //sped.purchaseCorrection(lastOrderId, amount, txnNumber, cc_num, cc_expdate, ecom);
                    sped.purchaseCorrection(orderId, amount, txnNumber, cc_num, cc_expdate, cust_id, ecom);
                    break;
                case R.id.btnBatchClose:
                    sped.batchClose();
                    break;
                case R.id.btnInitialization:
                    sped.initialization();
                    break;
                default:
                    break;
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, "finanacial transaction failed "+ex.getMessage());
        }

    }

    private void setReceiptText(String rcptText)
    {
        Log.d(TAG, "About to print receipt...");
        receiptText = rcptText;

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                viewReceipt.setText(receiptText);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    */
/* Terminal Service variables *//*

    private boolean terminalCreated = false;
    private boolean terminalConnected = false;
    private boolean terminalReady = false;


    private TerminalService sped = null;
    private TerminalServiceConnection terminalConn = null;

    private  boolean tip = false;

    private void bindTerminalService()
    {
        Intent i = new Intent(this, TerminalService.class);
        terminalConn = new TerminalServiceConnection();
        getApplicationContext().bindService(i, terminalConn, Context.BIND_AUTO_CREATE);
    }

    private void startTerminalService()
    {
        Intent i = new Intent (this, TerminalService.class);
        i.putExtra("DEBUG", true);
        Log.d(TAG, "About to start terminal service...");
        getApplicationContext().startService(i);
    }

    private void stopTerminalService()
    {
        sped.disconnect();
        Intent i = new Intent (this, TerminalService.class);
        getApplicationContext().stopService(i);
    }

    private void onSpedCreated()
    {
        try
        {
            sped.initialize();
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Cannot initialize sped: " + ex.getMessage());
        }
    }

    class TerminalServiceConnection implements ServiceConnection
    {

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder boundService)
        {
            try
            {
                sped = ((com.moneris.terminal.TerminalService.TerminalBinder)boundService).getService();
                onTerminalServiceBinded();
            }
            catch (Exception ex)
            {
                Log.d(TAG, ex.getMessage());
                sped = null;
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            sped = null;
        }

    }

	*/
/* Terminal Handlers *//*


    @Override
    public void processStatus(int status)
    {
        Log.d(TAG, "Got terminal status [" + status + "]");
    }

    public void onTerminalServiceBinded()
    {
        if (sped != null)
        {
            List<String> terminalAddresses = sped.getTerminalAddresses();

            if (terminalAddresses != null)
            {
                String address = terminalAddresses.get(0);

                sped.setBluetoothAddress(address);

                sped.setHost(monerisHost);
                sped.setStoreId(storeId);
                sped.setApiToken(apiToken);
                sped.setSocketTimeout(120000);

                //sped.setKeepAlive(30 * 1000);

                //sped.setStoreId("moncp00006");
                //sped.setApiToken("gyZ5Otx8FFvVJo6VMrhT");

                sped.setEventHandler(this);

                sped.connect();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "There are no paired devices", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void processTransactionCompleted()
    {
        Log.d(TAG, "Got terminal completed");
        try
        {
            if (!terminalCreated)
            {
                terminalCreated = true;
            }

            if (localReceiptCopy != null)
            {
                sendEmailReceipt(localReceiptCopy);
                localReceiptCopy = null;
            }
            toggleButton(true);
            generateNewOrderId();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void generateNewOrderId()
    {
        final String newOrderId = "android_"+(System.currentTimeMillis()/1000);
        runOnUiThread(new Runnable() {
            public void run()
            {
                ((EditText)findViewById(R.id.txtOrderId)).setText(newOrderId.toCharArray(), 0, newOrderId.length());
            }
        });
    }

    private void sendEmailReceipt(TerminalReceipt receipt)
    {
        Log.d(TAG, "Building receipt url");

        StringBuilder emailBuilder = new StringBuilder();
        final String emailURL;

        emailBuilder.append("https://"+monerisHost+"/emvreceipt/index.php?");

        emailBuilder.append("order_no=" + receipt.getReceiptId() + "&");
        emailBuilder.append("cust_id=" + receipt.getCustId() + "&");
        emailBuilder.append("amount=" + receipt.getAmount() + "&");
        emailBuilder.append("transaction_no=" + receipt.getTransactionNo() + "&");
        emailBuilder.append("first4last4=" + receipt.getPan() + "&");
        emailBuilder.append("card_type=" + receipt.getCardType() + "&");
        emailBuilder.append("response_code=" + receipt.getResponseCode() + "&");
        emailBuilder.append("reference_num=" + receipt.getRefNum() + "&");
        emailBuilder.append("approval_code=" + receipt.getAuthCode() + "&");
        emailBuilder.append("iso_code=" + receipt.getIsoCode() + "&");
        emailBuilder.append("transaction_time=" + receipt.getTransTime() + "&");
        emailBuilder.append("transaction_date=" + receipt.getTransDate() + "&");
        emailBuilder.append("account_type=" + receipt.getAccountType() + "&");
        emailBuilder.append("receipt_language=" + receipt.getLang() + "&");
        emailBuilder.append("tip_amount=" + receipt.getTipAmount() + "&");
        emailBuilder.append("form_amount=" + receipt.getAmount() + "&");
        emailBuilder.append("transaction_code=" + receipt.getTransType() + "&");
        emailBuilder.append("message=" + receipt.getMessage() + "&");
        emailBuilder.append("emv_app_name=" + receipt.getAppPreferredName() + "&");
        emailBuilder.append("TSI=" + receipt.getTSI() + "&");
        emailBuilder.append("emv_app_label=" + receipt.getAppLabel() + "&");
        emailBuilder.append("emv_card_plan=" + receipt.getCardPlan() + "&");
        emailBuilder.append("emv_aid=" + receipt.getAid() + "&");
        emailBuilder.append("emv_pan_entry=" + receipt.getPanEntry() + "&");
        emailBuilder.append("emv_arqc=" + receipt.getARQC() + "&");
        emailBuilder.append("emv_tvr_arqc=" + receipt.getTvrARQC() + "&");
        emailBuilder.append("emv_tc_acc=" + receipt.getTCACC() + "&");
        emailBuilder.append("emv_tvr_tc_acc=" + receipt.getTvrTCACC() + "&");
        emailBuilder.append("emv_cvm=" + receipt.getCvmIndicator() + "&");
        emailBuilder.append("error_message=" + receipt.getErrorMessage() + "&");
        emailBuilder.append("error_code=" + receipt.getErrorCode());

        emailURL = emailBuilder.toString().replaceAll(" ", "%20");
        // Making HTTP Request
		*/
/* try
		{
			HttpClient httpClient = new DefaultHttpClient();
    			Log.d(TAG, "Http Request:" + emailBuilder.toString());
			HttpPost httpPost = new HttpPost(emailBuilder.toString());

    		HttpResponse response = httpClient.execute(httpPost);
    		Log.d(TAG, "Http Response:" + response.toString());
		}
		catch (Exception e)
		{
    			Log.d(TAG, "Error on httpPost: "+ e.getMessage(), e);
		}
		*//*


        try
        {
            Thread emailThread = new Thread(new Runnable() {
                public void run()
                {
                    try
                    {
                        HttpClient httpClient = new DefaultHttpClient();
                        Log.d(TAG, "Http Request:" + emailURL);
                        HttpPost httpPost = new HttpPost(emailURL);

                        HttpResponse response = httpClient.execute(httpPost);
                        Log.d(TAG, "Http Response:" + response.toString());

                    }
                    catch (Exception ex)
                    {
                        Log.d(TAG, "Error on httpPost: "+ ex.getMessage(), ex);
                    }
                }
            });
            emailThread.run();
        }
        catch (Exception e)
        {
            Log.d(TAG, "Error on creating thread for httpPost: "+ e.getMessage(), e);
        }
    }

    @Override
    public void processReceipt(TerminalReceipt receipt)
    {
        Log.d(TAG, "Got terminal receipt");
        StringBuilder sb = new StringBuilder("Terminal Receipt: \n\n");

        sb.append("TerminalId: " + receipt.getTerminalId() + "\n");
        sb.append("CustId: " + receipt.getCustId() + "\n");
        sb.append("ExpDate: " + receipt.getExpDate() + "\n");
        sb.append("TransType: " + receipt.getTransType() + "\n");
        sb.append("ReceiptId: " + receipt.getReceiptId() + "\n");
        sb.append("RefNum: " + receipt.getRefNum() + "\n");
        sb.append("IsoCode: " + receipt.getIsoCode() + "\n");
        sb.append("CardType: " + receipt.getCardType() + "\n");
        sb.append("AuthCode: " + receipt.getAuthCode() + "\n");
        sb.append("Completed: " + receipt.getCompleted() + "\n");
        sb.append("Message: " + receipt.getMessage() + "\n");
        sb.append("TransTime: " + receipt.getTransTime() + "\n");
        sb.append("TransDate: " + receipt.getTransDate() + "\n");
        sb.append("Lang: " + receipt.getLang() + "\n");
        sb.append("AccountType: " + receipt.getAccountType() + "\n");
        sb.append("Ecom: " + receipt.getEcom() + "\n");
        sb.append("Pan: " + receipt.getPan() + "\n");
        sb.append("TransactionNo: " + receipt.getTransactionNo() + "\n");
        sb.append("StoreId: " + receipt.getStoreId() + "\n");
        sb.append("OrderId: " + receipt.getOrderId() + "\n");
        sb.append("ApiToken: " + receipt.getApiToken() + "\n");
        sb.append("Amount: " + receipt.getAmount() + "\n");
        sb.append("ResponseCode: " + receipt.getResponseCode() + "\n");
        sb.append("ErrorMessage: " + receipt.getErrorMessage() + "\n");
        sb.append("TipAmount: " + receipt.getTipAmount() + "\n");
        sb.append("Track2: " + receipt.getTrack2() + "\n");
        sb.append("PurchaseCount: " + receipt.getPurchaseCount() + "\n");
        sb.append("PurchaseAmount: " + receipt.getPurchaseAmount() + "\n");
        sb.append("RefundCount: " + receipt.getRefundCount() + "\n");
        sb.append("RefundAmount: " + receipt.getRefundAmount() + "\n");
        sb.append("CorrectionCount: " + receipt.getCorrectionCount() + "\n");
        sb.append("CorrectionAmount: " + receipt.getCorrectionAmount() + "\n");
        sb.append("MCEmvData1: " + receipt.getMCEmvData1() + "\n");
        sb.append("MCEmvData2: " + receipt.getMCEmvData2() + "\n");
        sb.append("MCEmvData3: " + receipt.getMCEmvData3() + "\n");
        sb.append("AppPreferredName: " + receipt.getAppPreferredName() + "\n");
        sb.append("AppLabel: " + receipt.getAppLabel() + "\n");
        sb.append("CardPlan: " + receipt.getCardPlan() + "\n");
        sb.append("Aid: " + receipt.getAid() + "\n");
        sb.append("PanEntry: " + receipt.getPanEntry() + "\n");
        sb.append("ARQC: " + receipt.getARQC() + "\n");
        sb.append("TvrARQC: " + receipt.getTvrARQC() + "\n");
        sb.append("TCACC: " + receipt.getTCACC() + "\n");
        sb.append("TvrTCACC: " + receipt.getTvrTCACC() + "\n");
        sb.append("CvmIndicator: " + receipt.getCvmIndicator() + "\n");
        sb.append("ServiceCode: " + receipt.getServiceCode() + "\n");
        sb.append("TSI: " + receipt.getTSI() + "\n");
        sb.append("ErrorMessage: " + receipt.getErrorMessage() + "\n");
        sb.append("ErrorCode: " + receipt.getErrorCode() + "\n");
        sb.append("Ticket: " + receipt.getTicket() + "\n");
        sb.append("TimedOut: " + receipt.getTimedOut() + "\n");

        sb.append("EchoData: " + receipt.getEchoData() + "\n");

        lastOrderId = receipt.getReceiptId();
        lastTxnNumber = receipt.getTransactionNo();

        Log.d(TAG, sb.toString());

        setReceiptText(sb.toString());

        localReceiptCopy = receipt;
        //sendEmailReceipt(receipt);
    }

    @Override
    public void processDisplay(String msg)
    {
        Log.d(TAG, "Got terminal display: [" + msg +"]");
    }

    @Override
    public void terminalConnected()
    {
        terminalConnected = true;
        Toast.makeText(getApplicationContext(), "Terminal connected", Toast.LENGTH_SHORT).show();
        if (terminalCreated)
        {
            toggleButton(true);
        }
        else
        {
            Log.d(TAG, "About to call sped Initialize");
            try
            {
                if (sped != null)
                {
                    sped.initialize();
                }
            }
            catch (Exception ex)
            {
                Log.d(TAG, "init failed..."+ex.getMessage());
            }
        }
    }

    @Override
    public void terminalDisconnected()
    {
        this.terminalConnected = true;
        Toast.makeText(getApplicationContext(), "Terminal disconnected", Toast.LENGTH_SHORT).show();
        if (terminalCreated)
        {
            toggleButton(false);
        }
    }
*/

//Moneris Functions End SA03062016

}

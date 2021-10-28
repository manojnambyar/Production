package in.geofoods.production;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "com.geofoods.production.MainActivity";
    //ui
    TextView tvDate;

    //vars
    Double LI, GI, CI, MV, GC, GD, CC , AP,PP,WP; // finished products variables
    Double iR, iBR, iU, vU, dRR, dIR, dU, dC, dF, C; // raw materials variables
    Double gc, greenChilly, ginger, onion, tamarint, salt, mustard, redChilly, coconutOil, water, totChutney, extra; // chutney raw materials variables
    private String smsRM, smsCCRM, smsOrdr;
    private ArrayList<String> addressee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: reached");
        tvDate = findViewById(R.id.dateText);

        setdate();
    }

    //getting order data into variables from the text field
    private void getOrderData() {
        smsOrdr = "\n*Ord:*";
        Log.d(TAG, "getOrderData: reached");
        LinearLayout orderGroup = findViewById(R.id.orderGroup);
        for (int i = 0; i < orderGroup.getChildCount(); i++) {
            View v = orderGroup.getChildAt(i);
            if (v instanceof LinearLayout) {

                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    View v2 = ((LinearLayout) v).getChildAt(j);

                    if (v2 instanceof EditText && v2.getTag() != null) {
                        switch (v2.getTag().toString()) {
                            case "LI":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    LI = 0.0;
                                } else {
                                    LI = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr += "\nLI-" + LI;
                                }
                                break;
                            case "GI":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    GI = 0.0;
                                } else {
                                    GI = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nGI-"+GI;
                                }
                                break;
                            case "CI":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    CI = 0.0;
                                } else {
                                    CI = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nCI-"+CI;
                                }
                                break;
                            case "MV":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    MV = 0.0;
                                } else {
                                    MV = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nMV-"+MV;
                                }
                                break;
                            case "GC":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    GC = 0.0;
                                } else {
                                    GC = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nGC-"+GC;
                                }
                                break;
                            case "GD":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    GD = 0.0;
                                } else {
                                    GD = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nGD-"+GD;
                                }
                                break;
                            case "CC":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    CC = 0.0;
                                } else {
                                    CC = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nCC-"+CC;
                                }
                                break;
                            case "AP":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    AP = 0.0;
                                } else {
                                    AP = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nAP-"+AP;
                                }
                                break;
                            case "PP":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    PP = 0.0;
                                } else {
                                    PP = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nPP-"+PP;
                                }
                                break;
                            case "WP":
                                if (((EditText) v2).getText().toString().isEmpty()) {
                                    WP = 0.0;
                                } else {
                                    WP = Double.parseDouble(((EditText) v2).getText().toString());
                                    smsOrdr+="\nWP-"+WP;
                                }
                                break;

                        }
                    }

                }

            } else {
//                Log.d(TAG, "getOrderData: ");
            }


        }
//        Log.d(TAG, "getOrderData 1: "+ (LI+GI+CI+MV+GC+GD+CC+AP+PP+WP));
        calculateRM();

    }


    //setting initial date and selected date from date picker
    private void setdate() {

        tvDate.setText(new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date()));
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Order Date");
        final MaterialDatePicker materialDatePicker = builder.build();
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), TAG);

            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                tvDate.setText(materialDatePicker.getHeaderText());
            }

        });
    }

    private void sendSMS(String message, String mNumber) {
        Log.d(TAG, "sendSMS: Reached");
        try {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            } else {
                // Permission has already been granted
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mNumber, null, message, null, null);
                Toast.makeText(this, " SEND TO " + mNumber, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "sendSMS:  error is :" + e.toString());
            Toast.makeText(this, "Error is " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(this, "Clicked : "+ view.getTag() , Toast.LENGTH_SHORT).show();
        switch (view.getTag().toString()) {

            case "calc":
                getOrderData();
//                Log.d(TAG, "onClick: calculator");
                break;

            case "waproduction":
//                Log.d(TAG, "onClick: waproduction");

                Toast.makeText(this, "waproduction", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: wa production : " + smsRM);

                if (smsRM != null) {
                    sendWA(smsRM+smsOrdr);
                } else {
                    Toast.makeText(this, "Please calculate RM before sending ", Toast.LENGTH_SHORT).show();
                }
//                sendWA(smsRM);
                break;

            case "smsproduction":
                Log.d(TAG, "onClick: sms production  reached : " + smsRM);
                if (smsRM != null) {
                    setupSMS(smsRM+smsOrdr);
                } else {
                    Toast.makeText(this, "Please calculate RM before sending", Toast.LENGTH_SHORT).show();
                }
                break;

            case "wacc":
//                Log.d(TAG, "onClick: wacc");
                Toast.makeText(this, "wacc", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: wa cc " + smsCCRM);
                if (smsCCRM != null) {
                    sendWA(smsCCRM);
                } else {
                    Toast.makeText(this, "Please calculate RM before sending ", Toast.LENGTH_SHORT).show();
                }
//                sendWA(smsCCRM);
                break;

            case "smscc":
//                Log.d(TAG, "onClick: smscc");
                Log.d(TAG, "onClick: smscc " + smsCCRM);
                if (smsCCRM != null) {
                    setupSMS(smsCCRM);
                } else {
                    Toast.makeText(this, "Please calculate RM before sending ", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void sendWA(String message) {
        getAdressee();
        if (isWhatsappInstalled("com.whatsapp")) {

            if (addressee == null || addressee.isEmpty() || addressee.size() == 0) {
                Toast.makeText(this, "No receiver selected,No message send", Toast.LENGTH_SHORT).show();
            } else {

                for (int i = 0; i < addressee.size(); i++) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + addressee.get(i) + "&text=" + message));
                    startActivity(intent);
                }
                Toast.makeText(this, "Whatsapp message send ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isWhatsappInstalled(String uri) {
        PackageManager packageManager = getPackageManager();
        boolean waInstalled;
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            waInstalled = true;
        } catch (Exception e) {
            waInstalled = false;
        }

        return waInstalled;
    }

    private void setupSMS(String message) {
//        Log.d(TAG, "setupSMS: sending to : " + addressee.toString());
        getAdressee();
        if (addressee == null || addressee.isEmpty() || addressee.size() == 0) {
            Toast.makeText(this, "No receiver selected,No SMS send", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < addressee.size(); i++) {
                sendSMS(message, addressee.get(i));
            }
            //Toast.makeText(this, "SMS send ", Toast.LENGTH_SHORT).show();

        }
    }

    private void getAdressee() {
        addressee = new ArrayList<>();
        LinearLayout receiverGroup = findViewById(R.id.receiverGroup);
        for (int i = 0; i < receiverGroup.getChildCount(); i++) {
            View v = receiverGroup.getChildAt(i);
//            Log.d(TAG, "getAdressee: childposition " + i);
            if (v instanceof CheckBox && ((CheckBox) v).isChecked()) {
//                Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is checked");
                switch (v.getTag().toString()) {
                    case "Manoj":
                        addressee.add("9820730135");
//                        Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is cheked");
                        break;
                    case "Balika":
                        addressee.add("8879733626");
//                        Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is cheked");
                        break;
                    case "Padma":
                        addressee.add("9324471943");
//                        Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is cheked");
                        break;
                    case "Shiva":
                        addressee.add("9967280169");
//                        Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is cheked");
                        break;
                    case "Sabitha":
                        addressee.add("9820240135");
//                        Log.d(TAG, "getAdressee: "+v.getTag().toString()+" is cheked");
                        break;
                }
            }

        }
//        Log.d(TAG, "getAdressee: " + addressee.toString());
    }


    // set RM to the text field for view and messaging
    private void setRMToTextView() {
        smsRM = "*RM*";
        smsCCRM = "*CC*";
        //code to set rm to tv+
        Log.d(TAG, "setRMToTextView: reached");
        LinearLayout RMGroup = findViewById(R.id.RMGroup);
        LinearLayout chutneyRMCGroup = findViewById(R.id.chutneyRMGroup);

        for (int i = 0; i < RMGroup.getChildCount(); i++) {
            View v = RMGroup.getChildAt(i);
            if (v instanceof LinearLayout) {

                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    View v2 = ((LinearLayout) v).getChildAt(j);

                    if (v2 instanceof TextView && v2.getTag() != null) {
//                        Log.d(TAG, "setRMToTextView: "+v2.getTag().toString());
                        DecimalFormat formatter = new DecimalFormat("#,##0.000");
//                        formatter.setMaximumFractionDigits(3);
//                        formatter.setMinimumFractionDigits(3);
//                        formatter.setMinimumIntegerDigits(1);
//                        formatter.setRoundingMode(RoundingMode.HALF_UP);

                        switch (v2.getTag().toString()) {
                            case "iR":
                                ((TextView) v2).setText(formatter.format(iR));
//                                ((TextView) v2).setText(iR.toString());
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(iR);
                                break;
                            case "iBR":
                                ((TextView) v2).setText(formatter.format(iBR));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(iBR);
                                break;
                            case "iU":
                                ((TextView) v2).setText(formatter.format(iU));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(iU);
                                break;
                            case "vU":
                                ((TextView) v2).setText(formatter.format(vU));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(vU);
                                break;
                            case "dRR":
                                ((TextView) v2).setText(formatter.format(dRR));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(dRR);
                                break;
                            case "dIR":
                                ((TextView) v2).setText(formatter.format(dIR));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(dIR);
                                break;
                            case "dU":
                                ((TextView) v2).setText(formatter.format(dU));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(dU);
                                break;
                            case "dC":
                                ((TextView) v2).setText(formatter.format(dC));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(dC);
                                break;
                            case "dF":
                                ((TextView) v2).setText(formatter.format(dF));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(dF);
                                break;
                            case "C":
                                ((TextView) v2).setText(formatter.format(C));
//                                Log.d(TAG, "setRMToTextView: "+((TextView) v2).getText());
                                smsRM += "\n" + v2.getTag().toString() + "-" + formatter.format(C);
                                break;
                        }


                    }
                }
            }
        }


        for (int i = 0; i < chutneyRMCGroup.getChildCount(); i++) {
            View v = chutneyRMCGroup.getChildAt(i);
            if (v instanceof LinearLayout) {

                for (int j = 0; j < ((LinearLayout) v).getChildCount(); j++) {
                    View v2 = ((LinearLayout) v).getChildAt(j);

                    if (v2 instanceof TextView && v2.getTag() != null) {

//                        Log.d(TAG, "setChutneyRMToTextView: "+v2.getTag().toString());
                        DecimalFormat formatter = new DecimalFormat();
                        formatter.setMaximumFractionDigits(3);
                        formatter.setMinimumFractionDigits(3);
                        formatter.setMinimumIntegerDigits(1);
                        formatter.setRoundingMode(RoundingMode.HALF_UP);

                        switch (v2.getTag().toString()) {
                            case "gc":
                                ((TextView) v2).setText(formatter.format(gc));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(gc);
                                break;
                            case "greenChilly":
                                ((TextView) v2).setText(formatter.format(greenChilly));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(greenChilly);
                                break;
                            case "ginger":
                                ((TextView) v2).setText(formatter.format(ginger));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(ginger);
                                break;
                            case "onion":
                                ((TextView) v2).setText(formatter.format(onion));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(onion);
                                break;
                            case "tamarint":
                                ((TextView) v2).setText(formatter.format(tamarint));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(tamarint);
                                break;
                            case "salt":
                                ((TextView) v2).setText(formatter.format(salt));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(salt);
                                break;
                            case "mustard":
                                ((TextView) v2).setText(formatter.format(mustard));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(mustard);
                                break;
                            case "redChilly":
                                ((TextView) v2).setText(formatter.format(redChilly));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(redChilly);
                                break;
                            case "coconutOil":
                                ((TextView) v2).setText(formatter.format(coconutOil));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(coconutOil);
                                break;
                            case "water":
                                ((TextView) v2).setText(formatter.format(water));
//                                Log.d(TAG, "setRMToTextView: " + ((TextView) v2).getText());
                                smsCCRM += "\n" + v2.getTag().toString() + "-" + formatter.format(water);
                                break;
                        }
                    }
                }
            }
        }
    }

    //calculation of RM for production
    public void calculateRM() {
        Log.d(TAG, "calculateRM: reached");


//        ************************************** Define variables for calculation
        Double lIdliMix = 7.00;
        Double gIdliMix = 4.00;
        Double cIdliMix = 3.33333;
        Double dosaMix = 6.6666666666;
        Double boilMix = 0.0775;

//        ****************************************** calculating raw materials required.

//        *******************************calculate Rice to soak*******************
        iR = 0.00;

        if (LI == 0.0)
            //Log.i("Info :", "Value is zero");
            iR = iR + 0.00;
        else
            iR = iR + LI / 2.46405;
        //Log.i("Info : ", "Value is not zero");

        if (GI == 0.0)
            iR = iR + 0.00;
        else
            iR = iR + (GI / 3.46744 / (1 + boilMix));

        if (CI == 0.0)
            iR = iR + 0.00;
        else
            iR = iR + (CI + 2) / 3.62350 / (1 + boilMix);


        //*******************************calculate Rice to Boil for Idli *******************
        iBR = 0.00;
        if (GI == 0.0)
            iBR = iBR + 0.00;
        else
            iBR = iBR + ((GI / 3.46744 / (1 + boilMix)) * boilMix);

        if (CI == 0.0)
            iBR = iBR + 0.00;
        else
            iBR = iBR + (((CI + 1) / 3.62350 / (1 + boilMix)) * boilMix);

        //******************************calculate Urad for Idli*************
        iU = 0.00;

        if (LI == 0.0)
            iU = iU + 0.00;
        else
            iU = iU + ((LI / 2.46405) / lIdliMix);

        if (GI == 0.0)
            iU = iU + 0.00;
        else
            iU = iU + (((GI / 3.46744)) / gIdliMix);

        if (CI == 0.0)
            iU = iU + 0.00;
        else
            iU = iU + ((((CI + 1) / 3.62350)) / cIdliMix);

        //***************************** calculate urad for vada************
        vU = 0.00;
        if (MV == 0.0)
            vU = vU + 0.00;
        else
            vU = vU + ((MV) / 2.20);

        //********************* calculate coconut for grated coconut************
        C = 0.00;

        if (GC == 0.0)
            C = C + 0.00;
        else
            C = C + (GC / 0.21);


        //*************************** calculate ingredients for dosa *****************
        dRR = GD / 3.48488 * 0.8;
        dIR = GD / 3.48488 * 0.2;
        dU = (dRR + dIR) / dosaMix;
        dC = dU / 2;
        dF = (dRR + dIR + dU + dC) * 0.008;

        // ************************ calculate ingredients for chutney******************

        //Double gratedcoconut,greenchilly,ginger,onion,tamarint,salt,mustard,redchilly,oil,water; // chutney raw materials variables
        if (CC > 0) {
            extra = 150.000;
        } else {
            extra = 0.000;
        }
        gc = ((CC * 300 + extra) * 140.535945 / 450) / 1000;
        greenChilly = ((CC * 300 + extra) * 5.621438 / 450) / 1000;
        ginger = ((CC * 300 + extra) * 1.873813 / 450) / 1000;
        onion = ((CC * 300 + extra) * 3.747625 / 450) / 1000;
        tamarint = ((CC * 300 + extra) * 6.919053 / 450) / 1000;
        salt = ((CC * 300 + extra) * 7.248532 / 450) / 1000;
        mustard = ((CC * 300 + extra) * 2.810719 / 450) / 1000;
        redChilly = ((CC * 300 + extra) * 1.873813 / 450) / 1000;
        coconutOil = ((CC * 300 + extra) * 9.369063 / 450) / 1000;
        water = ((CC * 300 + extra) * 270.000 / 450) / 1000;
        totChutney = gc + greenChilly + ginger + onion + tamarint + salt + mustard + redChilly + coconutOil + water;

        setRMToTextView();

    }
}
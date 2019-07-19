package com.demo.reynzdev.javadroiddemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button emailButton = findViewById(R.id.emailBtn);
        Button batButton = findViewById(R.id.batBtn);
        Button callButton = findViewById(R.id.callBtn);

        emailButton.setOnClickListener(this);
        batButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.emailBtn:
                sendEmail();
                break;
            case R.id.callBtn:
                call();
                break;
            case R.id.batBtn:
                checkBatteryStatus();
                break;
            default:
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9988776655"));
        startActivity(intent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,"johndoe@example.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Hi! I am sending you a test email");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBatteryStatus() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver(this);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        MainActivity mMainActivity;

        public MyBroadcastReceiver(MainActivity mainActivity) {
            mMainActivity = mainActivity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // status
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            switch (status) {
                case 1:         // Unknown
                case 3:         // Discharging
                case 4:         // NotCharging
                case 5:         // Full
                    int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    float batPercentage = (level/(float)scale) * 100;
                    String batLevel = "Battery remaining " + (int)batPercentage + "%";
                    Toast.makeText(mMainActivity,batLevel,Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(mMainActivity,"Charging",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}


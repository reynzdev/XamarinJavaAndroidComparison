package com.demo.reynzdev.javadroiddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Starting Dialer
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:9988776655"));
        startActivity(intent);

        // Sending Email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","johndoe@example.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello World!");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"Hi! I am sending you a test email");

        if(emailIntent.resolveActivity(getPackageManager()) != null){
            startActivity(Intent.createChooser(emailIntent, "Choose your mail application"));
        }else{
            // inform the user that no email clients are installed or provide an alternative
        }
    }
}

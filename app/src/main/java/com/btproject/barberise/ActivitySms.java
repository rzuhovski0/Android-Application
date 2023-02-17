package com.btproject.barberise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivitySms extends AppCompatActivity {

    EditText phoneNumberEditText;
    Button sendButton;
    SmsManager smsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        phoneNumberEditText = findViewById(R.id.phoneNoEditText);
        sendButton = findViewById(R.id.sendSmsButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = phoneNumberEditText.getText().toString();
                if(phoneNo.equals(""))
                    return;
                Intent intent = new Intent(getApplicationContext(),VerifyPhoneNoActivity.class);
                intent.putExtra("phoneNo",phoneNo);
                startActivity(intent);
            }
        });
    }

    public void sendMessage() {
        String phoneNumber = phoneNumberEditText.getText().toString();

        String message = "test";
        if(!phoneNumber.equals("")) {
            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent);
        }
    }
}

package com.example.sudokusolver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class devActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting up the cutom title in the actionbar of the application in this devActivity
        setTitle(R.string.devactivity);
        setContentView(R.layout.activity_dev);
        textView = findViewById(R.id.textView);
        String message = "Hello there I am Aditya Kumar. Thank you for downloading my application.\n" +
                "Feel free to visit my website DevConnect and check out my other projects.\n" +
                "here is my link:- https://radiant-bastion-62859.herokuapp.com/profile/3947f970-07f0-4863-bdf6-0d247c0b2e82/";
        textView.setText(message);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        devActivity.this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
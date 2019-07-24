package com.example.ugcch.hazira;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ugcch.hazira.login.UserSessionManager;

public class Deactivate_Screen extends AppCompatActivity {
    Button btnBack;
    UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate__screen);
        session = new UserSessionManager(getApplicationContext());
        btnBack = findViewById(R.id.goBacks);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });
    }
}

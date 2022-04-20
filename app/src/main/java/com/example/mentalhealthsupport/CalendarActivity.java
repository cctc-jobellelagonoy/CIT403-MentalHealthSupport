package com.example.mentalhealthsupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class CalendarActivity extends AppCompatActivity{
    private TextView tvDate, tvSupport, tvWhat;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String userId;
    private User currentUser;
    Button btnLogout, btnChat, btnReport;

    TextView tvDatePicker;
    CountdownView myCountdownView;

    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        userId = getIntent().getStringExtra(Login.ID_KEY);

        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        databaseUsers = mdatabase.getReference();
        getUser();

        myCountdownView = findViewById(R.id.mycountdown);

        tvWhat = findViewById(R.id.tvWhat);
        btnReport = findViewById(R.id.btnReport);
        tvDate = (TextView)findViewById(R.id.tvDate);
        btnLogout = findViewById(R.id.btnLogout);
        tvSupport = findViewById(R.id.tvSupport);
        btnChat = findViewById(R.id.btnChat);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
        date = dateFormat.format(calendar.getTime());
        tvDate.setText(date);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CalendarActivity.this, CometChatUI.class));
            }
        });

        tvSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, Quotes.class);
                intent.putExtra(Login.ID_KEY, userId);
                startActivity(intent);
                finish();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent symptomsintent = new Intent(CalendarActivity.this, ReportActivity.class);
                symptomsintent.putExtra(Login.ID_KEY, currentUser.getId());
                symptomsintent.putExtra("name", currentUser.getFullName());
                startActivity(symptomsintent);
                finish();
            }
        });

        tvWhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CalendarActivity.this, WhatShouldDo.class);
                startActivity(i);
            }
        });
    }

    private void getIsolationDate(long pickerDate){


        try {

            Date now = new Date();

            long currentDate = now.getTime();
            long countDownToPickerDate = pickerDate - currentDate;
            myCountdownView.start(countDownToPickerDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        getUser();
//    }

    public void getUser(){
        Log.d("USER ID", "getUser: "+userId);
        databaseUsers.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                Log.d("CurrentUser", "onDataChange: "+currentUser.getId());
                getIsolationDate(currentUser.getIsolationEnd());
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Log.d("for", "onDataChange: "+snapshot1.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

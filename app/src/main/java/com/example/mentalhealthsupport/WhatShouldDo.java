package com.example.mentalhealthsupport;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WhatShouldDo extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<com.example.mentalhealthsupport.MobileOS> mobileOSes;
    private com.example.mentalhealthsupport.RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_should_do);
        recyclerView = findViewById(R.id.recycler_view);
        mobileOSes = new ArrayList<>();

        setData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new com.example.mentalhealthsupport.RecyclerAdapter(this, mobileOSes);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setData() {
        ArrayList<com.example.mentalhealthsupport.Phone> directory1 = new ArrayList<>();
        directory1.add(new com.example.mentalhealthsupport.Phone("Ateneo Bulatao Center"));
        directory1.add(new com.example.mentalhealthsupport.Phone("Bayanihan at Makatimed"));
        directory1.add(new com.example.mentalhealthsupport.Phone("Camp Navarro General Hospital"));
        directory1.add(new com.example.mentalhealthsupport.Phone("EAMC Wellness Service"));


        ArrayList<com.example.mentalhealthsupport.Phone> directory2 = new ArrayList<>();
        directory2.add(new com.example.mentalhealthsupport.Phone("CARAGA Open Helpline"));
        directory2.add(new com.example.mentalhealthsupport.Phone("CLVMHP Online Counseling & Psychotherapy"));
        directory2.add(new com.example.mentalhealthsupport.Phone("DOC CHD-5 Bicol Region"));


        ArrayList<com.example.mentalhealthsupport.Phone> directory3 = new ArrayList<>();
        directory3.add(new com.example.mentalhealthsupport.Phone("Occidental Mindoro Provincial Hospital"));


        ArrayList<com.example.mentalhealthsupport.Phone> directory4 = new ArrayList<>();
        directory4.add(new com.example.mentalhealthsupport.Phone("Philippine Mental Health Association (PMHA)"));
        directory4.add(new com.example.mentalhealthsupport.Phone("Eastern Visayas Regional Medical Center"));
        directory4.add(new com.example.mentalhealthsupport.Phone("Espada Psychological Consultancy"));

        ArrayList<com.example.mentalhealthsupport.Phone> directory5 = new ArrayList<>();
        directory5.add(new com.example.mentalhealthsupport.Phone("National Center for Mental Health - 0917 899 8727 (USAP)"));
        directory5.add(new com.example.mentalhealthsupport.Phone("Natasha Goulbourn Foundation (NGF) - 0917 558 HOPE (4673)"));
        directory5.add(new com.example.mentalhealthsupport.Phone("Living Free Foundation - 0917 883 3161"));
        directory5.add(new com.example.mentalhealthsupport.Phone("Mood Harmony -  (02) 844-2941"));
        directory5.add(new com.example.mentalhealthsupport.Phone("UGAT Foundation -  (02) 8-426-6496"));

        mobileOSes.add(new com.example.mentalhealthsupport.MobileOS("NATIONAL CAPITAL REGION", directory1));
        mobileOSes.add(new com.example.mentalhealthsupport.MobileOS("BICOL REGION", directory2));
        mobileOSes.add(new com.example.mentalhealthsupport.MobileOS("MIMAROPA", directory3));
        mobileOSes.add(new com.example.mentalhealthsupport.MobileOS("VISAYA", directory4));
        mobileOSes.add(new com.example.mentalhealthsupport.MobileOS("HOTLINES", directory5));
    }

}
package com.example.mentalhealthsupport;



import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class MobileOS extends ExpandableGroup<Phone> {

    public MobileOS(String title, List<com.example.mentalhealthsupport.Phone> items) {
        super(title, items);
    }
}
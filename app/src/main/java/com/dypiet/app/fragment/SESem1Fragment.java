package com.dypiet.app.fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class SESem1Fragment extends SubjectsFragment{

    private String branch;
    private String year;

    @Override
    public Query getQuery() {
        Query query = FirebaseDatabase.getInstance ().getReference ().child ("Quiz").child (branch).child(year).child ("Sem1");
        return query;
    }

    public void setData(String year, String branch){
        this.year = year;
        this.branch = branch;
    }

    @Override
    protected String getBranch() {
        return branch;
    }

}

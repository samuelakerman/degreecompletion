package edu.brandeis.cs.cosi153.majortracker;

import android.widget.ProgressBar;

/**
 * Created by sebastian on 12/1/17.
 */

public class ObjectEntry {

    private String majorName;
    private ProgressBar firstBar = null;
    //ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressbar);


    public ObjectEntry(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajor(String majorName) {
        this.majorName = majorName;
    }

}

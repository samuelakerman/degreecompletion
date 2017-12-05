package edu.brandeis.cs.cosi153.majortracker;

import android.widget.ProgressBar;

/**
 * Created by skye_wang on 12/1/17.
 */

public class ObjectEntry {

    private String majorName;
    private ProgressBar progressBar = null;
    public int progress=0;


    public ObjectEntry(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorName() {
        return majorName;
    }

    public ProgressBar getProgressBar(){
        return  progressBar;
    }
    public void setProgressBar(ProgressBar bar){
        progressBar=bar;
    }

}

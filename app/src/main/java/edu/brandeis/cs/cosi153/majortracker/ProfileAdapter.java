package edu.brandeis.cs.cosi153.majortracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class ProfileAdapter extends ArrayAdapter {

    private final ArrayList<ObjectEntry> data;
    private final Context context;
    public static HashMap<String,Integer> progress = new HashMap<String,Integer>();

    public ProfileAdapter(Context context, ArrayList<ObjectEntry> data) {
        super(context, R.layout.object_entry, data);
        this.context = context;
        this.data = data;
    }

    public void addMajors(ObjectEntry item) {
        data.add(item);
    }

    public ArrayList<ObjectEntry> getData() {
        return data;
    }

    public int getCount() {
        return data.size();
    }

    public ObjectEntry getItem(int index) {
        return data.get(index);
    }

    public long getItemId(int index) {
        return index;
    }

    public View getView(int index, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View entryView = inflater.inflate(R.layout.object_entry, parent, false);

            TextView name = (TextView) entryView.findViewById(R.id.textViewMajor);
            ProgressBar bar = (ProgressBar) entryView.findViewById(R.id.progressBarMajor);
            name.setText(data.get(index).getMajorName());
            bar.setMax(0);
            bar.setMax(100);
            bar.setProgress(progress.get(data.get(index).getMajorName())*100/10);
            bar.refreshDrawableState();
            return entryView;
        }
        return view;
    }

    public boolean contains(String major){
        for(ObjectEntry o : data){
            if(o.getMajorName().equals(major))
                return true;
        }
        return false;
    }
}

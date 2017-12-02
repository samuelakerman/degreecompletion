package edu.brandeis.cs.cosi153.majortracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sebastian on 12/1/17.
 */

public class ProfileAdapter extends ArrayAdapter {

   // private static final android.R.attr R = ;
    private final ArrayList<ObjectEntry> data;
    private final Context context;
    //ProgressBar progress = (ProgressBar) progressbar.findViewById();


    public ProfileAdapter(Context context, ArrayList<ObjectEntry> data) {
        super(context, R.layout.object_entry, data);
        this.context = context;
        this.data = data;
        data.add(new ObjectEntry("Major 1"));
        data.add(new ObjectEntry("Major 2"));
        data.add(new ObjectEntry("Major 3"));

    }

    public void addExpense(ObjectEntry item) {
        data.add(item);
    }

    public ArrayList<ObjectEntry> getData() {
        return data;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int index) {
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
            name.setText(data.get(index).getMajorName());
            return entryView;
        }
        return view;
    }

}

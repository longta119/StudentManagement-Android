package com.example.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private boolean checked = false;
    // boolean array for storing the state of each CheckBox
    boolean[] checkBoxState;
    ViewHolder viewHolder;

    public CustomAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        checkBoxState = new boolean[values.size()];
    }

    private class ViewHolder
    {
        TextView name;
        CheckBox checkBox;

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public TextView getName() {
            return name;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.rowlayout, null);
            viewHolder = new ViewHolder();

            //cache the views
            viewHolder.name = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            //link the cached views to the convertview
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(values.get(position));

        //Set the state of the CheckBox using the boolean array
        viewHolder.checkBox.setChecked(checkBoxState[position]);

        //for managing the state of the boolean array according to the state of the CheckBox
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;
                }
                else
                    checkBoxState[position]=false;

            }
        });

        return convertView;
    }

    public boolean[] getCheckBoxState(){
        return checkBoxState;
    }

    public String getName(int pos){
        String val = values.get(pos);
        String[] arr = val.split(",");
        String val1 = arr[0];
        return val1;
    }

}

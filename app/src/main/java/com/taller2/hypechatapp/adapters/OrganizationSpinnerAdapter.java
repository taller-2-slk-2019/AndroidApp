package com.taller2.hypechatapp.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taller2.hypechatapp.R;
import com.taller2.hypechatapp.model.Organization;

import java.util.List;

public class OrganizationSpinnerAdapter extends ArrayAdapter<Organization> {

    private List<Organization> organizations;
    private Context context;

    public OrganizationSpinnerAdapter(Context context, int textViewResourceId, List<Organization> modelArrayList) {
        super(context, textViewResourceId, modelArrayList);
        this.organizations = modelArrayList;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public Organization getItem(int position) {
        return organizations.get(position);
    }

    @Override
    public int getCount() {
        int count = organizations.size();
        //return count > 0 ? count - 1 : count;
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        Organization model = getItem(position);

        View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        TextView label = spinnerRow.findViewById(android.R.id.text1);
//        label.setTextColor(context.getResources().getColor(R.color.whiteHype));
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        label.setText(String.format("%s", model != null ? model.getName() : ""));

        return spinnerRow;
    }
}
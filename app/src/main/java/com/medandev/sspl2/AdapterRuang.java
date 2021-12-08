package com.medandev.sspl2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.medandev.sspl2.DataRuang;

import java.util.List;

public class AdapterRuang  extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataRuang> item;

    public AdapterRuang(Activity activity, List<DataRuang> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_ruang, null);

        TextView ruang = (TextView) convertView.findViewById(R.id.ruang);

        DataRuang data;
        data = item.get(position);

        ruang.setText(data.getRuang());

        return convertView;
    }
}

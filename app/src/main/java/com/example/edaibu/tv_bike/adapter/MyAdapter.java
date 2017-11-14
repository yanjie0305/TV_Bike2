package com.example.edaibu.tv_bike.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.edaibu.tv_bike.R;

import java.util.List;

/**
 * Created by ${gyj} on 2017/9/22.
 */

public class MyAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> list;
    private TextView tv_name;

    public MyAdapter(Context context, List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.item, null);
        tv_name = (TextView) view1.findViewById(R.id.tv_name);
        tv_name.setText(list.get(i));
        return view1;
    }
}

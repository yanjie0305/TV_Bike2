package com.example.edaibu.tv_bike.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.edaibu.tv_bike.R;

/**
 * Created by ${gyj} on 2017/9/19.
 */

public class LeftFragment extends Fragment  {

    private View leftView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        leftView = inflater.inflate(R.layout.left_layout, null);
        return leftView;
    }

}

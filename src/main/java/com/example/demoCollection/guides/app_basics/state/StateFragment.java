package com.example.demoCollection.guides.app_basics.state;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.demoCollection.R;
import java.util.List;

/**
 * Author: JW.Liu
 * Created on 2018/6/15 15:27
 */
public class StateFragment extends Fragment {

    private List mDataList;
    private TextView mTv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_state, container, false);
        mTv = rootView.findViewById(R.id.tv);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null != mDataList) {
            mTv.setText(mDataList.toString());
        }
    }

    public void setData(List dataList) {
        mDataList = dataList;
    }

}
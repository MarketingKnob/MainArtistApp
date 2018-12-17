package com.samyotech.fabartist.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samyotech.fabartist.DTO.HistoryDTO;
import com.samyotech.fabartist.DTO.UserDTO;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.network.NetworkManager;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.ui.activity.BaseActivity;
import com.samyotech.fabartist.ui.adapter.PaidAdapter;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.utils.ProjectUtils;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private BaseActivity baseActivity;
    private PaidFrag paidFrag = new PaidFrag();
    private UnPaidFrag unPaidFrag = new UnPaidFrag();
    private FragmentManager fragmentManager;
    private CustomTextView tvPaid, tvUnPaid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.invoice));
        fragmentManager = getChildFragmentManager();
        tvUnPaid = (CustomTextView) view.findViewById(R.id.tvUnPaid);
        tvPaid = (CustomTextView) view.findViewById(R.id.tvPaid);

        tvPaid.setOnClickListener(this);
        tvUnPaid.setOnClickListener(this);

        fragmentManager.beginTransaction().add(R.id.frame, paidFrag).commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaid:
                setSelected(true, false);
                fragmentManager.beginTransaction().replace(R.id.frame, paidFrag).commit();
                break;
            case R.id.tvUnPaid:
                setSelected(false, true);
                fragmentManager.beginTransaction().replace(R.id.frame, unPaidFrag).commit();
                break;
        }

    }

    public void setSelected(boolean firstBTN, boolean secondBTN) {
        if (firstBTN) {
            tvPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvPaid.setTextColor(getResources().getColor(R.color.white));
            tvUnPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvUnPaid.setTextColor(getResources().getColor(R.color.gray));
        }
        if (secondBTN) {
            tvPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvPaid.setTextColor(getResources().getColor(R.color.gray));
            tvUnPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvUnPaid.setTextColor(getResources().getColor(R.color.white));


        }
        tvPaid.setSelected(firstBTN);
        tvUnPaid.setSelected(secondBTN);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}

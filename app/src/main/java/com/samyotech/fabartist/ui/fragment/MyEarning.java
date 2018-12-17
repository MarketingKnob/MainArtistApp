package com.samyotech.fabartist.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.samyotech.fabartist.DTO.EarningDTO;
import com.samyotech.fabartist.DTO.UserDTO;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.ui.activity.BaseActivity;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyEarning extends Fragment {
    private View view;
    private BarChart mChart;
    private String TAG = MyEarning.class.getSimpleName();
    private EarningDTO earningDTO;
    private ArrayList<EarningDTO.ChartData> chartDataList;
    private HashMap<String, String> params = new HashMap<>();
    private HashMap<String, String> paramsRequest = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private BaseActivity baseActivity;

    List<String> list = new ArrayList<String>();
    private CardView cvRequestPayment;
    String[] stringArray;
    private CustomTextViewBold tvOnlineEarning, tvCashEarning, tvWalletAmount, tvTotalEarning, tvJobDone, tvTotalJob, tvCompletePercentages;
    private DialogInterface dialog_book;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_earning, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_earnings));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        params.put(Consts.ARTIST_ID, userDTO.getUser_id());
        paramsRequest.put(Consts.USER_ID, userDTO.getUser_id());

        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        mChart = v.findViewById(R.id.chart1);
        tvOnlineEarning = v.findViewById(R.id.tvOnlineEarning);
        tvCashEarning = v.findViewById(R.id.tvCashEarning);
        tvWalletAmount = v.findViewById(R.id.tvWalletAmount);
        tvTotalEarning = v.findViewById(R.id.tvTotalEarning);
        tvJobDone = v.findViewById(R.id.tvJobDone);
        tvTotalJob = v.findViewById(R.id.tvTotalJob);
        tvCompletePercentages = v.findViewById(R.id.tvCompletePercentages);
        cvRequestPayment = v.findViewById(R.id.cvRequestPayment);


        cvRequestPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDailog();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getEarning();
    }

    public void getEarning() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.MY_EARNING1_API, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        earningDTO = new Gson().fromJson(response.getJSONObject("data").toString(), EarningDTO.class);
                        showData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    public void requestPayment() {

        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.WALLET_REQUEST_API, paramsRequest, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showLong(getActivity(), msg);
                    dialog_book.dismiss();
                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }

    public void showData() {

        tvOnlineEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getOnlineEarning());
        tvCashEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getCashEarning());
        tvWalletAmount.setText(earningDTO.getCurrency_symbol() + earningDTO.getWalletAmount());
        tvTotalEarning.setText(earningDTO.getCurrency_symbol() + earningDTO.getTotalEarning());
        tvJobDone.setText(earningDTO.getCurrency_symbol() + earningDTO.getJobDone());
        tvTotalJob.setText(earningDTO.getCurrency_symbol() + earningDTO.getTotalJob());
        tvCompletePercentages.setText(earningDTO.getCompletePercentages() + " %");


        chartDataList = new ArrayList<>();
        chartDataList = earningDTO.getChartData();

        for (int i = 0; i < chartDataList.size(); i++) {

            list.add(chartDataList.get(i).getDay());
        }

        stringArray = list.toArray(new String[0]);


        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return stringArray[(int) value % stringArray.length];
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        setData(chartDataList);

    }


    private void setData(ArrayList<EarningDTO.ChartData> charts) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < charts.size(); i++) {

            yVals1.add(new BarEntry(i, Float.parseFloat(charts.get(i).getCount())));
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getResources().getString(R.string.earning_graph));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
            mChart.invalidate();
        }
    }
    public void bookDailog() {
        try {
            new AlertDialog.Builder(getActivity())
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(getResources().getString(R.string.payment))
                    .setMessage(getResources().getString(R.string.process_payment))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            requestPayment();

                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

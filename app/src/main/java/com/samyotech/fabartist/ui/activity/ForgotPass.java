package com.samyotech.fabartist.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;


import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.network.NetworkManager;
import com.samyotech.fabartist.utils.CommonUtil;
import com.samyotech.fabartist.utils.CustomButton;
import com.samyotech.fabartist.utils.CustomEditText;
import com.samyotech.fabartist.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPass extends AppCompatActivity {
    private Context mContext;
    private CustomEditText etEmail;
    private CustomButton btnSubmit;
    private HashMap<String, String> parms = new HashMap<>();
    private String TAG = ForgotPass.class.getSimpleName();
    LinearLayout llMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        mContext = ForgotPass.this;
        setUiAction();
    }

    public void setUiAction() {

        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        llMain = findViewById(R.id.ll_main);

        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.hideKeyboard(ForgotPass.this);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.hideKeyboard(ForgotPass.this);
                submitForm();
            }
        });
    }

    public void submitForm() {
        if (!ValidateMobile()) {
            return;
        } else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                updatepass();

            } else {
                ProjectUtils.showToast(mContext, getResources().getString(R.string.internet_concation));
            }
        }
    }


    public boolean ValidateMobile() {
        if (!ProjectUtils.isEmailValid(etEmail.getText().toString().trim())) {
            etEmail.setError(getResources().getString(R.string.val_email));
            etEmail.requestFocus();
            return false;
        }
        return true;
    }

    public void updatepass() {
        parms.put(Consts.EMAIL_ID, ProjectUtils.getEditTextValue(etEmail));
        ProjectUtils.showProgressDialog(mContext, false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.FORGET_PASSWORD_API, parms, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left,
                            R.anim.anim_slide_out_left);
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }
            }
        });
    }

}

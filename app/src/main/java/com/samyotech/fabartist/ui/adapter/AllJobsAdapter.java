package com.samyotech.fabartist.ui.adapter;

/**
 * Created by mayank on 31/10/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.fabartist.DTO.AllJobsDTO;
import com.samyotech.fabartist.DTO.UserDTO;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.network.NetworkManager;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.ui.fragment.AllJobsFrag;
import com.samyotech.fabartist.utils.CustomEditText;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllJobsAdapter extends RecyclerView.Adapter<AllJobsAdapter.MyViewHolder> {
    private String TAG = AllJobsAdapter.class.getSimpleName();
    private HashMap<String, String> params = new HashMap<>();
    private Dialog dialogApplyJob;
    private Context mContext;
    private AllJobsFrag allJobsFrag;

    private ArrayList<AllJobsDTO> objects = null;
    private ArrayList<AllJobsDTO> allJobsDTOList;

    private UserDTO userDTO;
    private LayoutInflater inflater;
    private CustomEditText etAboutD, etPriceD;
    private CustomTextViewBold tvYesAbout, tvNoAbout;
    private SharedPrefrence prefrence;

    public AllJobsAdapter(AllJobsFrag allJobsFrag, ArrayList<AllJobsDTO> objects, UserDTO userDTO, LayoutInflater inflater) {
        this.allJobsFrag = allJobsFrag;
        this.mContext = allJobsFrag.getActivity();
        this.objects = objects;
        this.allJobsDTOList = new ArrayList<AllJobsDTO>();
        this.allJobsDTOList.addAll(objects);
        this.userDTO = userDTO;
        this.inflater = inflater;
        prefrence = SharedPrefrence.getInstance(mContext);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(R.layout.adapter_all_jobs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//0 = pending, 1 = confirm , 2 = complete, 3 =reject

        holder.tvJobId.setText(objects.get(position).getJob_id());
        holder.tvTitle.setText(mContext.getResources().getString(R.string.job_title)+" " + objects.get(position).getTitle());
        holder.tvDescription.setText(objects.get(position).getDescription());
        holder.tvCategory.setText(objects.get(position).getCategory_name());
        holder.tvAddress.setText(objects.get(position).getAddress());
        holder.tvName.setText(objects.get(position).getUser_name());
        holder.tvPrice.setText(objects.get(position).getCurrency_symbol() + objects.get(position).getPrice());
        holder.tvDate.setText(mContext.getResources().getString(R.string.date)+" " + ProjectUtils.changeDateFormate(objects.get(position).getCreated_at()));


        Glide.with(mContext).
                load(objects.get(position).getAvtar())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivImage);
        holder.llApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params.put(Consts.USER_ID, objects.get(position).getUser_id());
                params.put(Consts.JOB_ID, objects.get(position).getJob_id());
                params.put(Consts.ARTIST_ID, userDTO.getUser_id());
                dialogAbout();
            }
        });


    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvJobId, tvPrice;
        public CustomTextView tvDate, tvTitle, tvDescription, tvCategory, tvAddress, tvName;
        public RelativeLayout rlClick;
        public CircleImageView ivImage;
        public LinearLayout llApply;

        public MyViewHolder(View view) {
            super(view);
            tvJobId = view.findViewById(R.id.tvJobId);
            tvDate = view.findViewById(R.id.tvDate);
            rlClick = view.findViewById(R.id.rlClick);
            ivImage = view.findViewById(R.id.ivImage);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvAddress = view.findViewById(R.id.tvAddress);
            llApply = view.findViewById(R.id.llApply);
            tvName = view.findViewById(R.id.tvName);

        }
    }



    public void dialogAbout() {
        dialogApplyJob = new Dialog(mContext/*, android.R.style.Theme_Dialog*/);
        dialogApplyJob.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogApplyJob.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogApplyJob.setContentView(R.layout.dailog_apply_job);

        etPriceD = (CustomEditText) dialogApplyJob.findViewById(R.id.etPriceD);
        etAboutD = (CustomEditText) dialogApplyJob.findViewById(R.id.etAboutD);
        tvYesAbout = (CustomTextViewBold) dialogApplyJob.findViewById(R.id.tvYesAbout);
        tvNoAbout = (CustomTextViewBold) dialogApplyJob.findViewById(R.id.tvNoAbout);

        dialogApplyJob.show();
        dialogApplyJob.setCancelable(false);

        tvNoAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogApplyJob.dismiss();

            }
        });
        tvYesAbout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(etAboutD));
                        params.put(Consts.PRICE, ProjectUtils.getEditTextValue(etPriceD));
                        submitPersonalProfile();

                    }
                });
    }

    public void applyJob() {

        new HttpsRequest(Consts.APPLIED_JOB_API, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                dialogApplyJob.dismiss();
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);

                    allJobsFrag.getjobs();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(allJobsDTOList);
        } else {
            for (AllJobsDTO allJobsDTO : allJobsDTOList) {
                if (allJobsDTO.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(allJobsDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void submitPersonalProfile() {
        if (!validation(etAboutD, mContext.getResources().getString(R.string.val_des))) {
            return;
        } else if (!validation(etPriceD, mContext.getResources().getString(R.string.val_price))) {
            return;
        }  else {
            if (NetworkManager.isConnectToInternet(mContext)) {
                applyJob();
            } else {
                ProjectUtils.showToast(mContext, mContext.getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(mContext, msg);
            return false;
        } else {
            return true;
        }
    }

}
package com.samyotech.fabartist.ui.adapter;

/**
 * Created by varun on 18/07/18.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.fabartist.DTO.AllJobsDTO;
import com.samyotech.fabartist.DTO.AppliedJobDTO;
import com.samyotech.fabartist.DTO.UserDTO;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.ui.fragment.AppliedJobsFrag;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.utils.ProjectUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.MyViewHolder> {
    private String TAG = AppliedJobAdapter.class.getSimpleName();
    private HashMap<String, String> params;
    private HashMap<String, String> paramsStart;
    private DialogInterface dialog_book;
    private AppliedJobsFrag appliedJobsFrag;
    private ArrayList<AppliedJobDTO> objects = null;
    private ArrayList<AppliedJobDTO> appliedJobDTOSList;
    private UserDTO userDTO;
    private Context mContext;
    private LayoutInflater inflater;
    SimpleDateFormat sdf1, timeZone;
    private Date date;

    public AppliedJobAdapter(AppliedJobsFrag appliedJobsFrag, ArrayList<AppliedJobDTO> objects, UserDTO userDTO, LayoutInflater inflater) {
        this.appliedJobsFrag = appliedJobsFrag;
        this.objects = objects;
        this.appliedJobDTOSList = new ArrayList<AppliedJobDTO>();
        this.appliedJobDTOSList.addAll(objects);
        this.userDTO = userDTO;
        this.mContext = appliedJobsFrag.getActivity();
        this.inflater = inflater;
        sdf1 = new SimpleDateFormat(Consts.DATE_FORMATE_SERVER, Locale.ENGLISH);
        timeZone = new SimpleDateFormat(Consts.DATE_FORMATE_TIMEZONE, Locale.ENGLISH);

        date = new Date();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(R.layout.adapter_applied_job, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tvDate.setText(mContext.getResources().getString(R.string.date)+" " + ProjectUtils.changeDateFormate(objects.get(position).getCreated_at()));
        holder.tvJobId.setText(objects.get(position).getJob_id());
        holder.tvName.setText(objects.get(position).getUser_name());
        holder.tvDescription.setText(objects.get(position).getDescription());
        holder.tvAddress.setText(objects.get(position).getUser_address());
        holder.tvEmail.setText(objects.get(position).getUser_email());
        holder.tvMobile.setText(objects.get(position).getUser_mobile());

        holder.tvCategory.setText(objects.get(position).getCategory_name());
        holder.tvPrice.setText(objects.get(position).getCurrency_symbol() + objects.get(position).getPrice());

        Glide.with(mContext).
                load(objects.get(position).getUser_image())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivProfile);

        if (objects.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.llDecline.setVisibility(View.VISIBLE);
            holder.llStart.setVisibility(View.GONE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.pending));
            holder.llInpro.setVisibility(View.GONE);
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
        } else if (objects.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.llDecline.setVisibility(View.GONE);
            holder.llStart.setVisibility(View.VISIBLE);
            holder.llInpro.setVisibility(View.GONE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.confirm));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_yellow));
        } else if (objects.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.llDecline.setVisibility(View.GONE);
            holder.llStart.setVisibility(View.GONE);
            holder.llInpro.setVisibility(View.GONE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.com));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
        } else if (objects.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.llDecline.setVisibility(View.GONE);
            holder.llStart.setVisibility(View.GONE);
            holder.llInpro.setVisibility(View.GONE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.rej));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_dark_red));
        } else if (objects.get(position).getStatus().equalsIgnoreCase("5")) {
            holder.llDecline.setVisibility(View.GONE);
            holder.llStart.setVisibility(View.GONE);
            holder.llInpro.setVisibility(View.VISIBLE);
            holder.tvStatus.setText(mContext.getResources().getString(R.string.inprogres));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_dark_red));
        }


        holder.llDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params = new HashMap<>();
                params.put(Consts.AJ_ID, objects.get(position).getAj_id());
                params.put(Consts.STATUS, "3");
                rejectDialog(mContext.getResources().getString(R.string.reject), mContext.getResources().getString(R.string.reject_msg));
            }
        });

        holder.llStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsStart = new HashMap<>();
                paramsStart.put(Consts.USER_ID, objects.get(position).getUser_id());
                paramsStart.put(Consts.ARTIST_ID, objects.get(position).getArtist_id());
                paramsStart.put(Consts.DATE_STRING, sdf1.format(date).toString().toUpperCase());
                paramsStart.put(Consts.TIMEZONE, timeZone.format(date));
                paramsStart.put(Consts.PRICE, objects.get(position).getPrice());
                paramsStart.put(Consts.JOB_ID, objects.get(position).getJob_id());
                startDialog(mContext.getResources().getString(R.string.start), mContext.getResources().getString(R.string.start_app));
            }
        });
    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView tvDate, tvStatus, tvName, tvDescription, tvMobile, tvEmail, tvAddress, tvCategory;
        public CircleImageView ivProfile;
        public LinearLayout llStatus, llDecline, llStart, llInpro;
        public CustomTextViewBold tvPrice, tvJobId;

        public MyViewHolder(View view) {
            super(view);
            llStatus = view.findViewById(R.id.llStatus);
            tvStatus = view.findViewById(R.id.tvStatus);
            llStart = view.findViewById(R.id.llStart);
            tvDate = view.findViewById(R.id.tvDate);
            tvJobId = view.findViewById(R.id.tvJobId);
            tvName = view.findViewById(R.id.tvName);
            tvDescription = view.findViewById(R.id.tvDescription);
            ivProfile = view.findViewById(R.id.ivProfile);
            llDecline = view.findViewById(R.id.llDecline);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvEmail = view.findViewById(R.id.tvEmail);
            tvMobile = view.findViewById(R.id.tvMobile);
            llInpro = view.findViewById(R.id.llInpro);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvPrice = view.findViewById(R.id.tvPrice);
        }
    }

    public void reject() {

        new HttpsRequest(Consts.JOB_STATUS_ARTIST_API, params, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    dialog_book.dismiss();
                    appliedJobsFrag.getjobs();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public void startJob() {

        new HttpsRequest(Consts.START_JOB_API, paramsStart, mContext).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showToast(mContext, msg);
                    dialog_book.dismiss();
                    appliedJobsFrag.getjobs();
                } else {
                    ProjectUtils.showToast(mContext, msg);
                }


            }
        });
    }

    public void rejectDialog(String title, String msg) {
        try {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(title)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            reject();

                        }
                    })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    public void startDialog(String title, String msg) {
        try {
            new AlertDialog.Builder(mContext)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(title)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            startJob();

                        }
                    })
                    .setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(appliedJobDTOSList);
        } else {
            for (AppliedJobDTO appliedJobDTO : appliedJobDTOSList) {
                if (appliedJobDTO.getUser_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(appliedJobDTO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
package com.samyotech.fabartist.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kyleduo.switchbutton.SwitchButton;
import com.samyotech.fabartist.DTO.ArtistBookingDTO;
import com.samyotech.fabartist.DTO.ArtistDetailsDTO;
import com.samyotech.fabartist.DTO.CategoryDTO;
import com.samyotech.fabartist.DTO.GalleryDTO;
import com.samyotech.fabartist.DTO.ProductDTO;
import com.samyotech.fabartist.DTO.QualificationsDTO;
import com.samyotech.fabartist.DTO.ReviewsDTO;
import com.samyotech.fabartist.DTO.SkillsDTO;
import com.samyotech.fabartist.DTO.UserDTO;
import com.samyotech.fabartist.R;
import com.samyotech.fabartist.https.HttpsRequest;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.interfacess.Helper;
import com.samyotech.fabartist.interfacess.OnSpinerItemClick;
import com.samyotech.fabartist.network.NetworkManager;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.ui.activity.BaseActivity;
import com.samyotech.fabartist.ui.adapter.GalleryPagerAdapter;
import com.samyotech.fabartist.ui.adapter.PreviousworkPagerAdapter;
import com.samyotech.fabartist.ui.adapter.ProductPagerAdapter;
import com.samyotech.fabartist.ui.adapter.QualificationAdapter;
import com.samyotech.fabartist.ui.adapter.ReviewAdapter;
import com.samyotech.fabartist.ui.adapter.SkillsAdapter;
import com.samyotech.fabartist.utils.AutoScrollViewPager;
import com.samyotech.fabartist.utils.CustomEditText;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.utils.CustomTextViewBold;
import com.samyotech.fabartist.utils.ImageCompression;
import com.samyotech.fabartist.utils.MainFragment;
import com.samyotech.fabartist.utils.ProjectUtils;
import com.samyotech.fabartist.utils.SpinnerDialog;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ArtistProfileView extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private String TAG = ArtistProfileView.class.getSimpleName();
    private CustomTextViewBold tvNameHedar, tvName, tvReviewsText;
    private CircleImageView ivArtist;
    private CustomTextView tvWork, tvLocation, tvArtistRate, tvRating, tvJobComplete, tvProfileComplete, tvAbout, tvBio;
    private RatingBar ratingbar;
    private RecyclerView rvQualification, rvSkills, rvReviews;
    private ViewPager vpProducts, vpPreviousWork;
    private ImageView ic_left_pro, ic_right_pro, ic_left_pw, ic_right_pw;
    private ArtistDetailsDTO artistDetailsDTO = new ArtistDetailsDTO();
    private ImageView ivEditPersonal, ivEditAbout, ivEditQualification, ivEditSkils, ivEditProduct, ivEditGallery;
    private SkillsAdapter skillsAdapter;
    private QualificationAdapter qualificationAdapter;
    private ProductPagerAdapter productPagerAdapter;
    private PreviousworkPagerAdapter previousworkPagerAdapter;
    private ReviewAdapter reviewAdapter;
    private LinearLayoutManager mLayoutManagerSkills, mLayoutManagerQuali, mLayoutManagerReview;
    ArrayList<GalleryDTO> galleryList;
    private ArrayList<SkillsDTO> skillsDTOList;
    private ArrayList<SkillsDTO> skillsDTOListAdd = new ArrayList<>();

    private ArrayList<QualificationsDTO> qualificationsDTOList;
    private ArrayList<ProductDTO> productDTOList;
    private ArrayList<ArtistBookingDTO> artistBookingDTOList;
    private ArrayList<ReviewsDTO> reviewsDTOList;

    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> paramsRate = new HashMap<>();
    private HashMap<String, String> parmsSkills = new HashMap<>();
    private SharedPrefrence prefrence;
    private UserDTO userDTO;

    private Dialog dialogEditPersonal, dialogEditAbout, dialogEditQualification, dialogEditSkils, dialogEditProduct, dialogEditGallery;
    private CustomEditText etCategoryD, etNameD, etBioD, etLocationD, etRateD, etAboutD, etQaulTitleD, etQaulDesD,etCountryD,etCityD;
    private CustomEditText etImageD, etProNameD, etRateProD, etImageGallD;
    private CustomTextViewBold tvYes, tvNo, tvYesAbout, tvNoAbout, tvYesQuali, tvNoQuali, tvYesPro, tvNoPro, tvYesGall, tvNoGall;
    int my_tag = 0;
    private HashMap<String, String> paramsUpdate;

    private ArrayList<CategoryDTO> categoryDTOS;
    private HashMap<String, String> parmsCategory = new HashMap<>();

    private SpinnerDialog spinnerDialogCate;
    private int limit = -1;
    private int selected = 0;
    private LimitExceedListener limitListener;
    MyAdapterCheck myAdapterCheck;
    private HashMap<String, File> paramsFile;

    BottomSheet.Builder builder;
    Uri picUri;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    File file;
    Bitmap bitmap = null;

    public GalleryPagerAdapter galleryPagerAdapter;
    public AutoScrollViewPager viewEvents;
    public LinearLayout viewDots;
    public int dotsCount;
    private ImageView[] dots;
    private CustomTextViewBold tvOnOff;
    private Switch swOnOff;
    private BaseActivity baseActivity;
    private View view;
    private CustomTextViewBold tvText;
    private CustomTextView  tvRate;
    private SwitchButton switchRate;
    private Place place;
    private double lats = 0.0;
    private double longs = 0.0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_artist_profile_view, container, false);

        prefrence = SharedPrefrence.getInstance(getActivity());

        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        paramsRate.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.ARTIST_ID, userDTO.getUser_id());
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parmsCategory.put(Consts.USER_ID, userDTO.getUser_id());
        parmsSkills.put(Consts.USER_ID, userDTO.getUser_id());
        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_profile));


        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        viewEvents = (AutoScrollViewPager) v.findViewById(R.id.viewEvents);
        viewDots = (LinearLayout) v.findViewById(R.id.viewDots);
        swOnOff = v.findViewById(R.id.swOnOff);
        tvOnOff = v.findViewById(R.id.tvOnOff);
        tvBio = v.findViewById(R.id.tvBio);
        ivEditPersonal = v.findViewById(R.id.ivEditPersonal);
        ivEditAbout = v.findViewById(R.id.ivEditAbout);
        ivEditQualification = v.findViewById(R.id.ivEditQualification);
        ivEditSkils = v.findViewById(R.id.ivEditSkils);
        ivEditProduct = v.findViewById(R.id.ivEditProduct);
        ivEditGallery = v.findViewById(R.id.ivEditGallery);
        tvNameHedar = v.findViewById(R.id.tvNameHedar);
        tvName = v.findViewById(R.id.tvName);
        ivArtist = v.findViewById(R.id.ivArtist);
        tvRate = v.findViewById(R.id.tvRate);
        tvWork = v.findViewById(R.id.tvWork);
        tvLocation = v.findViewById(R.id.tvLocation);
        tvArtistRate = v.findViewById(R.id.tvArtistRate);
        tvRating = v.findViewById(R.id.tvRating);
        tvJobComplete = v.findViewById(R.id.tvJobComplete);
        tvProfileComplete = v.findViewById(R.id.tvProfileComplete);
        tvAbout = v.findViewById(R.id.tvAbout);
        tvReviewsText = v.findViewById(R.id.tvReviewsText);
        ratingbar = v.findViewById(R.id.ratingbar);
        rvQualification = v.findViewById(R.id.rvQualification);
        rvSkills = v.findViewById(R.id.rvSkills);
        rvReviews = v.findViewById(R.id.rvReviews);
        vpProducts = v.findViewById(R.id.vpProducts);
        vpPreviousWork = v.findViewById(R.id.vpPreviousWork);
        ic_left_pro = v.findViewById(R.id.ic_left_pro);
        ic_right_pro = v.findViewById(R.id.ic_right_pro);
        ic_left_pw = v.findViewById(R.id.ic_left_pw);
        ic_right_pw = v.findViewById(R.id.ic_right_pw);
        switchRate = v.findViewById(R.id.switchRate);

        switchRate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isShown()) {
                    if (b == true) {
                        paramsRate.put(Consts.ARTIST_COMMISSION_TYPE, "0");

                        chnageRate();
                    } else {
                        paramsRate.put(Consts.ARTIST_COMMISSION_TYPE, "1");

                        chnageRate();
                    }

                }


            }
        });


        ic_left_pro.setOnClickListener(this);
        ic_right_pro.setOnClickListener(this);
        ic_left_pw.setOnClickListener(this);
        ic_right_pw.setOnClickListener(this);

        ivEditPersonal.setOnClickListener(this);
        ivEditAbout.setOnClickListener(this);
        ivEditQualification.setOnClickListener(this);
        ivEditSkils.setOnClickListener(this);
        ivEditProduct.setOnClickListener(this);
        ivEditGallery.setOnClickListener(this);


        mLayoutManagerSkills = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerQuali = new LinearLayoutManager(getActivity().getApplicationContext());
        mLayoutManagerReview = new LinearLayoutManager(getActivity().getApplicationContext());

        rvSkills.setLayoutManager(mLayoutManagerSkills);
        rvQualification.setLayoutManager(mLayoutManagerQuali);
        rvReviews.setLayoutManager(mLayoutManagerReview);

        if (NetworkManager.isConnectToInternet(getActivity())) {
            getCategory();

        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }


        builder = new BottomSheet.Builder(getActivity()).sheet(R.menu.menu_cards);
        builder.title(getResources().getString(R.string.select_img));
        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.camera_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                try {
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    File file = getOutputMediaFile(1);
                                    if (!file.exists()) {
                                        try {
                                            ProjectUtils.pauseProgressDialog();
                                            file.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        //Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.asd", newFile);
                                        picUri = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", file);
                                    } else {
                                        picUri = Uri.fromFile(file); // create
                                    }

                                    prefrence.setValue(Consts.IMAGE_URI_CAMERA, picUri.toString());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri); // set the image file
                                    startActivityForResult(intent, PICK_FROM_CAMERA);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        break;
                    case R.id.gallery_cards:
                        if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_CAMERA, Manifest.permission.CAMERA)) {
                            if (ProjectUtils.hasPermissionInManifest(getActivity(), PICK_FROM_GALLERY, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                File file = getOutputMediaFile(1);
                                if (!file.exists()) {
                                    try {
                                        file.createNewFile();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                picUri = Uri.fromFile(file);

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_pic)), PICK_FROM_GALLERY);

                            }
                        }
                        break;
                    case R.id.cancel_cards:
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });


        swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsUpdate = new HashMap<>();
                paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
                    paramsUpdate.put(Consts.IS_ONLINE, "0");
                    isOnline();
                } else {
                    paramsUpdate.put(Consts.IS_ONLINE, "1");
                    isOnline();
                }
            }
        });
    }

    private File getOutputMediaFile(int type) {
        String root = Environment.getExternalStorageDirectory().toString();
        File mediaStorageDir = new File(root, Consts.APP_NAME);
        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    Consts.APP_NAME + timeStamp + ".png");

            imageName = Consts.APP_NAME + timeStamp + ".png";
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);

                                Log.e("image", imagePath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(getActivity());
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);


                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(prefrence.getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(prefrence
                        .getValue(Consts.IMAGE_URI_CAMERA));
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    place = PlaceAutocomplete.getPlace(getActivity(), data);
                    getAddress(place.getLatLng().latitude, place.getLatLng().longitude);
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = null;
                if (data != null) {
                    status = PlaceAutocomplete.getStatus(getActivity(), data);
                }
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }



    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(getActivity(), MainFragment.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_left_pro:
                int previous = getItemMinusPro(1);
                vpProducts.setCurrentItem(previous);
                break;
            case R.id.ic_right_pro:
                int next = getItemPlusPro(1);
                vpProducts.setCurrentItem(next);
                break;
            case R.id.ic_left_pw:
                int previousPw = getItemMinusPW(1);
                vpPreviousWork.setCurrentItem(previousPw);
                break;
            case R.id.ic_right_pw:
                int nextPw = getItemPlusPW(1);
                vpPreviousWork.setCurrentItem(nextPw);
                break;
            case R.id.ivEditPersonal:
                my_tag = 1;
                dialogPersonalProfile();
                break;
            case R.id.ivEditAbout:
                my_tag = 2;
                dialogAbout();
                break;
            case R.id.ivEditQualification:
                dialogQualification();
                break;
            case R.id.ivEditSkils:
                my_tag = 3;
                dialogSkills();

                break;
            case R.id.ivEditProduct:
                dialogProduct();
                break;
            case R.id.ivEditGallery:
                dialogGallery();
                break;



        }
    }

    public void getArtist() {
        new HttpsRequest(Consts.GET_ARTIST_BY_ID_API, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {

                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        showData();
                        parmsSkills.put(Consts.CAT_ID, artistDetailsDTO.getCategory_id());
                        getSkills();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showData() {
        tvName.setText(artistDetailsDTO.getName());
        ratingbar.setRating(Float.parseFloat(artistDetailsDTO.getAva_rating()));
        tvWork.setText(artistDetailsDTO.getCategory_name());
        tvLocation.setText(artistDetailsDTO.getLocation());
        tvRating.setText("(" + artistDetailsDTO.getAva_rating() + "/5)");
        tvJobComplete.setText(artistDetailsDTO.getJobDone() +"% "+ getResources().getString(R.string.jobs_comleted));
        tvProfileComplete.setText(artistDetailsDTO.getName());
        tvProfileComplete.setText(artistDetailsDTO.getCompletePercentages() + getResources().getString(R.string.completion));

        tvAbout.setText(artistDetailsDTO.getAbout_us());
        tvBio.setText(artistDetailsDTO.getBio());

        if (artistDetailsDTO.getIs_online().equalsIgnoreCase("1")) {
            tvOnOff.setText(getResources().getString(R.string.online));
            swOnOff.setChecked(true);

        } else {
            tvOnOff.setText(getResources().getString(R.string.offline));
            swOnOff.setChecked(false);
        }

        Glide.with(getActivity()).
                load(artistDetailsDTO.getImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivArtist);

        skillsDTOList = new ArrayList<>();
        skillsDTOList = artistDetailsDTO.getSkills();
        skillsAdapter = new SkillsAdapter(getActivity(), skillsDTOList);
        rvSkills.setAdapter(skillsAdapter);

        qualificationsDTOList = new ArrayList<>();
        qualificationsDTOList = artistDetailsDTO.getQualifications();
        qualificationAdapter = new QualificationAdapter(getActivity(), qualificationsDTOList);
        rvQualification.setAdapter(qualificationAdapter);


        artistBookingDTOList = new ArrayList<>();
        artistBookingDTOList = artistDetailsDTO.getArtist_booking();
        previousworkPagerAdapter = new PreviousworkPagerAdapter(getActivity(), artistBookingDTOList);
        vpPreviousWork.setAdapter(previousworkPagerAdapter);
        vpPreviousWork.setPageTransformer(true, new ZoomOutSlideTransformer());
        vpPreviousWork.setCurrentItem(0);
        vpPreviousWork.setOnPageChangeListener(this);

        productDTOList = new ArrayList<>();
        productDTOList = artistDetailsDTO.getProducts();
        productPagerAdapter = new ProductPagerAdapter(getActivity(), productDTOList);
        vpProducts.setAdapter(productPagerAdapter);
        vpProducts.setPageTransformer(true, new ZoomOutSlideTransformer());
        vpProducts.setCurrentItem(0);
        vpProducts.setOnPageChangeListener(this);

        reviewsDTOList = new ArrayList<>();
        reviewsDTOList = artistDetailsDTO.getReviews();
        reviewAdapter = new ReviewAdapter(getActivity(), reviewsDTOList);
        rvReviews.setAdapter(reviewAdapter);

        tvReviewsText.setText(getResources().getString(R.string.review)+" (" + reviewsDTOList.size() + ")");

        if (artistDetailsDTO.getArtist_commission_type().equalsIgnoreCase("0")) {
            switchRate.setChecked(true);
            tvRate.setText(getResources().getString(R.string.hour_rate));
            tvArtistRate.setText(getResources().getString(R.string.rate)+" "+artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice() + getResources().getString(R.string.hr));

        } else {
            switchRate.setChecked(false);
            tvRate.setText(getResources().getString(R.string.fix_rate));
            tvArtistRate.setText(getResources().getString(R.string.rate)+" "+artistDetailsDTO.getCurrency_type() + artistDetailsDTO.getPrice() +" "+ getResources().getString(R.string.fixed_rate));

        }



        galleryList = new ArrayList<>();
        galleryList = artistDetailsDTO.getGallery();
        galleryPagerAdapter = new GalleryPagerAdapter(getActivity(), galleryList, ArtistProfileView.this);
        viewEvents.setAdapter(galleryPagerAdapter);
        viewEvents.setCurrentItem(0);
        viewEvents.setOnPageChangeListener(this);
        viewEvents.startAutoScroll();
        viewEvents.setInterval(5000);
        viewEvents.setCycle(true);
        viewEvents.setStopScrollWhenTouch(true);
        setPageViewIndicator(galleryPagerAdapter, viewEvents, viewDots);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
   /*     int lastPosPro = productDTOList.size();
        if (position == 0) {
            ic_left_pro.setVisibility(View.GONE);
        } else {
            ic_left_pro.setVisibility(View.VISIBLE);
        }
        if (position == (lastPosPro - 1)) {

            ic_right_pro.setVisibility(View.GONE);
        } else {
            ic_right_pro.setVisibility(View.VISIBLE);
        }

        int lastPosPW = artistBookingDTOList.size();
        if (position == 0) {
            ic_left_pw.setVisibility(View.GONE);
        } else {
            ic_left_pw.setVisibility(View.VISIBLE);
        }
        if (position == (lastPosPW - 1)) {

            ic_right_pw.setVisibility(View.GONE);
        } else {
            ic_right_pw.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public void onPageSelected(int position) {
        try {
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }

            dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
            if (position + 1 == dotsCount) {

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public int getItemPlusPro(int i) {
        return vpProducts.getCurrentItem() + i;
    }

    public int getItemMinusPro(int i) {
        return vpProducts.getCurrentItem() - i;
    }

    public int getItemPlusPW(int i) {
        return vpPreviousWork.getCurrentItem() + i;
    }

    public int getItemMinusPW(int i) {
        return vpPreviousWork.getCurrentItem() - i;
    }

    public void dialogPersonalProfile() {
        paramsUpdate = new HashMap<>();

        dialogEditPersonal = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditPersonal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditPersonal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditPersonal.setContentView(R.layout.dailog_ar_personal_info);

        etNameD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etNameD);
        etBioD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etBioD);
        etCategoryD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etCategoryD);
        etLocationD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etLocationD);
        etRateD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etRateD);
        etCityD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etCityD);
        etCountryD = (CustomEditText) dialogEditPersonal.findViewById(R.id.etCountryD);
        tvText = (CustomTextViewBold) dialogEditPersonal.findViewById(R.id.tvText);

        etCategoryD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogCate.showSpinerDialog();
            }
        });

        for (int j = 0; j < categoryDTOS.size(); j++) {
            if (categoryDTOS.get(j).getId().equalsIgnoreCase(artistDetailsDTO.getCategory_id())) {
                categoryDTOS.get(j).setSelected(true);
                etCategoryD.setText(categoryDTOS.get(j).getCat_name());
                tvText.setText(getResources().getString(R.string.commis_msg)+categoryDTOS.get(j).getCurrency_type() + categoryDTOS.get(j).getPrice());


            }
        }

        spinnerDialogCate = new SpinnerDialog((Activity) getActivity(), categoryDTOS, getResources().getString(R.string.select_cate));// With 	Animation
        spinnerDialogCate.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, String id, int position) {
                etCategoryD.setText(item);
                paramsUpdate.put(Consts.CATEGORY_ID, id);
                tvText.setText(getResources().getString(R.string.commis_msg)+categoryDTOS.get(position).getCurrency_type() + categoryDTOS.get(position).getPrice());


            }
        });
        etNameD.setText(artistDetailsDTO.getName());
        etBioD.setText(artistDetailsDTO.getBio());

        etLocationD.setText(artistDetailsDTO.getLocation());
        etRateD.setText(artistDetailsDTO.getPrice());
        etCategoryD.setText(artistDetailsDTO.getCategory_name());
        etCityD.setText(artistDetailsDTO.getCity());
        etCountryD.setText(artistDetailsDTO.getCountry());

        tvYes = (CustomTextViewBold) dialogEditPersonal.findViewById(R.id.tvYes);
        tvNo = (CustomTextViewBold) dialogEditPersonal.findViewById(R.id.tvNo);
        dialogEditPersonal.show();
        dialogEditPersonal.setCancelable(false);

        etLocationD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();

            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditPersonal.dismiss();

            }
        });
        tvYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        paramsUpdate.put(Consts.NAME, ProjectUtils.getEditTextValue(etNameD));
                        paramsUpdate.put(Consts.BIO, ProjectUtils.getEditTextValue(etBioD));
                        paramsUpdate.put(Consts.LOCATION, ProjectUtils.getEditTextValue(etLocationD));
                        paramsUpdate.put(Consts.PRICE, ProjectUtils.getEditTextValue(etRateD));
                        paramsUpdate.put(Consts.CITY, ProjectUtils.getEditTextValue(etCityD));
                        paramsUpdate.put(Consts.COUNTRY, ProjectUtils.getEditTextValue(etCountryD));
                        paramsUpdate.put(Consts.LATITUDE, String.valueOf(lats));
                        paramsUpdate.put(Consts.LONGITUDE, String.valueOf(longs));
                        submitPersonalProfile();

                    }
                });

    }

    public void submitPersonalProfile() {
        if (!validation(etCategoryD, getResources().getString(R.string.val_cat_sele))) {
            return;
        }else if (!validation(etNameD, getResources().getString(R.string.val_name))) {
            return;
        }else if (!validation(etBioD, getResources().getString(R.string.val_bio))) {
            return;
        }else if (!validation(etLocationD, getResources().getString(R.string.val_location))) {
            return;
        }else if (!validation(etRateD, getResources().getString(R.string.val_rate))) {
            return;
        }
        else{
            if (NetworkManager.isConnectToInternet(getActivity())) {
                updateProfile();
            } else {
                ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
            }
        }
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(getActivity(), msg);
            return false;
        } else {
            return true;
        }
    }

    public void dialogAbout() {
        paramsUpdate = new HashMap<>();

        dialogEditAbout = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditAbout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditAbout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditAbout.setContentView(R.layout.dailog_ar_about);

        etAboutD = (CustomEditText) dialogEditAbout.findViewById(R.id.etAboutD);
        tvYesAbout = (CustomTextViewBold) dialogEditAbout.findViewById(R.id.tvYesAbout);
        tvNoAbout = (CustomTextViewBold) dialogEditAbout.findViewById(R.id.tvNoAbout);

        etAboutD.setText(artistDetailsDTO.getAbout_us());

        dialogEditAbout.show();
        dialogEditAbout.setCancelable(false);

        tvNoAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditAbout.dismiss();

            }
        });
        tvYesAbout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        paramsUpdate.put(Consts.ABOUT_US, ProjectUtils.getEditTextValue(etAboutD));

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            updateProfile();
                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }

    public void dialogQualification() {
        paramsUpdate = new HashMap<>();

        dialogEditQualification = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditQualification.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditQualification.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditQualification.setContentView(R.layout.dailog_ar_qualification);

        etQaulTitleD = (CustomEditText) dialogEditQualification.findViewById(R.id.etQaulTitleD);
        etQaulDesD = (CustomEditText) dialogEditQualification.findViewById(R.id.etQaulDesD);
        tvYesQuali = (CustomTextViewBold) dialogEditQualification.findViewById(R.id.tvYesQuali);
        tvNoQuali = (CustomTextViewBold) dialogEditQualification.findViewById(R.id.tvNoQuali);


        dialogEditQualification.show();
        dialogEditQualification.setCancelable(false);

        tvNoQuali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditQualification.dismiss();

            }
        });
        tvYesQuali.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        paramsUpdate.put(Consts.TITLE, ProjectUtils.getEditTextValue(etQaulTitleD));
                        paramsUpdate.put(Consts.DESCRIPTION, ProjectUtils.getEditTextValue(etQaulDesD));

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            addQualification();
                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }

    public void updateProfile() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.UPDATE_PROFILE_ARTIST_API, paramsUpdate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {

                    try {
                        ProjectUtils.showToast(getActivity(), msg);

                        artistDetailsDTO = new Gson().fromJson(response.getJSONObject("data").toString(), ArtistDetailsDTO.class);
                        //getArtist();
                        if (my_tag == 1) {
                            dialogEditPersonal.dismiss();
                        } else if (my_tag == 2) {
                            dialogEditAbout.dismiss();
                        } else if (my_tag == 3) {
                            dialogEditSkils.dismiss();
                        }

                        showData();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    public void addQualification() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_QUALIFICATION_API, paramsUpdate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    getArtist();
                    dialogEditQualification.dismiss();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    public void addProduct() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_PRODUCT_API, paramsUpdate, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    getArtist();
                    dialogEditProduct.dismiss();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


    public void getCategory() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_CATEGORY_API, parmsCategory, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        getArtist();
                        categoryDTOS = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<CategoryDTO>>() {
                        }.getType();
                        categoryDTOS = (ArrayList<CategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }

    public void getSkills() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_SKILLS_BY_CAT_API, parmsSkills, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        Type getpetDTO = new TypeToken<List<SkillsDTO>>() {
                        }.getType();
                        skillsDTOListAdd = (ArrayList<SkillsDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);


                        for (int j = 0; j < skillsDTOListAdd.size(); j++) {
                            for (int i = 0; i < artistDetailsDTO.getSkills().size(); i++) {
                                if (skillsDTOListAdd.get(j).getSkill().equalsIgnoreCase(artistDetailsDTO.getSkills().get(i).getSkill())) {
                                    skillsDTOListAdd.get(j).setSelected(true);
                                }
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }
            }
        });
    }


    public void dialogSkills() {
        paramsUpdate = new HashMap<>();

        dialogEditSkils = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditSkils.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditSkils.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditSkils.setContentView(R.layout.dailog_ar_skills);

        CustomTextViewBold tvCancle = (CustomTextViewBold) dialogEditSkils.findViewById(R.id.tvCancle);
        CustomTextViewBold tvOK = (CustomTextViewBold) dialogEditSkils.findViewById(R.id.tvOK);
        ListView listView = (ListView) dialogEditSkils.findViewById(R.id.list);

        final EditText searchBox = (EditText) dialogEditSkils.findViewById(R.id.searchBox);
        myAdapterCheck = new MyAdapterCheck(getActivity(), skillsDTOListAdd);
        listView.setAdapter(myAdapterCheck);

        searchBox.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                myAdapterCheck.getFilter().filter(searchBox.getText().toString());
            }
        });
        tvCancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogEditSkils.dismiss();
            }
        });

        tvOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StringBuilder allLabels = new StringBuilder();
                StringBuilder allID = new StringBuilder();
                for (SkillsDTO s : myAdapterCheck.arrayList) {
                    if (s.isSelected()) {
                        if (allLabels.length() > 0) {
                            allLabels.append(", "); // some divider between the different texts
                            allID.append(", "); // some divider between the different texts
                        }
                        allLabels.append(s);
                        allID.append(s.getId());

                    }

                }
                paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                paramsUpdate.put(Consts.SKILLS, "[" + allID.toString() + "]");
                if (NetworkManager.isConnectToInternet(getActivity())) {
                    updateProfile();
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                //   SpinnerDialog.this.onSpinerItemClick.onClick(allLabels.toString(), allID.toString(), clickpos);

                //SpinnerDialog.this.alertDialog.dismiss();
            }
        });


        dialogEditSkils.show();
        dialogEditSkils.setCancelable(false);
    }


    public class MyAdapterCheck extends BaseAdapter implements Filterable {

        ArrayList<SkillsDTO> arrayList;
        ArrayList<SkillsDTO> mOriginalValues; // Original Values
        LayoutInflater inflater;

        public MyAdapterCheck(Context context, ArrayList<SkillsDTO> arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            CustomTextView text1;
            CheckBox checkBox1;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyAdapterCheck.ViewHolder holder;

            if (convertView == null) {
                holder = new MyAdapterCheck.ViewHolder();
                convertView = inflater.inflate(R.layout.spinner_view_checkbox, parent, false);
                holder.text1 = (CustomTextView) convertView.findViewById(R.id.text1);
                holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);
            } else {
                holder = (MyAdapterCheck.ViewHolder) convertView.getTag();
            }


            holder.text1.setText(arrayList.get(position).getSkill());
            holder.text1.setTypeface(null, Typeface.NORMAL);
            holder.checkBox1.setChecked(arrayList.get(position).isSelected());

            convertView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (arrayList.get(position).isSelected()) { // deselect
                        selected--;
                    } else if (selected == limit) { // select with limit
                        if (limitListener != null)
                            limitListener.onLimitListener(arrayList.get(position));
                        return;
                    } else { // selected
                        selected++;
                    }

                    final MyAdapterCheck.ViewHolder temp = (MyAdapterCheck.ViewHolder) v.getTag();
                    temp.checkBox1.setChecked(!temp.checkBox1.isChecked());

                    arrayList.get(position).setSelected(!arrayList.get(position).isSelected());
                    Log.i("TAG", "On Click Selected Item : " + arrayList.get(position).getSkill() + " : " + arrayList.get(position).isSelected());
                    notifyDataSetChanged();
                }
            });
            holder.checkBox1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (arrayList.get(position).isSelected()) { // deselect
                        selected--;
                    } else if (selected == limit) { // select with limit
                        if (limitListener != null)
                            limitListener.onLimitListener(arrayList.get(position));
                        return;
                    } else { // selected
                        selected++;
                    }

                    final MyAdapterCheck.ViewHolder temp = (MyAdapterCheck.ViewHolder) v.getTag();
                    temp.checkBox1.setChecked(!temp.checkBox1.isChecked());

                    arrayList.get(position).setSelected(!arrayList.get(position).isSelected());
                    Log.i("TAG", "On Click Selected Item : " + arrayList.get(position).getSkill() + " : " + arrayList.get(position).isSelected());
                    notifyDataSetChanged();
                }
            });


            if (arrayList.get(position).isSelected()) {
//                holder.text1.setTypeface(null, Typeface.BOLD);
                // convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.list_selected));
            }
            holder.checkBox1.setTag(holder);

            return convertView;
        }

        @SuppressLint("DefaultLocale")
        @Override
        public Filter getFilter() {
            return new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    arrayList = (ArrayList<SkillsDTO>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<SkillsDTO> FilteredArrList = new ArrayList<>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
                    }

                    if (constraint == null || constraint.length() == 0) {
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getSkill();
                            if (data.toLowerCase().contains(constraint.toString())) {
                                FilteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
        }
    }

    public interface LimitExceedListener {
        void onLimitListener(SkillsDTO data);
    }


    public void dialogProduct() {
        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        dialogEditProduct = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditProduct.setContentView(R.layout.dailog_ar_product);

        etImageD = (CustomEditText) dialogEditProduct.findViewById(R.id.etImageD);
        etProNameD = (CustomEditText) dialogEditProduct.findViewById(R.id.etProNameD);
        etRateProD = (CustomEditText) dialogEditProduct.findViewById(R.id.etRateProD);
        tvYesPro = (CustomTextViewBold) dialogEditProduct.findViewById(R.id.tvYesPro);
        tvNoPro = (CustomTextViewBold) dialogEditProduct.findViewById(R.id.tvNoPro);


        dialogEditProduct.show();
        dialogEditProduct.setCancelable(false);


        etImageD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        tvNoPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditProduct.dismiss();

            }
        });
        tvYesPro.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        paramsUpdate.put(Consts.PRODUCT_NAME, ProjectUtils.getEditTextValue(etProNameD));
                        paramsUpdate.put(Consts.PRICE, ProjectUtils.getEditTextValue(etRateProD));
                        paramsFile.put(Consts.PRODUCT_IMAGE, file);

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            addProduct();
                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }


    private void setPageViewIndicator(final GalleryPagerAdapter galleryPagerAdapter, final AutoScrollViewPager viewEvents, final LinearLayout viewDots) {
        viewDots.removeAllViews();
        try {

            Log.d("###setPageViewIndicator", " : called");
            dotsCount = galleryPagerAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getActivity());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        20,
                        20
                );

                params.setMargins(4, 0, 4, 0);

                final int presentPosition = i;
                dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        viewEvents.setCurrentItem(presentPosition);
                        return true;
                    }

                });


                viewDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addGallery() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ADD_GALLERY_API, paramsUpdate, paramsFile, getActivity()).imagePost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    getArtist();
                    dialogEditGallery.dismiss();
                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }


    public void dialogGallery() {
        paramsUpdate = new HashMap<>();
        paramsFile = new HashMap<>();
        dialogEditGallery = new Dialog(getActivity()/*, android.R.style.Theme_Dialog*/);
        dialogEditGallery.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEditGallery.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEditGallery.setContentView(R.layout.dailog_ar_gallry);

        etImageGallD = (CustomEditText) dialogEditGallery.findViewById(R.id.etImageGallD);
        tvYesGall = (CustomTextViewBold) dialogEditGallery.findViewById(R.id.tvYesGall);
        tvNoGall = (CustomTextViewBold) dialogEditGallery.findViewById(R.id.tvNoGall);


        dialogEditGallery.show();
        dialogEditGallery.setCancelable(false);


        etImageGallD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
        tvNoGall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEditGallery.dismiss();

            }
        });
        tvYesGall.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsUpdate.put(Consts.USER_ID, userDTO.getUser_id());
                        paramsFile.put(Consts.IMAGE, file);

                        if (NetworkManager.isConnectToInternet(getActivity())) {
                            addGallery();
                        } else {
                            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                        }
                    }
                });

    }

    public void isOnline() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.ONLINE_OFFLINE_API, paramsUpdate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    ProjectUtils.showToast(getActivity(), msg);
                    getArtist();

                } else {
                    ProjectUtils.showToast(getActivity(), msg);
                }


            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }





    public void chnageRate() {
        new HttpsRequest(Consts.CHANGE_COMMISSION_ARTIST_API, paramsRate, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    ProjectUtils.showLong(getActivity(), msg);
                    getArtist();
                } else {
                    ProjectUtils.showLong(getActivity(), msg);
                }
            }
        });
    }

    public void findPlace() {
        try {
          /*  Intent intent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);
            startActivityForResult(intent, 101);*/

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            startActivityForResult(builder.build(getActivity()), 101);
        } catch (Exception e) {
            // TODO: Handle the error.
        }
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);

            etLocationD.setText(obj.getAddressLine(0));

            lats = lat;
            longs = lng;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


package com.samyotech.fabartist.ui.adapter;

/**
 * Created by pushpraj on 20/2/18.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samyotech.fabartist.DTO.ProductDTO;
import com.samyotech.fabartist.interfacess.Consts;
import com.samyotech.fabartist.preferences.SharedPrefrence;
import com.samyotech.fabartist.utils.CustomTextView;
import com.samyotech.fabartist.R;

import java.util.ArrayList;


public class ProductPagerAdapter extends PagerAdapter {
    LayoutInflater mLayoutInflater;
    Context context;
    private ArrayList<ProductDTO> productDTOList;
    private SharedPrefrence prefrence;
    public ProductPagerAdapter(Context context, ArrayList<ProductDTO> productDTOList) {
        this.context = context;
        this.productDTOList = productDTOList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        prefrence = SharedPrefrence.getInstance(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.productpageradapter, container, false);

        ImageView IVproduct = (ImageView) itemView.findViewById(R.id.IVproduct);
        CustomTextView CTVproductname = itemView.findViewById(R.id.CTVproductname);
        LinearLayout LLBuynow = itemView.findViewById(R.id.LLBuynow);
        CustomTextView CTVproductprice = itemView.findViewById(R.id.CTVproductprice);

        CTVproductname.setText(productDTOList.get(position).getProduct_name());
        CTVproductprice.setText(productDTOList.get(position).getCurrency_type()+productDTOList.get(position).getPrice());
        Glide.with(context).
                load(productDTOList.get(position).getProduct_image())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(IVproduct);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return productDTOList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

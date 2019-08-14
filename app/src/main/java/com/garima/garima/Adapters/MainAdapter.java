package com.garima.garima.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.garima.garima.Model.main;
import com.garima.garima.R;
import com.garima.garima.helper.LruBitmapCache;
import com.garima.garima.helper.PrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

import static android.view.animation.AnimationUtils.loadAnimation;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder>  {

    private  ArrayList<main> CanteenArray;
    private Context mContext;
    private ArrayList<main> albumList;
    public  String Op_time,Cl_time;
    private String Mn_order;
    private PrefManager pref;
    private RelativeLayout layoutBottomSheet_e,layoutBottomSheet_r;
    private LinearLayout layoutBottomSheet_i;
    private TextView _mValue,_iValue;
    private int itemSelected=0;
    private double _orderValue=0;
    private  DecimalFormat df = new DecimalFormat("0.00");
    private ArrayList<String>_main=new ArrayList<String>();
    private ArrayList<String>noOfitems=new ArrayList<String>();
    private ArrayList<String>Shared;
    private int _ID=0;
    private String _phoneNo;
    private RecyclerView _reviewRV;
    private Button _checkout;
    private boolean _searched=false;
    private LinearLayout L1;
    private ImageLoader imageLoader;
    private boolean _filter=false;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private RecyclerView recyclerView;
    private boolean isLoading=false;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private View itemView;
    private boolean _exit=false;
    private RecyclerView _moreRV;

    public void setPref(PrefManager pref1) {
        pref=pref1;
    }

    public void setLinearLayout_item(LinearLayout layoutBottomSheet) {
        layoutBottomSheet_i=layoutBottomSheet;
    }
    public void setButton(Button checkout) {
        _checkout=checkout;
    }
    public void setValues(TextView moneyValue, TextView itemValue) {
        _mValue=moneyValue;
        _iValue=itemValue;
    }


    public void setPhone(String phoneNo) {
        _phoneNo=phoneNo;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView Thumbnail;
        private TextView Count,Name;
        private RadioButton button2_plus;
        private TextView Price,Discount,DiscountP;
        private RelativeLayout Rl_below;
        private LinearLayout rrr;
        private ImageButton _minus,_add;
        private EditText _noItem;
        private CardView _card_view;

        public MyViewHolder(View view) {
            super(view);
            _card_view=itemView.findViewById(R.id.card_view);
            Thumbnail = itemView.findViewById(R.id.service_pic);
            button2_plus = itemView.findViewById(R.id.button2_plus);
            Discount = itemView.findViewById(R.id.discount_);
            DiscountP = itemView.findViewById(R.id.discountprice_);
            Name = itemView.findViewById(R.id._name);
            Price= itemView.findViewById(R.id.price_);
            rrr=view.findViewById(R.id._rrr);
            _minus=view.findViewById(R.id.button2_minus);
            _add=view.findViewById(R.id.button2_add);
            _noItem=view.findViewById(R.id.rate_km);
            Rl_below=view.findViewById(R.id.rl4);

        }
    }


    public MainAdapter(Context mContext, ArrayList<main> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;


    }

    @Override
    public int getItemViewType(int position) {
        return albumList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           if (viewType == VIEW_TYPE_ITEM) {
                itemView = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.horizontal_recyle, parent, false);
               return new MyViewHolder(itemView);
           } else if (viewType == VIEW_TYPE_LOADING) {
                itemView = LayoutInflater.from(parent.getContext())
                       .inflate(R.layout.item_loading, parent, false);

           }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final main album = albumList.get(position);

        holder.Name.setText(album.getName(position));
         holder.Rl_below.setEnabled(false);
     //   holder.Thumbnail.setDefaultImageResId(R.drawable.defaulter);
        if(!_filter) {
            String url = album.getPhoto(position).replaceAll(" ", "%20");
            imageLoader = LruBitmapCache.getInstance(mContext)
                    .getImageLoader();
            imageLoader.get(url, ImageLoader.getImageListener(holder.Thumbnail,
                    R.mipmap.ic_launcher, R.mipmap
                            .ic_launcher));
            holder.Thumbnail.setImageUrl(url, imageLoader);
        }else {
            holder.Thumbnail.setDefaultImageResId(R.mipmap.ic_launcher);
        }

        if(pref.get_ride()!=0){
            holder.button2_plus.setVisibility(View.GONE);
        }else{
            holder.button2_plus.setVisibility(View.VISIBLE);
            if(pref.get_packagesharedPreferences()!=null) {
                Set<String> set = pref.get_packagesharedPreferences();
                _main.clear();
                _main.addAll(set);
                for (String s : set) {
                    String[] pars = s.split("\\_");
                    if (Double.parseDouble(pars[0]) == albumList.get(position).getID(position)) {
                        holder.button2_plus.setAnimation(AnimationUtils.loadAnimation(mContext,
                                R.anim.fade_out));
                        holder.button2_plus.setVisibility(View.GONE);
                        holder.rrr.setAnimation(AnimationUtils.loadAnimation(mContext,
                                R.anim.slide_up1));
                        holder.rrr.setVisibility(View.VISIBLE);
                        holder._noItem.setText(pars[1]);
                        break;
                    }
                }
            }

            if(pref.get_food_items()!=0){
                _iValue.setText(String.valueOf(pref.get_food_items()));
                _orderValue=pref.get_food_money();
                pref.set_food_money((float) _orderValue);
                _mValue.setText("\u20B9"+df.format(pref.get_food_money()));
                if(pref.get_food_items()!=0) {
                    layoutBottomSheet_i.setVisibility(View.VISIBLE);

                }
            }else{
                pref.set_food_money(0);
            }
        }



        if( albumList.get(position).getDiscount(position)!=0){
            holder.Discount.setText("\u20B9"+df.format(album.getDiscount(position))+" off");
            double dis=album.getFinal_Price(position);
            holder.DiscountP.setText("\u20B9"+ dis);
        }else{
            holder.Discount.setVisibility(View.GONE);

        }

        if( album.getPrice(position)!=0){
            holder.Price.setText("\u20B9" + df.format(album.getPrice(position)));

        }else{
            holder.Price.setVisibility(View.GONE);

        }





       holder.button2_plus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked ) {

                   holder.button2_plus.setChecked(true);
                   if (pref.get_packagesharedPreferences() != null) {
                       Set<String> set = pref.get_packagesharedPreferences();
                       _main.clear();
                       _main.addAll(set);
                   }
                   _main.add(albumList.get(position).getID(position) + "_" + 1 + "_" +
                           albumList.get(position).getPrice(position) +
                           "_" + albumList.get(position).getName(position) +
                           "_" + position);
                   pref.packagesharedPreferences(_main);
                   pref.setnoOfItems(_main);
                   setPrice(albumList.get(position).getPrice(position));
                   holder.button2_plus.setAnimation(AnimationUtils.loadAnimation(mContext,
                           R.anim.fade_out));
                   holder.button2_plus.setVisibility(View.GONE);
                   holder.rrr.setAnimation(AnimationUtils.loadAnimation(mContext,
                           R.anim.slide_up1));
                   holder.rrr.setVisibility(View.VISIBLE);
                   holder._noItem.setText("1");

               }
           }
       });


        holder._add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int Rate_=Integer.parseInt(holder._noItem.getText().toString());
                if (Rate_ >= 0 ) {
                    Rate_ = 1 + Rate_;
                    _orderValue=pref.get_food_money()+albumList.get(position).getPrice(position);
                    pref.set_food_money((float) _orderValue);
                    _mValue.setText("\u20B9"+df.format(pref.get_food_money()));
                    holder._noItem.setText(String.valueOf(Rate_));
                    for(int i=0;i<_main.size();i++){
                        String[] pars = _main.get(i).split("\\_");
                        if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                            String s = pars[0];
                            _main.remove(i);
                            _main.add(s+"_"+ Rate_ +"_"+ Rate_ * albumList.get(position).getPrice(position) +"_"+albumList.get(position).getName(position)+"_"+ position);
                        }

                    }
                    pref.packagesharedPreferences(_main);
                }


            }
        });
        holder._minus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int Rate_=Integer.parseInt(holder._noItem.getText().toString());
                if (Rate_ > 0 ) {
                    Rate_ = Rate_ - 1;
                    holder._noItem.setText(String.valueOf(Rate_));
                    _orderValue=pref.get_food_money()-albumList.get(position).getPrice(position);
                    pref.set_food_money((float) _orderValue);
                    _mValue.setText("\u20B9"+df.format(pref.get_food_money()));
                     for(int i=0;i<_main.size();i++){
                        String[] pars = _main.get(i).split("\\_");
                        if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                            String s = pars[0];
                            _main.remove(i);
                            _main.add(s+"_"+ Rate_ +"_"+ Rate_ * albumList.get(position).getPrice(position) +"_"+albumList.get(position).getName(position)+"_"+ position);

                        }

                    }
                    pref.packagesharedPreferences(_main);
                }

                if(Rate_==0){

                    holder.rrr.setVisibility(View.GONE);
                    holder.button2_plus.setAnimation(AnimationUtils.loadAnimation(mContext,
                            R.anim.slide_up1));
                    holder.button2_plus.setVisibility(View.VISIBLE);
                    holder.button2_plus.setChecked(false);
                    holder.button2_plus.setEnabled(true);
                    for(int i=0;i<_main.size();i++){
                        String[] pars = _main.get(i).split("\\_");
                        if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                            _main.remove(i);
                        }

                    }
                    pref.packagesharedPreferences(_main);

                }
            }
        });

        holder._noItem.addTextChangedListener(new TextWatcher() {

                                                  @Override
                                                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                  }

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
                                                  public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                int after) {
                TranslateAnimation animObj= new TranslateAnimation(0,0, 0, holder._noItem.getHeight());
                animObj.setDuration(1000);
                holder._noItem.startAnimation(animObj);

            }
                                              });



    }

    private void setNon(double v,String s) {
        itemSelected=pref.get_food_items();
        itemSelected=itemSelected-1;
        pref.set_food_items(itemSelected);
        _iValue.setText(String.valueOf(pref.get_food_items()));
        _orderValue=pref.get_food_money()-v;
        pref.set_food_money((float) _orderValue);
        _mValue.setText("\u20B9"+df.format(pref.get_food_money()));
        if(pref.get_food_items()!=0) {
            layoutBottomSheet_i.setVisibility(View.VISIBLE);

        }
    }

    private void setPrice(double v) {
        itemSelected=pref.get_food_items();
        itemSelected=itemSelected+1;
        pref.set_food_items(itemSelected);
        _iValue.setText(String.valueOf(pref.get_food_items()));
        _orderValue=pref.get_food_money()+v;
        pref.set_food_money((float) _orderValue);
        _mValue.setText("\u20B9"+df.format(pref.get_food_money()));
        if(pref.get_food_items()!=0){

                layoutBottomSheet_i.setAnimation(loadAnimation(mContext,
                        R.anim.fade_in));
                layoutBottomSheet_i.setVisibility(View.VISIBLE);

        }

    }

    private void showPopupMenu(View v, int id) {
        _ID=id;
        PopupMenu popup = new PopupMenu(mContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_food_item, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.write_review:
                        open_review();
                    return true;
                case R.id.Show_Reviews:
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }


    private void open_review() {
        if(!((Activity)mContext).isFinishing()) {
            final Dialog dialog1 = new Dialog(mContext, R.style.AlertDialogTheme);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setCancelable(true);
            dialog1.setContentView(R.layout.review_layout);

            ImageView btn_positive = dialog1.findViewById(R.id._i1);
            final RatingBar ratingBarbill = dialog1.findViewById(R.id.ratingBarbill);

            final EditText title = dialog1.findViewById(R.id.title);

            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(title.getText().toString())){
                        if(ratingBarbill.getRating()==0){
                            Toast.makeText(mContext,"Please rate the food",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext,"Test Only",Toast.LENGTH_SHORT).show();
                            //new PostReview().execute(title.getText().toString(),String.valueOf(ratingBarbill.getRating()));
                        }
                    }else{
                        title.setError("Empty!Add review");
                    }

                    dialog1.cancel();
                }
            });

            dialog1.show();
        }
    }



}



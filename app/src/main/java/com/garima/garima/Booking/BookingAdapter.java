package com.garima.garima.Booking;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.garima.garima.Activities.Mainactivity;
import com.garima.garima.Model.main;
import com.garima.garima.R;
import com.garima.garima.helper.PrefManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<main> albumList;
    public  String Op_time,Cl_time;
    private String Mn_order;
    private PrefManager pref;
    private DecimalFormat df = new DecimalFormat("0.00");
    private int _from=0;
    private double _orderValue=0;
    private ArrayList<String>_main=new ArrayList<String>();
    private ArrayList<String>noOfitems=new ArrayList<String>();

    public void setPref(PrefManager pref1) {
        pref=pref1;
    }

    public void setFrom(int j) {
        _from=j;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView _name,_price,_slNo,_noItem;
        public ImageButton _minus,_add, _delete;



        public MyViewHolder(View view) {
            super(view);
            _name = view.findViewById(R.id._Name);
            _price=view.findViewById(R.id.price_);
            _slNo =  view.findViewById(R.id._slNo);
            _noItem = view.findViewById(R.id.rate_km);
            _minus=view.findViewById(R.id.button2_minus);
            _add =  view.findViewById(R.id.button2_add);
            _delete = view.findViewById(R.id.button2_plus);
        }
    }


    public BookingAdapter(Context mContext, ArrayList<main> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookingadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final main album = albumList.get(position);
        holder._name.setText(album.getName(position));
        holder._price.setText("\u20B9"+df.format(album.getFinal_Price(position)));
        holder._noItem.setText(String.valueOf(album.getNoofItems(position)));
        holder._slNo.setText(String.valueOf(position+1));
        if(_from!=0){
            holder._delete.setEnabled(false);
            holder._delete.setVisibility(View.GONE);
        }


        if(pref.get_packagesharedPreferences()!=null) {
            Set<String> set = pref.get_packagesharedPreferences();
            _main.clear();
            _main.addAll(set);
        }
        holder._delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<String>_main=new ArrayList<String>();
                Set<String> set = pref.get_packagesharedPreferences();
                _main.addAll(set);
                for(int i=0;i<_main.size();i++){
                    String[] pars = _main.get(i).split("\\_");
                    if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                        _main.remove(i);
                        pref.set_food_items(pref.get_food_items()-1);
                        double _orderValue = pref.get_food_money() - Double.parseDouble(pars[1]) * albumList.get(position).getPrice(position);
                        pref.set_food_money((float) _orderValue); break;
                    }

                }
                pref.packagesharedPreferences(_main);
                removeAt(position);
                if(pref.get_food_items()!=0) {
                    ((Activity) mContext).recreate();
                }else{
                    if (!((Activity)mContext).isFinishing()) {
                        new AlertDialog.Builder(mContext, R.style.AlertDialogTheme)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle("Are you sure?")
                                .setMessage("Your order with "+pref.getCanteen()+" about to empty ")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        pref.set_food_items(0);
                                        pref.set_food_money(0);
                                        pref.packagesharedPreferences(null);
                                        Intent o = new Intent(mContext, Mainactivity.class);
                                        mContext.startActivity(o);
                                        ((Activity) mContext).finish();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                    }
                }



            }
        });

        holder._add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int Rate_=Integer.parseInt(holder._noItem.getText().toString());
                double single=albumList.get(position).getPrice(position)/Rate_;
                if (Rate_ >= 0 ) {
                    Rate_ = 1 + Rate_;
                    _orderValue=pref.get_food_money()+single;
                    pref.set_food_money((float) _orderValue);
                    holder._noItem.setText(String.valueOf(Rate_));
                    holder._price.setText("\u20B9"+df.format(Rate_*single));
                    for(int i=0;i<_main.size();i++){
                        String[] pars = _main.get(i).split("\\_");
                        if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                            String s = pars[0];
                            _main.remove(i);
                             _main.add(s+"_"+ Rate_ +"_"+ Rate_ * single +"_"+albumList.get(position).getName(position)+"_"+ position);
                        }

                    }
                    pref.packagesharedPreferences(_main);
                    ((Activity) mContext).recreate();
                }


            }
        });

        holder._minus.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                int Rate_=Integer.parseInt(holder._noItem.getText().toString());
                double single=albumList.get(position).getPrice(position)/Rate_;
                if (Rate_ > 1 ) {
                    Rate_ = Rate_ - 1;
                    holder._noItem.setText(String.valueOf(Rate_));
                    _orderValue=pref.get_food_money()-single;
                    pref.set_food_money((float) _orderValue);

                    for(int i=0;i<_main.size();i++){
                        String[] pars = _main.get(i).split("\\_");
                        if(albumList.get(position).getID(position)==Integer.parseInt(pars[0])) {
                            String s = pars[0];
                            _main.remove(i);
                            holder._price.setText("\u20B9"+df.format(albumList.get(position).getPrice(position)-single));
                            _main.add(s+"_"+ Rate_ +"_"+ Rate_ * single +"_"+albumList.get(position).getName(position)+"_"+ position);

                        }

                    }
                    pref.packagesharedPreferences(_main);
                    ((Activity) mContext).recreate();
                }


            }
        });

    }

    private void removeAt(int p1) {

        if(albumList!=null && albumList.size()!=0 && albumList.get(p1)!=null ) {
            albumList.remove(p1);
            notifyItemRemoved(p1);
            notifyItemRangeChanged(p1, albumList.size());
        }

    }
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
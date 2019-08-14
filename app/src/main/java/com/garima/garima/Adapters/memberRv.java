package com.garima.garima.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.garima.garima.Model.Members;
import com.garima.garima.R;
import com.garima.garima.helper.LruBitmapCache;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class memberRv extends RecyclerView.Adapter<memberRv.ViewHolder> {

    private ArrayList<Members> mItems;
    private Context mContext;
    private ImageLoader imageLoader;
    private int _from=0;


    public memberRv(Context aContext, ArrayList<Members> mItems) {
        this.mItems = mItems;
        this.mContext = aContext;

    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //More to come
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.member_rv, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Members album_pos = mItems.get(position);

        if(album_pos.getName(position)==null && !TextUtils.isEmpty(album_pos.getName(position))){
            viewHolder.Name.setText(album_pos.getName(position));
        }

        if (album_pos.getAddress(position) != null && !TextUtils.isEmpty(album_pos.getAddress(position))) {
            viewHolder.Address.setText(album_pos.getAddress(position));
        }
        String url = album_pos.getPhoto(position).replaceAll(" ", "%20");
        imageLoader = LruBitmapCache.getInstance(mContext)
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(viewHolder.Thumbnail,
                R.mipmap.ic_launcher, R.mipmap
                        .ic_launcher));
        viewHolder.Thumbnail.setImageUrl(url, imageLoader);


        if(_from==2){
            viewHolder._delete.setVisibility(View.VISIBLE);
            viewHolder._info.setVisibility(View.GONE);

        }else if(_from==1){
            viewHolder._delete.setVisibility(View.GONE);
            viewHolder._info.setVisibility(View.VISIBLE);

        }
    }




    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setFrom(int i) {
        _from=i;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView Name,Address;
        private NetworkImageView Thumbnail;
        private ImageView _info,_delete;


        public ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id._name);
            Address = itemView.findViewById(R.id._address);
            Thumbnail = itemView.findViewById(R.id.profile_picture);
            _info=itemView.findViewById(R.id._image_1);
            _delete=itemView.findViewById(R.id._image_2);
        }

    }


}







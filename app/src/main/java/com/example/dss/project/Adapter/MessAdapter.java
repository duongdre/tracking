package com.example.dss.project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.example.dss.R;
import com.example.dss.project.Models.Mess;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MessAdapter extends RecyclerView.Adapter<MessAdapter.ViewHolder> {

    public static final int MSG_TYPE_SENDING = 1;
    public static final int MSG_TYPE_IMAGE = 2;
    private Context mContext;
    private List<Mess> mMess;
    private String url;

    FirebaseUser firebaseUser;

    public MessAdapter(Context mContext, List<Mess> mMess, String url){
        this.mContext = mContext;
        this.mMess = mMess;
        this.url = url;
    }

    @NonNull
    @Override
    public MessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_SENDING){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_sending, parent, false);
            return new MessAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_receiving, parent, false);
            return new MessAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessAdapter.ViewHolder holder, int position) {
        final Mess mess = mMess.get(position);

        if (mess.getType().equals("Text")){
            holder.show_mess.setText(mess.getValue());
            holder.show_image.setVisibility(View.GONE);
        }else if (mess.getType().equals("Image")){
            holder.show_mess.setVisibility(View.GONE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference httpsReference = storage.getReferenceFromUrl(mess.getValue());
            final long ONE_MEGABYTE = 2048 * 2048;
            httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    bmp = Bitmap.createScaledBitmap(bmp, 160, 160, true);
                    holder.show_image.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(mContext, "Không thể tải ảnh", Toast.LENGTH_LONG).show();
                }
            });
            //Toast.makeText(mContext, "ImageURL: " + mess.getValue(), Toast.LENGTH_SHORT).show();
        }

        if (url.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ww);
        }else{
            holder.profile_image.setImageResource(R.mipmap.ww);
//            Glide.with(mContext).load(url).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mMess.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_mess;
        public ImageView profile_image, show_image;

        public ViewHolder(View itemView){
            super(itemView);

            show_mess = itemView.findViewById(R.id.show_mess);
            profile_image = itemView.findViewById(R.id.profile_image);
            show_image = itemView.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mMess.get(position).getFromWho().equals("Device")){
            return MSG_TYPE_SENDING;
        }else{
            return MSG_TYPE_IMAGE;
        }
    }
}

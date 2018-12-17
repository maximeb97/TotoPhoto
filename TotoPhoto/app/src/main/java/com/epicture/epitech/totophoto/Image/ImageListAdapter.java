package com.totophoto.Image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.totophoto.Activity.Preview.PreviewActivity;
import com.totophoto.R;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Image> imageUrls;

    /**
     *
     * @param context The context
     * @param imageUrls The list of the url for your images
     */
    public ImageListAdapter(Context context, ArrayList<Image> imageUrls) {
        super(context, R.layout.grid_view_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View currentView;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_view_image, parent, false);
        }
        currentView = convertView;
        currentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                Glide.clear((ImageView)view.findViewById(R.id.imageInGrid));
            }
        });

        ((ImageView)currentView.findViewById(R.id.imageInGrid)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PreviewActivity.class);
                Bundle b = new Bundle();
                b.putString("link", ((TextView)currentView.findViewById(R.id.textItemUrl)).getText().toString());
                b.putString("title", ((TextView)currentView.findViewById(R.id.textItemTitle)).getText().toString());
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
        if (imageUrls.size() > 0) {
            ((TextView)currentView.findViewById(R.id.textItemUrl)).setText(imageUrls.get(position).getLink());
            Glide
                    .with(getContext())
                    .load(imageUrls.get(position).getLink())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            ((ProgressBar)currentView.findViewById(R.id.progressBar2)).setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            ((ProgressBar)currentView.findViewById(R.id.progressBar2)).setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .error(R.drawable.error_memes)
                    .into((ImageView)convertView.findViewById(R.id.imageInGrid));

            if (imageUrls.get(position).getName().equals("null") || imageUrls.get(position).getName() == null)
                ((TextView)currentView.findViewById(R.id.textItemTitle)).setVisibility(View.GONE);
            else
                ((TextView)currentView.findViewById(R.id.textItemTitle)).setText(imageUrls.get(position).getName());
        }
        return convertView;
    }

    /**
     * Set the Array to used
     * @param images The ArrayList of the images to display
     */
    public void setImages(ArrayList<Image> images) {
        imageUrls.clear();
        addImages(images);
    }

    /**
     * Add the images for display
     * @param images The ArrayList of the images to display
     */
    public void addImages(ArrayList<Image> images) {
        int i = 0;
        while (i < images.size()) {
            imageUrls.add(images.get(i));
            i++;
        }
        notifyDataSetChanged();
    }
}
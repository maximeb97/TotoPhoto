package com.totophoto.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.totophoto.DataBase.ManageDB;
import com.totophoto.R;

import java.util.Locale;


public class settingsFragment extends Fragment {
    private static final String TAG = "settingsFragment";
    private View view;
    private Context fContext;
    private ManageDB db = null;

    private ImageView en;
    private ImageView fr;
    private ImageView zh;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        fContext = view.getContext();
        db = new ManageDB(fContext);
        this.setImgListener();
        return view;
    }

    private void setImgListener() {
        en = (ImageView)view.findViewById(R.id.ENselect);
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLang("en");
            }
        });

        fr = (ImageView)view.findViewById(R.id.FRselect);
        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLang("fr");
            }
        });

        zh = (ImageView)view.findViewById(R.id.ZHselect);
        zh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLang("zh");
            }
        });
    }

    public void setLang(String lang) {
        db.setLang(lang += "-S");
        Activity activity = (Activity)fContext;
        Intent intent = activity.getIntent();

        activity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
    }
}

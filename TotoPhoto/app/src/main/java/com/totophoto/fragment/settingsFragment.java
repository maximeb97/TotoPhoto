package com.totophoto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.totophoto.DataBase.ManageDB;
import com.totophoto.MainActivity;
import com.totophoto.R;

public class settingsFragment extends Fragment {
    private static final String TAG = "settingsFragment";
    private View view;
    private Context fContext;
    private String lang;
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
        this.lang = lang;
    }
}

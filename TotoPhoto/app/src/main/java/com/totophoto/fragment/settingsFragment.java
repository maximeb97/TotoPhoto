package com.totophoto.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.totophoto.DataBase.ManageDB;
import com.totophoto.Models.Settings;
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
    private TextView txt;
    private Switch onoff;

    private Activity activity;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        fContext = view.getContext();
        db = new ManageDB(fContext);
        activity = (Activity)fContext;
        intent = activity.getIntent();
        txt = (TextView)view.findViewById(R.id.LanguageSelect);
        this.setImgListener();
        this.setSwitchListener();
        Settings sets = db.getSettings();
        this.setColor(sets.getMode());
        return view;
    }

    private void setColor(String mode) {
        if (mode != null) {
            if (mode.equals("night")) {
                txt.setTextColor(fContext.getResources().getColor(R.color.colorTextNight, null));
                onoff.setTextColor(fContext.getResources().getColor(R.color.colorTextNight, null));
            }
        }
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

    private void setSwitchListener() {
        onoff = (Switch)view.findViewById(R.id.NightSelect);
        String tmp = db.getSettings().getMode();
        if (tmp != null) {
            if (tmp.equals("night")) {
                onoff.setChecked(true);
            } else {
                onoff.setChecked(false);
            }
        } else {
            onoff.setChecked(false);
        }
        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.setMode("night-S");
                } else {
                    db.setMode("day-S");
                }
                activity.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                activity.startActivity(intent);
            }
        });
    }

    public void setLang(String lang) {
        db.setLang(lang += "-S");
        activity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.finish();
        activity.startActivity(intent);
    }
}

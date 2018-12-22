package com.totophoto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.totophoto.DataBase.ManageDB;
import com.totophoto.Image.Image;
import com.totophoto.Image.ImageListAdapter;
import com.totophoto.R;
import java.util.ArrayList;


public class favoritesFragment extends Fragment {
    private static final String TAG = "favoritesFragment";
    private View view;
    private Context fContext;
    public GridView fGrid;
    private ImageListAdapter fAdapter;
    private ArrayList<Image> listFavorites;
    private ManageDB db = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        fContext = view.getContext();
        db = new ManageDB(fContext);
        fGrid = view.findViewById(R.id.fav_list);
        listFavorites = db.getFavoriteList();
        fAdapter = new ImageListAdapter(fContext, new ArrayList<Image>());
        fAdapter.setImages(listFavorites);
        fGrid.setAdapter(fAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listFavorites = db.getFavoriteList();
        fAdapter.setImages(listFavorites);
    }
}
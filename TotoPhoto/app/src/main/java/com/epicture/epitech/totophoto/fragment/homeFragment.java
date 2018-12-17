package com.totophoto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.totophoto.Image.Image;
import com.totophoto.Image.ImageListAdapter;
import com.totophoto.R;
import com.totophoto.api.imgur.imgur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class homeFragment extends Fragment {
    private View view;
    public GridView gridViewGallery;
    private EditText textSearch;
    private Context mContext;
    private imgur api;
    private ImageListAdapter adapter;
    private String currentResearch = null;
    private int currentPage = 0;
    private int loadingPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = view.getContext();
        gridViewGallery = view.findViewById(R.id.gridViewGallery);
        textSearch = view.findViewById(R.id.textSearch);
        api = new imgur(getContext());

        adapter = new ImageListAdapter(mContext, new ArrayList<Image>());
        gridViewGallery.setAdapter(adapter);

        textSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    currentPage = 0;
                    search(textSearch.getText().toString());
                }
            }
        });

        gridViewGallery.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                BottomNavigationView navigation = (BottomNavigationView)getActivity().findViewById(R.id.navigation);
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(currentPage == loadingPage && i > 0 && i + i1 >= i2){
                    currentPage++;
                    search(currentResearch);
                }
            }
        });

        ((RelativeLayout)view.findViewById(R.id.layoutFilters)).setVisibility(View.GONE);

        ((ToggleButton)view.findViewById(R.id.toggleFilters)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ((RelativeLayout)view.findViewById(R.id.layoutFilters)).setVisibility(View.VISIBLE);
                    updateSearchFilters();
                } else {
                    ((RelativeLayout)view.findViewById(R.id.layoutFilters)).setVisibility(View.GONE);
                    resetSearchFilters();
                }
                search(currentResearch);
            }
        });

        ((Spinner)view.findViewById(R.id.spinnerSort)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                api.setSort(getResources().getStringArray(R.array.spinner_sort)[i]);
                if (currentResearch != null && currentResearch.length() > 0)
                    search(currentResearch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ((Spinner)view.findViewById(R.id.spinnerType)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                api.setType(getResources().getStringArray(R.array.spinner_type)[i]);
                if (currentResearch != null && currentResearch.length() > 0)
                    search(currentResearch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        search("cats");
        return view;
    }

    private void updateSearchFilters() {
        String sort = getResources().getStringArray(R.array.spinner_sort)[((Spinner)view.findViewById(R.id.spinnerSort)).getSelectedItemPosition()];
        String type = getResources().getStringArray(R.array.spinner_type)[((Spinner)view.findViewById(R.id.spinnerType)).getSelectedItemPosition()];

        api.setSort(sort);
        api.setType(type);
    }

    private void resetSearchFilters() {
        ((Spinner)view.findViewById(R.id.spinnerSort)).setSelection(0);
        ((Spinner)view.findViewById(R.id.spinnerType)).setSelection(0);
        api.reset();
    }

    /**
     * Update the GridView with images
     * @param images A List of images
     * @param page The current search page
     */
    private void updateListView(ArrayList<Image> images, int page) {
        if (page > 0) {
            if (images.size() <= 0)
                return;
            adapter.addImages(images);
            loadingPage++;
        }
        else
            adapter.setImages(images);
    }

    /**
     * Call API to search with a word
     * @param word word to search
     */
    private void search(String word) {
        if (word.length() <= 0)
            return ;
        currentResearch = word;
        if (currentPage == 0)
            ((GridView)view.findViewById(R.id.gridViewGallery)).smoothScrollToPosition(0);
        api.search(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateListView(api.parseSearchResponse(response), currentPage);
            }
        }, word, currentPage);
    }
}

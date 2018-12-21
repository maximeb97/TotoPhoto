package com.totophoto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.totophoto.fragment.SectionPageAdapter;
import com.totophoto.fragment.accountFragment;
import com.totophoto.fragment.favoritesFragment;
import com.totophoto.fragment.homeFragment;


public class MainActivity extends AppCompatActivity {

    private SectionPageAdapter mSesctionPageAdapter;

    private ViewPager mViewPager;

    private int currentLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final BottomNavigationView navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mViewPager = (ViewPager)findViewById(R.id.tab_container);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (!isNetworkAvailable())
            displayNoInternet();
    }

    @Override
    public void setContentView(int layout) {
        currentLayout = layout;
        super.setContentView(layout);
    }

    @Override
    public void onBackPressed() {
        if (currentLayout != R.layout.activity_main)
            setContentView(R.layout.activity_main);
        else if (mViewPager.getCurrentItem() != 0)
            mViewPager.setCurrentItem(0);
        else
            super.onBackPressed();
    }

    private void setupViewPager(ViewPager mViewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new homeFragment());
        adapter.addFragment(new accountFragment());
        adapter.addFragment(new favoritesFragment());
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayNoInternet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle(getString(R.string.no_internet));

        alertDialogBuilder
                .setMessage(getString(R.string.internet_requiered))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(getString(R.string.ok_too), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                }).show();

        //AlertDialog alertDialog = alertDialogBuilder.create();

        //alertDialog.show();
    }

}

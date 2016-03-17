package com.linsea.slidingimageindicator.demo;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linsea.slidingimageindicator.SlidingImageIndicator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private SlidingImageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (SlidingImageIndicator) findViewById(R.id.indicator);


        String[] filenames = null;
        try {
            filenames = loadImageFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String[] finalFilenames = filenames;
        if (finalFilenames == null || filenames.length == 0) {
            return;
        }

        for (int i = 0; i < filenames.length; i++) {
            indicator.addImageThumbs(getBitmapThumbs(filenames[i]), true);
        }

        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ImageFragment.newInstance(finalFilenames[position]);
            }

            @Override
            public int getCount() {
                return finalFilenames.length;
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        indicator.setOnSlidingChangeListener(new SlidingImageIndicator.OnSlidingChangeListener() {
            @Override
            public void onSlidingChange(View child) {

            }
        });


    }

    private Bitmap getBitmapThumbs(String filename) {
        Bitmap bitmap = BitmapFactory.decodeFile((filename));
        return bitmap;

    }


    private String[] loadImageFiles() throws IOException {
        AssetManager am = getAssets();
        return am.list(".");
    }

}

package com.linsea.slidingimageindicator.demo;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kexiwei on 2015/10/26.
 */
public class ImageFragment extends Fragment {

    public static final String FILEPATH = "filepath";
    private CharSequence filepath;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            filepath = b.getCharSequence(FILEPATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ImageView iv = new ImageView(getContext());
        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.height = ViewGroup.MarginLayoutParams.MATCH_PARENT;
        params.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
        iv.setLayoutParams(params);
        AssetManager am = getActivity().getAssets();
        try {
            InputStream is = am.open(String.valueOf(filepath));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            iv.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iv;
    }

    public static ImageFragment newInstance(String filepath) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putCharSequence(FILEPATH, filepath);
        fragment.setArguments(bundle);
        return fragment;
    }
}

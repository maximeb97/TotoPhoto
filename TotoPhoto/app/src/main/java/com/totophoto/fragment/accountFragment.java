package com.totophoto.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.totophoto.Activity.Preview.PreviewActivity;
import com.totophoto.Image.Image;
import com.totophoto.R;
import com.totophoto.api.imgur.imgur;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class accountFragment extends Fragment {
    private static final String TAG = "accountFragment";
    private static int RESULT_LOAD_IMAGE = 1;

    private ImageView btnUpload = null;
    private ImageView btnTakePicture = null;
    private imgur api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        api = new imgur(getContext());
        btnUpload = view.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture();
            }
        });

        btnTakePicture = (ImageView)view.findViewById(R.id.takePicture);

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityPicture = new Intent(view.getContext(), takePictureActivity.class);
                startActivityForResult(activityPicture, 0);
            }
        });


        return view;
    }

    /**
     * The function is called for select a picture from your device
     */
    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    /**
     * The function is called for upload the picture
     * @param picture The picture to upload
     */
    private void uploadPicture(byte[] picture) {
        btnUpload.setEnabled(false);
        Toast.makeText(getContext(), R.string.uploading_pic, Toast.LENGTH_LONG).show();
        api.upload(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Image img = api.parseUploadResponse(response);
                    Toast.makeText(getContext(), R.string.pic_uploaded, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), PreviewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("link", img.getLink());
                    b.putString("title", img.getName());
                    b.putBoolean("is_new", true);
                    intent.putExtras(b);
                    startActivity(intent);
                    btnUpload.setEnabled(true);
                } catch (JSONException e) {
                    handleErrorUpload();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleErrorUpload();
            }
        }, picture);
    }

    /**
     * The fonction Handle the error when you upload
     */
    public void handleErrorUpload() {
        Toast.makeText(getContext(), R.string.pic_not_uploaded, Toast.LENGTH_LONG).show();
        btnUpload.setEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            InputStream iStream = null;
            try {
                iStream = getContext().getContentResolver().openInputStream(selectedImage);
                byte[] inputData = getBytes(iStream);
                uploadPicture(inputData);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }

    /**
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
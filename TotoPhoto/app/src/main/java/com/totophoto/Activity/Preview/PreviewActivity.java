package com.totophoto.Activity.Preview;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.totophoto.DataBase.ManageDB;
import com.totophoto.Image.Favorite;
import com.totophoto.R;

import java.io.IOException;


public class PreviewActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    ImageView picture = null;
    EditText textUrl = null;
    TextView textTitle = null;
    ProgressBar progressBar = null;
    ToggleButton btnFavorite = null;
    Favorite fav = null;

    private String videoSource = null;
    private SurfaceView surfaceView = null;
    private SurfaceHolder surfaceHolder = null;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String url = null;
        String title = null;
        if (b != null) {
            url = b.getString("link");
            videoSource = url;
            title = b.getString("title");
            if (b.containsKey("is_new")) {
                setNewName();
            }
        }
        if (url == null)
            finish();
        previewPicture(url, title);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void previewPicture(String url, String title) {
        setContentView(R.layout.preview_image);
        ImageView btnShare = null;
        if (title == null || title.equals("null"))
            title = getString(R.string.no_title);
        fav = new Favorite(title, url);
        final ManageDB db = new ManageDB(getApplicationContext());
        picture = (ImageView)findViewById(R.id.previewImage);
        textUrl = (EditText)findViewById(R.id.textUrl);
        textTitle = (TextView)findViewById(R.id.textPictureTitle);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        btnFavorite = (ToggleButton)findViewById(R.id.setFavoriteButton);

        btnShare = (ImageButton)findViewById(R.id.btn_share);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View)view.getParent();
                EditText textUrl = (EditText)parent.findViewById(R.id.textUrl);
                TextView textTitle = (TextView)parent.findViewById(R.id.textPictureTitle);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = textUrl.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, textTitle.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        textUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link", textUrl.getText().toString());
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        if (textUrl.getText().toString().contains(".mp4")) {
            progressBar.setVisibility(View.GONE);
            picture.setVisibility(View.INVISIBLE);
            loadVideo();
        } else {
            Glide.with(getApplicationContext()).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).error(R.drawable.error_memes).fitCenter().into(picture);
        }
        textUrl.setText(url);
        textTitle.setText(title);
        if (db.checkIfExist(fav))
            btnFavorite.setChecked(true);
        btnFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    db.addFavorite(fav);
                    Toast.makeText(getApplicationContext(), R.string.added_fav, Toast.LENGTH_SHORT).show();
                }
                else {
                    db.deleteFavorite(fav);
                    Toast.makeText(getApplicationContext(), R.string.delete_fav, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setNewName() {
        final EditText txtDialogTitle = new EditText(this);
        final String[] title = {null};

        txtDialogTitle.setHint(getString(R.string.title));

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title))
                .setMessage(getString(R.string.insert_a_tittle))
                .setView(txtDialogTitle)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        title[0] = txtDialogTitle.getText().toString();
                        textTitle.setText(title[0]);
                        fav.setName(title[0]);
                    }
                })
                .setNegativeButton(getString(R.string.no_title), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void loadVideo()
    {
        surfaceView = (SurfaceView)findViewById(R.id.previewVideo);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaController != null) {
                    mediaController.show();
                }
                return false;
            }
        });
    }

    public void surfaceCreated(SurfaceHolder holder) {

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(videoSource);
            mediaPlayer.prepare();
            mediaController = new MediaController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mediaPlayer = mp;
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(surfaceView);
        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }
}

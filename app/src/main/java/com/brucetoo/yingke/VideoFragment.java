package com.brucetoo.yingke;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by Bruce Too
 * On 2/25/16.
 * At 22:46
 */
public class VideoFragment extends Fragment implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private SurfaceView surfaceView;
    MediaPlayer mediaPlayer;
    private View rootView;

    private ScrollListener listener;

    public ScrollListener createScrollListener() {
        listener = new ScrollListener() {
            @Override
            public void onScroll(final float transY, final boolean goUp) {
                Log.e("onScroll", "transY " + transY);
                if (VideoFragment.this.isVisible() && null != rootView) {
                    if (goUp) {//向上滚动
                        rootView.setTranslationY(-transY);
                    } else {//向下滚动
                        rootView.setTranslationY(getDeviceHeight(getActivity()) - transY);
                    }

                    if(transY == 0){//强制刷新Y坐标 ----- 在此要加上停止视频播放，初始化下一次加载的操作
                        rootView.setTranslationY(0);
                    }
                }
            }
        };

        return listener;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view.findViewById(R.id.layout_root);
        surfaceView = (SurfaceView) view.findViewById(R.id.sv_video);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}

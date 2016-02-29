package com.brucetoo.yingke;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class MainActivity extends FragmentActivity {

    public View rootView;
    ScrollListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.root_content);

        VideoFragment fragment = new VideoFragment();
        listener = fragment.createScrollListener();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_video_play, fragment)
                .commit();

        new MainDialogFragment(listener).show(getSupportFragmentManager(),"MainDialogFragment");
    }
}

package com.brucetoo.yingke;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Bruce too
 * On 2016/2/26
 * At 17:49
 */
public class TopMainFragment extends Fragment {
    private VerticalViewPager viewPager;

    private ScrollListener listener;

    public TopMainFragment(ScrollListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (VerticalViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new TopLayerFragment();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private static final float thresholdOffset = 0.5f;
            private boolean scrollStarted, checkDirection, goUp;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (checkDirection) {
                    if (thresholdOffset > positionOffset) {
                        Log.e("onPageScrolled", "going top");
                        goUp = true;
                    } else {
                        Log.e("onPageScrolled", "going down");
                        goUp = false;
                    }
                    Log.e("onPageScrolled", "DeviceHeight:"+getDeviceMetrics(getActivity()).heightPixels
                    +"------ DeviceWidth:"+getDeviceMetrics(getActivity()).widthPixels);
                    checkDirection = false;
                }
                //positionOffsetPixels是横向时候的数据 需转换成竖向的数据
                float hegiht = getDeviceMetrics(getActivity()).heightPixels;
                float width = getDeviceMetrics(getActivity()).widthPixels;
                listener.onScroll((float) Math.floor(hegiht / width * positionOffsetPixels), goUp);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });
    }

    public static DisplayMetrics getDeviceMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }
}

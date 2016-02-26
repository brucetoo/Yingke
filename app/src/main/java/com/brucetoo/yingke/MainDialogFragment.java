package com.brucetoo.yingke;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bruce Too
 * On 2/25/16.
 * At 22:50
 */
public class MainDialogFragment extends DialogFragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if(position == 0){
                    return new TopEmptyFragment();
                }else if (position == 1){
                    return new TopLayerFragment();
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        viewPager.setCurrentItem(1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog){
            @Override
            public void onBackPressed() {
                //监听软键盘不存在时 dialog的返回键监听 处理自定义逻辑
                Log.e("MainDialogFragment","onBackPressed");
                super.onBackPressed();
            }
        };
        return dialog;
    }

}

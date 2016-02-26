package com.brucetoo.yingke;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Bruce Too
 * On 2/25/16.
 * At 22:50
 */
public class MainDialogFragment extends DialogFragment {


    View rootView;
    View topHeader;
    View leftHeader;
    View btnShow;
    CustomEditText editInput;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rootView = view.findViewById(R.id.root_view);
        topHeader = view.findViewById(R.id.tv_header_top);
        leftHeader = view.findViewById(R.id.tv_header_left);
        btnShow = view.findViewById(R.id.btn_show_edit);
        editInput = (CustomEditText) view.findViewById(R.id.edit_input);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInput.getVisibility() == View.VISIBLE) {
                    btnShow.setVisibility(View.VISIBLE);
                    editInput.setVisibility(View.GONE);
                    hideKeyboard();
                }
            }
        });


        //监听软键盘的隐藏和显示  但是activity的配置必须是 android:windowSoftInputMode="adjustResize" 但是此处的需求不能resize activity中的布局
        //所有排除此方法
//        activityRootView = ((MainActivity)getActivity()).rootView;
//        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {//                }else {

//            @Override
//            public void onGlobalLayout() {
//                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
//                if (heightDiff > 100) { // 软键盘存在
//                }
//            }
//        });


        //这个的目的是监听软键盘显示时 点击系统的返回键目的
        editInput.setKeycodeBackListener(new CustomEditText.OnKeycodeBackListener() {
            @Override
            public void onKeycodeBack() {
                hideKeyboard();
                editInput.setVisibility(View.GONE);
                //这里为了更友好 可以 用Handler延迟一段时间显示btnShow按钮 或者布局
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnShow.setVisibility(View.VISIBLE);
                    }
                },200);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //点击显示软键盘和布局的逻辑
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShow.setVisibility(View.GONE);
                editInput.setVisibility(View.VISIBLE);
                editInput.requestFocus();
                //必须在editInput显示后调用
                showKeyboard();
            }
        });
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editInput.getWindowToken(), 0);

        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(leftHeader,"translationX",-leftHeader.getWidth(),0);
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(topHeader,"translationY",-topHeader.getHeight(),0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftAnim,topAnim);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    private void showKeyboard() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editInput, InputMethodManager.SHOW_FORCED);
            }
        }, 100);

        //头部的View执行退出动画
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(leftHeader,"translationX",0,-leftHeader.getWidth());
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(topHeader,"translationY",0,-topHeader.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(leftAnim, topAnim);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public void toggleSoftInput() {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
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

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

}

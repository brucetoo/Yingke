package com.brucetoo.yingke;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Bruce Too
 * On 2/25/16.
 * At 22:50
 */
public class TopLayerFragment extends Fragment {


    View rootView;
    View topHeader;
    View leftHeader;
    View btnShow;
    EditText editInput;
    private boolean isOpen;

    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_layer, container, false);
        rootView = view.findViewById(R.id.root_view);
        topHeader = view.findViewById(R.id.tv_header_top);
        leftHeader = view.findViewById(R.id.tv_header_left);
        btnShow = view.findViewById(R.id.btn_show_edit);
        editInput = (EditText) view.findViewById(R.id.edit_input);


        //这个的目的是监听软键盘显示时 点击系统的返回键目的
//        editInput.setKeycodeBackListener(new CustomEditText.OnKeycodeBackListener() {
//            @Override
//            public void onKeycodeBack() {
//                hideKeyboard();
//                editInput.setVisibility(View.GONE);
//                //这里为了更友好 可以 用Handler延迟一段时间显示btnShow按钮 或者布局
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        btnShow.setVisibility(View.VISIBLE);
//                    }
//                }, 200);
//            }
//        });

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

        //监听软键盘的隐藏和显示  但是activity的配置必须是 android:windowSoftInputMode="adjustResize" 但是此处的需求不能resize activity中的布局
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > 100) { // 软键盘存在
                    btnShow.setVisibility(View.GONE);
                    editInput.setVisibility(View.VISIBLE);
                    animateToHide();
                } else {
//                    Log.e("onGlobalLayout", "hide");
                    btnShow.setVisibility(View.VISIBLE);
                    editInput.setVisibility(View.GONE);
                    animateToShow();
                }
            }
        });

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
    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editInput.getWindowToken(), 0);

        animateToShow();
    }

    private void animateToShow() {

        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(leftHeader, "translationX", -leftHeader.getWidth(), 0);
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(topHeader, "translationY", -topHeader.getHeight(), 0);
        animatorSetShow.playTogether(leftAnim, topAnim);
        animatorSetShow.setDuration(300);
        animatorSetShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if(!isOpen) {
            animatorSetShow.start();
        }

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
        animateToHide();
    }

    AnimatorSet animatorSetHide = new AnimatorSet();
    AnimatorSet animatorSetShow = new AnimatorSet();

    private void animateToHide() {

//            Log.e("animateTo", "hide:" + animatorSetShow.isRunning());
        ObjectAnimator leftAnim = ObjectAnimator.ofFloat(leftHeader, "translationX", 0, -leftHeader.getWidth());
        ObjectAnimator topAnim = ObjectAnimator.ofFloat(topHeader, "translationY", 0, -topHeader.getHeight());
        animatorSetHide.playTogether(leftAnim, topAnim);
        animatorSetHide.setDuration(300);
        animatorSetHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpen = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isOpen = true;
            }
        });
        if (!isOpen) {
            animatorSetHide.start();
        }
    }

    public void toggleSoftInput() {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }


    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

}

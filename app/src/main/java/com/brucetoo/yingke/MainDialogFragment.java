package com.brucetoo.yingke;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Bruce Too
 * On 2/25/16.
 * At 22:50
 */
public class MainDialogFragment extends DialogFragment {


    View topHeader;
    View leftHeader;
    View btnShow;
    EditText editInput;
    View  activityRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        topHeader = view.findViewById(R.id.tv_header_top);
        leftHeader = view.findViewById(R.id.tv_header_left);
        btnShow = view.findViewById(R.id.btn_show_edit);
        editInput = (EditText) view.findViewById(R.id.edit_input);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInput.getVisibility() == View.VISIBLE) {
                    toggleSoftInput();
                }
            }
        });

        activityRootView = ((MainActivity)getActivity()).rootView;

        //监听软键盘的隐藏和显示
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 软键盘存在
                    btnShow.setVisibility(View.GONE);
                    editInput.setVisibility(View.VISIBLE);
                    editInput.requestFocus();
                }else {
                    btnShow.setVisibility(View.VISIBLE);
                    editInput.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSoftInput();
            }
        });
    }

    public void toggleSoftInput() {
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog);
        return dialog;
    }

}

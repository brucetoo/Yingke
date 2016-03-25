package com.brucetoo.yingke;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Bruce too
 * On 2016/2/26
 * At 13:04
 */
public class CustomEditText extends EditText {

    private OnKeycodeBackListener listener;

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setKeycodeBackListener(OnKeycodeBackListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(null != listener) {
                listener.onKeycodeBack();
                return true;
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public interface OnKeycodeBackListener {
        void onKeycodeBack();
    }
}

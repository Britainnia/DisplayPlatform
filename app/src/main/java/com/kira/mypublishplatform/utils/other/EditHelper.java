package com.kira.mypublishplatform.utils.other;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.google.android.material.textfield.TextInputLayout;
import com.kira.mypublishplatform.base.MyApplication;
import com.kira.mypublishplatform.utils.ToastUtils;
import com.kira.mypublishplatform.utils.animator.AnimUtils;

import java.util.Objects;


/**
 * [\u4e00-\u9fa5_a-zA-Z0-9_]{4,10}
 * "[\\S]+","[\\S]+","[\\S]+","[\\S]+", Validators.REGEX_PHONE_NUMBER,"\\d+","[\\S]{6,}","[\\S]{6,}","[\\S]+")
 */

public class EditHelper {

    public Activity activity;

    private SparseArray<EditHelperData> editTexts = new SparseArray<EditHelperData>();

    public EditHelper(Activity activity) {
        this.activity = activity;
    }

    /*
     * 设置监听的输入框
     */
    public void setEditText(EditHelperData... edits) {
        editTexts.clear();
        for (EditHelperData e : edits) {
            if (e != null && e.getTextView() != null) {
                editTexts.put(e.getTextView().getId(), e);
                addTextChangedListener(e);
            }
        }
    }

    public void clear() {
        editTexts.clear();
    }

    public void addEditHelperData(EditHelperData edits) {
        if (edits != null) {
            editTexts.put(edits.getView().getId(), edits);
            addTextChangedListener(edits);
        }

    }
    private void addTextChangedListener(EditHelperData edits){
        if(edits.getView() instanceof TextInputLayout) {
            TextView v = edits.getTextView();
            if (v != null) {
                v.addTextChangedListener(new EditHelper.MyTextWatcher(edits));
            }
        }
    }
    public String getText(int id) {
        TextView textView = editTexts.get(id).getTextView();
        if (textView != null) {
            return textView.getText().toString().trim();
        } else {
            return "";
        }
    }

    public TextView getTextView(int id) {
        TextView textView = editTexts.get(id).getTextView();
        return textView;
    }

    /*
     * 检查是否满足
     */
    public boolean check() {
        try {
            for (int i = 0; i < editTexts.size(); i++) {
                EditHelperData e = editTexts.valueAt(i);
                if (!e.check(activity)) {
                    return false;
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return true;
        }

    }

    /*
        * 检查是否满足
        */
    public boolean check(int id) {
        try {

            EditHelperData e = editTexts.get(id);
            if (e == null || !e.check(activity)) {
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return true;
        }

    }

    public static class EditHelperData {

        View view;
        String msg;
        String regex;

        public EditHelperData(View textView, String regex, String msg) {
            this.regex = regex;
            this.view = textView;
            this.msg = msg;
        }

        public EditHelperData(View textView, String regex, @StringRes int msgStringId) {
            this.regex = regex;
            this.view = textView;
            this.msg = textView.getContext().getString(msgStringId);
        }


        public EditHelperData(View textView, int regexMaxLenght, @StringRes int msgStringId) {
            this.regex = "[\\S]{1," + regexMaxLenght + "}";
            this.view = textView;
            this.msg = textView.getContext().getString(msgStringId);
        }

        public TextView getTextView() {

            if (view instanceof TextInputLayout)
                return ((TextInputLayout) view).getEditText();
            else if (view instanceof TextView) return (TextView) view;
            else return null;
        }
        public View getView() {
             return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setMsg(@StringRes int msgStringId) {
            this.msg = MyApplication.myApp.getString(msgStringId);
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public boolean check(Activity activity) {
            String s = getTextView().getText().toString().replaceAll("\\s*", "");
            if (view == null || !s.matches(regex)) {

                if (view instanceof TextInputLayout && ((TextInputLayout) view).isErrorEnabled()) {
                    ((TextInputLayout) view).setError(msg);
                    ToastUtils.showShort(activity,msg);
                } else {
                    if (activity != null && !activity.isFinishing()) {
                        ToastUtils.showLong(activity,msg);
//                        SnackbarUtil.showLong(view, msg, SnackbarUtil.Warning).show();
                    } else
                        ToastUtils.showShort(activity,msg);
                }
                view.requestFocus();
                AnimUtils.shakeLeft(view, 0.85f, 6f);
                return false;
            }
            if (view instanceof TextInputLayout && ((TextInputLayout) view).isErrorEnabled()) {
                ((TextInputLayout) view).setError("");

            }
            return true;
        }

    }

    public class MyTextWatcher implements TextWatcher {

        EditHelperData editHelperData ;

        public MyTextWatcher(EditHelperData editHelperData) {
            this.editHelperData = editHelperData;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editHelperData.check(EditHelper.this.activity);
        }
    }
}

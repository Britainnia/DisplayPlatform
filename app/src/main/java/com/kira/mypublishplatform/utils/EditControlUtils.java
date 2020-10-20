package com.kira.mypublishplatform.utils;

import android.text.InputFilter;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * EditText限制
 */

public class EditControlUtils {

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            if(source.equals(" "))return "";
            else return null;
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter= (source, start, end, dest, dstart, dend) -> {
            String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            Pattern pattern = Pattern.compile(speChat);
            Matcher matcher = pattern.matcher(source.toString());
            if(matcher.find())return "";
            else return null;
        };
        editText.setFilters(new InputFilter[]{filter});
    }

}

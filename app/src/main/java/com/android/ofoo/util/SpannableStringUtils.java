package com.android.ofoo.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.util.List;

/**
 * Created by hp on 2017/2/10.
 */

public class SpannableStringUtils {

    public static SpannableStringBuilder style(String sup, List<String> subList, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(sup);

        if(subList == null || subList.size() == 0)
            return builder;

        int start = 0, end = 0;
        for(String sub : subList) {
            if (sub != null && sup.contains(sub)) {
                start = sup.indexOf(sub, end);
                end = start + sub.length();
                builder.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return builder;
    }

    public static SpannableStringBuilder style(String sup, String sub, CharacterStyle style) {
        return style(sup, sub, false, style);
    }

    public static SpannableStringBuilder style(String sup, String sub, boolean toEnd, CharacterStyle style){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(sup);

        if (sup.contains(sub)) {
            int start = sup.lastIndexOf(sub);
            int end = sup.length();
            if(!toEnd){
                end = start + sub.length();
            }
            builder.setSpan(style, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

}

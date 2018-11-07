package com.jd.fce.fragment.core.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by hanyang1 on 2017/10/24.
 */
public class FragmentDateUtils {

    private static String[] parsePatterns = { "yyyy-MM-dd",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
            "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

    public static Date parseDate(String dateStr){
        try {
            return DateUtils.parseDate(dateStr, parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}

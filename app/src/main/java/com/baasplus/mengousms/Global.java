package com.baasplus.mengousms;

import android.content.Context;
import android.content.SharedPreferences;

import com.baasplus.mengousms.constants.Setting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by abner-l on 15/6/7.
 */
public class Global {
    private static Set<String> numbers = Collections.synchronizedSet(new HashSet<String>());
    private static Global instance = null;
    private Context context;

    public synchronized static Global getInstance(Context context) {
        if (instance == null) {
            instance = new Global(context);
        }
        return instance;
    }

    private Global(Context contex) {
        this.context = contex;
        initNums();
    }


    private Global initNums() {
        SharedPreferences preferences = context.getSharedPreferences(Setting.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        numbers = preferences.getStringSet(Setting.KEY_LISTEN_NUMS, numbers);
        return this;
    }

    public Global addNum(String num) {
        numbers.add(num);
        return this;
    }

    public Global delNum(String num) {
        numbers.remove(num);
        return this;
    }

    public Set<String> getNumbers() {
        return numbers;
    }

    public boolean saveNums() {
        SharedPreferences preferences = context.getSharedPreferences(Setting.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putStringSet(Setting.KEY_LISTEN_NUMS, numbers);
        return editor.commit();
    }

}

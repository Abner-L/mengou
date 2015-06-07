package com.baasplus.mengousms;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.baasplus.mengousms.constants.Setting;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by abner-l on 15/6/7.
 */
public class Global {
    private static Set<String> numbers = Collections.synchronizedSet(new HashSet<String>());
    private static Set<String> keyWords = Collections.synchronizedSet(new HashSet<String>());
    private static Global instance = null;
    private static Context context;

    public synchronized static Global getInstance(Context context) {
        if (instance == null) {
            instance = new Global(context);
        }
        return instance;
    }

    private Global(Context contex) {
        this.context = contex;
        initNums();
        initKeyWords();
    }


    private Global initNums() {
        SharedPreferences preferences = context.getSharedPreferences(Setting.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        numbers = preferences.getStringSet(Setting.KEY_LISTEN_NUMS, numbers);
        return this;
    }

    private Global initKeyWords(){
        keyWords.clear();
        SharedPreferences preferences = context.getSharedPreferences(Setting.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        String kw = preferences.getString(Setting.KEY_SMS_KEYWORDS, "");
        if (TextUtils.isEmpty(kw)){
            return this;
        }
        String[] split = kw.split(" ");
        for (String keyWord: split){
            keyWords.add(keyWord);
        }
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

    public boolean saveKeyWords(){
        SharedPreferences preferences = context.getSharedPreferences(Setting.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putStringSet(Setting.KEY_SMS_KEYWORDS, keyWords);
        return editor.commit();
    }

    public static void refreshKeyWords(String kws){
        keyWords.clear();
        if (TextUtils.isEmpty(kws)){
            return;
        }
        String[] split = kws.split(" ");
        for (String keyWord: split){
            keyWords.add(keyWord);
        }

        for (String s: keyWords){
            Log.e("keyword: ", s);
        }
    }

}

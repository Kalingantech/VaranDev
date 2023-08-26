package com.example.varandev.Backups;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

public class LanguageManager_bkp {

    private Context ctx;

    public LanguageManager_bkp(Context context){
        ctx = context;
    }
    public void updateResource(String code){

        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ctx.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLanguage(code);
    }

    private void setLanguage(String code) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
        editor.putString("Language", code);
        editor.apply();
        Log.d("load language from pref", code);
    }
    public String getLanguage() {
        return ctx.getSharedPreferences("setting", Context.MODE_PRIVATE).getString("Language","en");
    }

}

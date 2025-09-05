package com.callos16.callscreen.colorphone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.callos16.callscreen.colorphone.item.ItemContact;
import com.callos16.callscreen.colorphone.item.ItemFavorites;
import com.callos16.callscreen.colorphone.item.ItemNote;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;


public class MyShare {
    private static SharedPreferences share(Context context) {
        return context.getSharedPreferences("preferences", 0);
    }

    public static int getSizeNotification(Context context) {
        return share(context).getInt("size_notification", 0);
    }

    public static int getSizeNavigation(Context context) {
        return share(context).getInt("size_navigation", 0);
    }

    public static void putSizeNotification(Context context, int i) {
        share(context).edit().putInt("size_notification", i).apply();
    }

    public static void putSizeNavigation(Context context, int i) {
        share(context).edit().putInt("size_navigation", i).apply();
    }

    public static void applyPolicy(Context context) {
        share(context).edit().putBoolean("apply_policy", true).apply();
    }

    public static boolean isApplyPolicy(Context context) {
        return share(context).getBoolean("apply_policy", false);
    }

    public static String getPhoto(Context context) {
        return share(context).getString("photo", "");
    }

    public static void putPhoto(Context context, String str) {
        share(context).edit().putString("photo", str).apply();
    }

    public static void putStyle(Context context, int i) {
        share(context).edit().putInt("style", i).apply();
    }

    public static int getStyle(Context context) {
        return share(context).getInt("style", 0);
    }

    public static void putLayout(Context context, int i) {
        share(context).edit().putInt("layout_pos", i).apply();
    }

    public static int getLayout(Context context) {
        return share(context).getInt("layout_pos", 2);
    }

    public static void putTheme(Context context, boolean z) {
        share(context).edit().putBoolean("dark", z).apply();
    }

    public static boolean getTheme(Context context) {
        return share(context).getBoolean("dark", true);
    }

    public static void putSoundPad(Context context, int i) {
        share(context).edit().putInt("sound_pad", i).apply();
    }

    public static int getSoundPad(Context context) {
        return share(context).getInt("sound_pad", 0);
    }

    public static boolean isRate(Context context) {
        return share(context).getBoolean("is_rate_app", false);
    }

    public static void rated(Context context) {
        share(context).edit().putBoolean("is_rate_app", true).apply();
    }

    public static void putPosSim(Context context, int i) {
        share(context).edit().putInt("pos_sim", i).apply();
    }

    public static int getPosSim(Context context) {
        return share(context).getInt("pos_sim", 0);
    }

    public static void putBlockNumber(Context context, ArrayList<ItemContact> arrayList) {
        share(context).edit().putString("arr_block", new Gson().toJson(arrayList)).apply();
    }

    public static ArrayList<ItemContact> getArrBlock(Context context) {
        String string = share(context).getString("arr_block", "");
        if (!string.isEmpty()) {
            ArrayList<ItemContact> arrayList = (ArrayList) new Gson().fromJson(string, new TypeToken<ArrayList<ItemContact>>() { 
            }.getType());
            if (arrayList != null) {
                return arrayList;
            }
        }
        return new ArrayList<>();
    }

    public static void putArrNote(Context context, ArrayList<ItemNote> arrayList) {
        share(context).edit().putString("arr_note", new Gson().toJson(arrayList)).apply();
    }

    public static ArrayList<ItemNote> getArrNote(Context context) {
        String string = share(context).getString("arr_note", "");
        if (!string.isEmpty()) {
            ArrayList<ItemNote> arrayList = (ArrayList) new Gson().fromJson(string, new TypeToken<ArrayList<ItemNote>>() { 
            }.getType());
            if (arrayList != null) {
                return arrayList;
            }
        }
        return new ArrayList<>();
    }

    public static void putFav(Context context, ArrayList<ItemFavorites> arrayList) {
        share(context).edit().putString("arr_fav_contact", new Gson().toJson(arrayList)).apply();
    }

    public static ArrayList<ItemFavorites> getFav(Context context) {
        String string = share(context).getString("arr_fav_contact", "");
        if (!string.isEmpty()) {
            ArrayList<ItemFavorites> arrayList = (ArrayList) new Gson().fromJson(string, new TypeToken<ArrayList<ItemFavorites>>() { 
            }.getType());
            if (arrayList != null) {
                return arrayList;
            }
        }
        return new ArrayList<>();
    }
}

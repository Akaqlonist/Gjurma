package arl.gjurma.com.Extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import arl.gjurma.com.Gjurma;

/**
 * Created by KRYTON on 28-09-2016.
 */
public final class SharedPrefHelper {


    public static final String AuthorizationToken="AuthToken";

    private static SharedPreferences.Editor getEditor() {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences != null) {
            return sharedpreferences.edit();
        } else {
            return null;
        }
    }

    public static int getPref(String s, int i) {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences == null) {
            return i;
        } else {
            return sharedpreferences.getInt(s, i);
        }
    }

    public static long getPref(String s, long l) {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences == null) {
            return l;
        } else {
            return sharedpreferences.getLong(s, l);
        }
    }

    public static String getPref(String s, String s1) {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences == null) {
            return s1;
        } else {
            return sharedpreferences.getString(s, s1);
        }
    }

    public static boolean getPref(String s, boolean flag) {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences == null) {
            return flag;
        } else {
            return sharedpreferences.getBoolean(s, flag);
        }
    }

    public static SharedPreferences getPreferences() {
        return Gjurma.getAppContext().getSharedPreferences("Gjurma", Context.MODE_PRIVATE);
    }

    public static void putPref(String s, int i) {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) {
            return;
        } else {
            editor.putInt(s, i);
            editor.commit();
            return;
        }
    }

    public static void putPref(String s, long l) {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) {
            return;
        } else {
            editor.putLong(s, l);
            editor.commit();
            return;
        }
    }

    public static void putPref(String s, String s1) {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) {
            return;
        } else {
            editor.putString(s, s1);
            editor.commit();
            return;
        }
    }

    public static void putPref(String s, boolean flag) {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) {
            return;
        } else {
            editor.putBoolean(s, flag);
            editor.commit();
            return;
        }
    }

    public static void removePref(String s) {
        SharedPreferences.Editor editor = getEditor();
        if (editor == null) {
            return;
        } else {
            editor.remove(s);
            editor.commit();
            return;
        }
    }

    public static boolean containsPref(String s) {
        SharedPreferences sharedpreferences = getPreferences();
        if (sharedpreferences == null) {
            return false;
        } else {
            return sharedpreferences.contains(s);
        }
    }

    public static void clearAll() {

    }

    public static void printAll() {
        Log.d("mytag", getPreferences().getAll().toString());
    }

}

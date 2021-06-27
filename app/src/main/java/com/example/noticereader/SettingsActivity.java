package com.example.noticereader;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    static Activity thisActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        thisActivity = this;

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.settings, new SettingsFragment(),"root")
                    .commit();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    static class Cbx_have_authorized_listener implements Preference.OnPreferenceChangeListener {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.e("test",newValue.toString());

            if(Boolean.parseBoolean(newValue.toString()))//如果是要切换成真的
            {
                //尝试获取权限
//                        if(//拿到)
//                            return true;
//                        else
                Toast.makeText(thisActivity, "未能获取读取通知权限", Toast.LENGTH_SHORT).show();
                return false;
            }
            else
            {
                return true;
            }

        }
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            CheckBoxPreference cbx_have_authorized = findPreference("have_authorized");
            assert cbx_have_authorized != null;
            cbx_have_authorized.setOnPreferenceChangeListener(new Cbx_have_authorized_listener());
        }

    }
}
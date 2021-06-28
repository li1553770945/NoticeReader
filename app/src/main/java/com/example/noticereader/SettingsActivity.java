package com.example.noticereader;


import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {
    public static SettingsActivity thisActivity = null;
    public static boolean open = false;
    public static boolean only_love = false;
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, MyNotificationListenerService .class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, MyNotificationListenerService .class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    public boolean isNotificationListenersEnabled() { //判断是否开启通知
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),   ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void gotoNotificationAccessSetting(Context context) {
        try {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                context.startActivity(intent);
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Toast.makeText(context, "您的手机暂不支持跳转到设置页面，请手动允许权限", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
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
        toggleNotificationListenerService(this);

    }
    static class Cbx_have_authorized_listener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            if(Boolean.parseBoolean(newValue.toString()))//如果是要切换成真的
            {
                if (!thisActivity.isNotificationListenersEnabled()) {
                    gotoNotificationAccessSetting(thisActivity);
                }
                if(thisActivity.isNotificationListenersEnabled())
                {
                    return true;
                }
                else
                {
                    Toast.makeText(thisActivity, "未能获取读取通知权限", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
            else
            {
                return true;
            }

        }
    }
    static class Swi_open_listener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            open = Boolean.parseBoolean(newValue.toString());//如果是要切换成真的
            return true;

        }
    }
    static class Swi_only_love_listener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            only_love = Boolean.parseBoolean(newValue.toString());//如果是要切换成真的
            return true;

        }
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            CheckBoxPreference cbx_have_authorized = findPreference("have_authorized");
            assert cbx_have_authorized != null;
            cbx_have_authorized.setOnPreferenceChangeListener(new Cbx_have_authorized_listener());

            SwitchPreference swi_open = findPreference("open");
            assert swi_open != null;
            open = swi_open.isChecked();
            swi_open.setOnPreferenceChangeListener(new Swi_open_listener());

            SwitchPreference swi_only_love = findPreference("only_love");
            assert swi_only_love != null;
            only_love= swi_only_love.isChecked();
            swi_only_love.setOnPreferenceChangeListener(new Swi_only_love_listener());
        }

    }
}


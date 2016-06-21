package com.matejdro.pebblekeep;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.matejdro.pebblecommons.util.RootUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        if (!RootUtil.isDeviceRooted() || !RootUtil.isRootAccessible())
        {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.error_device_not_rooted)
                    .setMessage(R.string.error_not_rooted_message)
                    .setPositiveButton(R.string.button_exit, null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener()
                    {
                        @Override
                        public void onDismiss(DialogInterface dialog)
                        {
                            finish();
                        }
                    })
                    .show();
        }
    }
}

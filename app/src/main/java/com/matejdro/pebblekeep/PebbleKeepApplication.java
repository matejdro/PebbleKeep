package com.matejdro.pebblekeep;

import com.matejdro.pebblecommons.PebbleCompanionApplication;
import com.matejdro.pebblecommons.pebble.PebbleTalkerService;
import com.matejdro.pebblekeep.pebble.KeepTalkerService;

import java.util.UUID;

import timber.log.Timber;

/**
 * Created by Matej on 31. 05. 2016.
 */
public class PebbleKeepApplication extends PebbleCompanionApplication {
    public static final UUID PEBBLE_APP_UUID = UUID.fromString("0d1db88b-d34d-434c-bcab-1b12bc88e36d");

    @Override
    public UUID getPebbleAppUUID() {
        return PEBBLE_APP_UUID;
    }

    @Override
    public Class<? extends PebbleTalkerService> getTalkerServiceClass() {
        return KeepTalkerService.class;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.setAppTag("PebbleNotificationCenter");
        Timber.plant(new Timber.AppTaggedDebugTree());
    }
}

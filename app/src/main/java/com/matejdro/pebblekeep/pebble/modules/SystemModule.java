package com.matejdro.pebblekeep.pebble.modules;

import android.util.SparseArray;

import com.getpebble.android.kit.util.PebbleDictionary;
import com.matejdro.pebblecommons.pebble.CommModule;
import com.matejdro.pebblecommons.pebble.PebbleCommunication;
import com.matejdro.pebblecommons.pebble.PebbleTalkerService;
import com.matejdro.pebblekeep.pebble.WatchappHandler;

import java.util.concurrent.Callable;

import timber.log.Timber;

/**
 * Created by Matej on 31. 05. 2016.
 */
public class SystemModule extends CommModule {
    public static int MODULE_SYSTEM = 0;

    private static final short ERROR_TYPE_OLD_PHONE_APP = 0;
    private static final short ERROR_TYPE_OLD_WATCH_APP = 1;
    private static final short ERROR_TYPE_KEEP_NOT_ACCESSIBLE = 2;

    private Callable<Boolean> runOnNext = null;

    public SystemModule(PebbleTalkerService service) {
        super(service);
    }

    @Override
    public boolean sendNextMessage() {
        if (runOnNext == null)
            return false;

        Callable<Boolean> oldRunOnNext = runOnNext;

        try
        {
            boolean ret = runOnNext.call();
            if (runOnNext == oldRunOnNext)
                runOnNext = null;

            return ret;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private void sendErrorMessage(short errorType)
    {
        PebbleDictionary data = new PebbleDictionary();
        data.addUint8(0, (byte) 0);
        data.addUint8(1, (byte) 1);
        data.addUint16(2, errorType);

        Timber.d("Sending error %d...", errorType);

        getService().getPebbleCommunication().sendToPebble(data);
    }

    private void gotMessagePebbleOpened(PebbleDictionary message)
    {
        final int watchappProtocolVersion = message.getUnsignedIntegerAsLong(2).intValue();

        Timber.d("Watchapp %d opened!", watchappProtocolVersion);

        if (watchappProtocolVersion == WatchappHandler.SUPPORTED_PROTOCOL_VERSION)
        {
            if (false) //TODO check for root here, load Keep DB
            {
                //TODO
            }
            else
            {
                runOnNext = new Callable<Boolean>()
                {
                    @Override
                    public Boolean call()
                    {
                        sendErrorMessage(ERROR_TYPE_KEEP_NOT_ACCESSIBLE);
                        return true;
                    }
                };
            }

            SparseArray<CommModule> modules = getService().getAllModules();
            for (int i = 0 ; i < modules.size(); i++)
                modules.valueAt(i).pebbleAppOpened();
        }
        else if (watchappProtocolVersion > WatchappHandler.SUPPORTED_PROTOCOL_VERSION)
        {
            runOnNext = new Callable<Boolean>()
            {
                @Override
                public Boolean call()
                {
                    sendErrorMessage(ERROR_TYPE_OLD_PHONE_APP);
                    return true;
                }
            };

            WatchappHandler.showUpdateNotification(getService(), true);
        }
        else
        {
            runOnNext = new Callable<Boolean>()
            {
                @Override
                public Boolean call()
                {
                    sendErrorMessage(ERROR_TYPE_OLD_WATCH_APP);
                    return true;
                }
            };

            WatchappHandler.showUpdateNotification(getService(), false);
        }


        PebbleCommunication communication = getService().getPebbleCommunication();
        communication.queueModulePriority(this);
        communication.resetBusy();
        communication.sendNext();
    }


    @Override
    public void gotMessageFromPebble(PebbleDictionary message)
    {
        int id = message.getUnsignedIntegerAsLong(1).intValue();

        Timber.d("system packet %d", id);

        switch (id)
        {
            case 0: //Pebble opened
                gotMessagePebbleOpened(message);
                break;
        }
    }}

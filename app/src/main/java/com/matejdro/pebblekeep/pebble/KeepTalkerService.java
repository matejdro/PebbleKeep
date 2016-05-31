package com.matejdro.pebblekeep.pebble;

import com.matejdro.pebblecommons.pebble.PebbleTalkerService;
import com.matejdro.pebblekeep.pebble.modules.SystemModule;

/**
 * Created by Matej on 31. 05. 2016.
 */
public class KeepTalkerService extends PebbleTalkerService {
    @Override
    protected void registerModules() {
        addModule(new SystemModule(this), SystemModule.MODULE_SYSTEM);
    }
}

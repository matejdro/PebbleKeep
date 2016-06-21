package com.matejdro.pebblekeep;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Keep;

import java.io.File;
import java.io.IOException;

public class KeepDatabase
{
    private static KeepDatabase instance;

    private Context context;

    private SQLiteDatabase database;

    private KeepDatabase(Context context)
    {
        this.context = context;

        reload();
    }

    public boolean isLoaded()
    {
        return database != null;
    }

    public boolean reload()
    {
        if (database != null)
        {
            database.close();
            database = null;
        }

        File dbFile = copyKeepDatabase();
        if (dbFile == null || !dbFile.exists())
            return false;

        database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        return database != null && database.isOpen();
    }

    private File copyKeepDatabase()
    {
        try {
            File resultFile = new File(context.getCacheDir(), "keep.db");

            // Copy database from keep folder to our cache folder
            String cmdLine = "cat /data/data/com.google.android.keep/databases/keep.db > " + resultFile.getAbsolutePath();
            String[] args = new String[] {"su", "-c", cmdLine };
            Process process = Runtime.getRuntime().exec(args);
            process.waitFor();

            // Chmod database file so it can be used
            cmdLine = "chmod 666 " + resultFile.getAbsolutePath();
            args = new String[] {"su", "-c", cmdLine };
            process = Runtime.getRuntime().exec(args);
            process.waitFor();

            return resultFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static KeepDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = new KeepDatabase(context);

        return instance;
    }

    public static KeepDatabase getFreshInstance(Context context)
    {
        if (instance == null)
            instance = new KeepDatabase(context);
        else
            instance.reload();

        return instance;
    }

}

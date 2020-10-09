package com.example.mvvmdemo.model.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract UsersDao userDao();

    public static UserDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        UserDatabase.class, "Users.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}

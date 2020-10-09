package com.example.mvvmdemo.model.local;

import androidx.annotation.NonNull;

import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;
import com.example.mvvmdemo.util.AppExecutors;

import java.util.List;

public class UsersLocalDataSource implements UserDataSource {

    private static volatile UsersLocalDataSource INSTANCE;

    private UsersDao mUsersDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private UsersLocalDataSource(@NonNull AppExecutors appExecutors,
                                 @NonNull UsersDao tasksDao) {
        mAppExecutors = appExecutors;
        mUsersDao = tasksDao;
    }

    public static UsersLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull UsersDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (UsersLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UsersLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull final LoadUserCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<User> tasks = mUsersDao.getTasks();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onUsersLoaded(tasks);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getUser(@NonNull String userId, @NonNull GetUserCallback callback) {

    }

    @Override
    public void saveUser(@NonNull User task) {

    }

    @Override
    public void deleteUser(@NonNull String userId) {

    }
}

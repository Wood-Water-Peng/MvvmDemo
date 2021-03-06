package com.example.mvvmdemo.model.local;

import androidx.annotation.NonNull;

import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;
import com.example.mvvmdemo.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
    public void getUser(@NonNull final String userId, @NonNull final GetUserCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final User task = mUsersDao.getUserById(userId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            callback.onUserLoaded(task);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveUser(@NonNull final User user) {
        checkNotNull(user);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mUsersDao.insertTask(user);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteUser(@NonNull String userId) {

    }
}

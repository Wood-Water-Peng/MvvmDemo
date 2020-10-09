package com.example.mvvmdemo.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import static androidx.core.util.Preconditions.checkNotNull;


/**
 * 获取User信息的单一入口
 */
public class UserRepository implements UserDataSource {
    private volatile static UserRepository INSTANCE = null;
    private final UserDataSource mTasksRemoteDataSource;

    private final UserDataSource mTasksLocalDataSource;

    // Prevent direct instantiation.
    @SuppressLint("RestrictedApi")
    private UserRepository(@NonNull UserDataSource tasksRemoteDataSource,
                           @NonNull UserDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksRemoteDataSource the backend data source
     * @param tasksLocalDataSource  the device storage data source
     * @return the {@link UserRepository} instance
     */
    public static UserRepository getInstance(UserDataSource tasksRemoteDataSource,
                                             UserDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (UserRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserRepository(tasksRemoteDataSource, tasksLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull LoadUserCallback callback) {

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

package com.example.mvvmdemo.model;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 对应MVVM的Model层，用于处理数据
 */
public interface UserDataSource {
    interface LoadUserCallback {

        void onUsersLoaded(List<User> tasks);

        void onDataNotAvailable();
    }

    interface GetUserCallback {

        void onUserLoaded(User task);

        void onDataNotAvailable();
    }

    void getUsers(@NonNull LoadUserCallback callback);

    void getUser(@NonNull String userId, @NonNull GetUserCallback callback);

    void saveUser(@NonNull User task);

    void deleteUser(@NonNull String userId);
}

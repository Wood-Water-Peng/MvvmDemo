package com.example.mvvmdemo.data;

import androidx.annotation.NonNull;

import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;

import java.util.LinkedHashMap;
import com.google.common.collect.Lists;
import java.util.Map;

public class FakeUsersRemoteDataSource implements UserDataSource {
    private static FakeUsersRemoteDataSource INSTANCE;
    private static final Map<String, User> USERS_SERVICE_DATA = new LinkedHashMap<>();

    // Prevent direct instantiation.
    private FakeUsersRemoteDataSource() {
    }

    public static FakeUsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeUsersRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getUsers(@NonNull LoadUserCallback callback) {
        callback.onUsersLoaded(Lists.newArrayList(USERS_SERVICE_DATA.values()));
    }

    @Override
    public void getUser(@NonNull String userId, @NonNull GetUserCallback callback) {
        User user = USERS_SERVICE_DATA.get(userId);
        callback.onUserLoaded(user);
    }

    @Override
    public void saveUser(@NonNull User task) {

    }

    @Override
    public void deleteUser(@NonNull String userId) {

    }
}

package com.example.mvvmdemo;/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.mvvmdemo.data.FakeUsersRemoteDataSource;
import com.example.mvvmdemo.model.UserRepository;
import com.example.mvvmdemo.model.local.UserDatabase;
import com.example.mvvmdemo.model.local.UsersLocalDataSource;
import com.example.mvvmdemo.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    @SuppressLint("RestrictedApi")
    public static UserRepository provideTasksRepository(@NonNull Context context) {
        checkNotNull(context);
        UserDatabase database = UserDatabase.getInstance(context);
        return UserRepository.getInstance(FakeUsersRemoteDataSource.getInstance(),
                UsersLocalDataSource.getInstance(new AppExecutors(),
                        database.userDao()));
    }
}

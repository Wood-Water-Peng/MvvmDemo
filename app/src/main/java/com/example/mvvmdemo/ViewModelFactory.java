package com.example.mvvmdemo;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mvvmdemo.addedituser.AddEditUserViewModel;
import com.example.mvvmdemo.model.UserRepository;
import com.example.mvvmdemo.userdetail.UserDetailViewModel;
import com.example.mvvmdemo.users.UsersViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final UserRepository mUsersRepository;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(Injection.provideTasksRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    private ViewModelFactory(UserRepository repository) {
        mUsersRepository = repository;
    }

    public UserRepository getTasksRepository() {
        return mUsersRepository;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UsersViewModel.class)) {
            //noinspection unchecked
            return (T) new UsersViewModel(mUsersRepository);
        } else if (modelClass.isAssignableFrom(UserDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new UserDetailViewModel(mUsersRepository);
        }else if (modelClass.isAssignableFrom(AddEditUserViewModel.class)) {
            //noinspection unchecked
            return (T) new AddEditUserViewModel(mUsersRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

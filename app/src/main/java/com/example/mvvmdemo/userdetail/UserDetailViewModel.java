package com.example.mvvmdemo.userdetail;

import androidx.lifecycle.ViewModel;

import com.example.mvvmdemo.model.UserRepository;

public class UserDetailViewModel extends ViewModel {
    private final UserRepository mUserRepository;

    public UserDetailViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }
}

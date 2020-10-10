package com.example.mvvmdemo.userdetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmdemo.Event;
import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;
import com.example.mvvmdemo.model.UserRepository;

public class UserDetailViewModel extends ViewModel implements UserDataSource.GetUserCallback {

    private final MutableLiveData<User> mUser = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mIsDataAvailable = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mEditTaskCommand = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mDeleteTaskCommand = new MutableLiveData<>();

    private final UserRepository mUserRepository;
    private final MutableLiveData<Event<Object>> mEditUserCommand = new MutableLiveData<>();

    public MutableLiveData<Event<Object>> getEditTaskCommand() {
        return mEditUserCommand;
    }

    public UserDetailViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }

    public void editUser() {
        mEditUserCommand.setValue(new Event<>(new Object()));
    }


    public MutableLiveData<Event<Object>> getDeleteTaskCommand() {
        return mDeleteTaskCommand;
    }

    public LiveData<User> getTask() {
        return mUser;
    }

    public LiveData<Boolean> getIsDataAvailable() {
        return mIsDataAvailable;
    }

    public LiveData<Boolean> getDataLoading() {
        return mDataLoading;
    }

    public void onRefresh() {
        if (mUser.getValue() != null) {
            start(mUser.getValue().getId());
        }
    }

    public void start(String taskId) {
        if (taskId != null) {
            mDataLoading.setValue(true);
            mUserRepository.getUser(taskId, this);
        }
    }

    @Override
    public void onUserLoaded(User task) {
        setTask(task);
        mDataLoading.setValue(false);
    }

    public void setTask(User task) {
        this.mUser.setValue(task);
        mIsDataAvailable.setValue(task != null);
    }

    @Override
    public void onDataNotAvailable() {

    }
}

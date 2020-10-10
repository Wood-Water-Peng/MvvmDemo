package com.example.mvvmdemo.addedituser;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmdemo.Event;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;
import com.example.mvvmdemo.model.UserRepository;

public class AddEditUserViewModel extends ViewModel implements UserDataSource.GetUserCallback {
    @Nullable
    private String mUserId;

    private boolean mIsNewTask;

    private boolean mIsDataLoaded = false;
    private final UserRepository mUserRepository;
    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> title = new MutableLiveData<>();

    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> description = new MutableLiveData<>();

    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mUserUpdated = new MutableLiveData<>();


    public LiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public AddEditUserViewModel(UserRepository mUserRepository) {
        this.mUserRepository = mUserRepository;
    }

    //执行添加用户的操作，耗时
    public void start(String userId) {
        //避免二次操作
        if (dataLoading.getValue() != null && dataLoading.getValue()) {
            // Already loading, ignore.
            return;
        }
        mUserId = userId;
        if (userId == null) {
            // No need to populate, it's a new task
            mIsNewTask = true;
            return;
        }

        if (mIsDataLoaded) {
            // No need to populate, already have data.
            return;
        }
        mIsNewTask = false;
        dataLoading.setValue(true);
        mUserRepository.getUser(userId, this);
    }

    private boolean mTaskCompleted = false;

    @Override
    public void onUserLoaded(User task) {
        title.setValue(task.getTitle());
        description.setValue(task.getDescription());
        mTaskCompleted = task.isCompleted();
        dataLoading.setValue(false);
        mIsDataLoaded = true;
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.setValue(false);
    }

    public void saveUser() {
        User user = new User(title.getValue(), description.getValue());
        if (user.isEmpty()) {
//            mSnackbarText.setValue(new Event<>(R.string.empty_task_message));
            return;
        }
        if (isNewTask() || mUserId == null) {
            createUser(user);
        } else {
            user = new User(title.getValue(), description.getValue(), mUserId, mTaskCompleted);
            updateTask(user);
        }
    }


    private void createUser(User newUser) {
        mUserRepository.saveUser(newUser);
        mUserUpdated.setValue(new Event<>(new Object()));
    }

    private boolean isNewTask() {
        return mIsNewTask;
    }

    private void updateTask(User user) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mUserRepository.saveUser(user);
        mUserUpdated.setValue(new Event<>(new Object()));
    }
}

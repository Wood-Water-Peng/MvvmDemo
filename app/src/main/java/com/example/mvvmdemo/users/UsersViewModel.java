package com.example.mvvmdemo.users;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mvvmdemo.Event;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.model.User;
import com.example.mvvmdemo.model.UserDataSource;
import com.example.mvvmdemo.model.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理用户列表相关逻辑
 */
public class UsersViewModel extends ViewModel {
    private final UserRepository mTasksRepository;

    private final MutableLiveData<Event<String>> mOpenUserEvent = new MutableLiveData<>();
    private final MutableLiveData<Event<Object>> mNewUserEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();
    private final MutableLiveData<List<User>> mItems = new MutableLiveData<>();
    private final MutableLiveData<Integer> mCurrentFilteringLabel = new MutableLiveData<>();
    private final MutableLiveData<Integer> mNoTaskIconRes = new MutableLiveData<>();
    private final MutableLiveData<Integer> mNoTasksLabel = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mTasksAddViewVisible = new MutableLiveData<>();

    // Not used at the moment
    private final MutableLiveData<Boolean> mIsDataLoadingError = new MutableLiveData<>();

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;
    public LiveData<Boolean> getTasksAddViewVisible() {
        return mTasksAddViewVisible;
    }

    public MutableLiveData<Integer> getNoTasksLabel() {
        return mNoTasksLabel;
    }

    public MutableLiveData<Integer> getNoTaskIconRes() {
        return mNoTaskIconRes;
    }

    public UsersViewModel(UserRepository mTasksRepository) {
        this.mTasksRepository = mTasksRepository;
        setFiltering(TasksFilterType.ALL_TASKS);
    }

    public MutableLiveData<Integer> getCurrentFilteringLabel() {
        return mCurrentFilteringLabel;
    }

    public LiveData<Event<String>> getOpenUserEvent() {
        return mOpenUserEvent;
    }

    public LiveData<Event<Object>> getNewUserEvent() {
        return mNewUserEvent;
    }

    public LiveData<Boolean> isDataLoading() {
        return mDataLoading;
    }

    // This LiveData depends on another so we can use a transformation.
    public final LiveData<Boolean> empty = Transformations.map(mItems,
            new Function<List<User>, Boolean>() {
                @Override
                public Boolean apply(List<User> input) {
                    return input.isEmpty();

                }
            });

    public void addNewUser() {
        mNewUserEvent.setValue(new Event<>(new Object()));
    }

    /**
     * Called by the {@link UsersAdapter}.
     */
    void openUser(String userId) {
        mOpenUserEvent.setValue(new Event<>(userId));

    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be {@link TasksFilterType#ALL_TASKS},
     *                    {@link TasksFilterType#COMPLETED_TASKS}, or
     *                    {@link TasksFilterType#ACTIVE_TASKS}
     */
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        switch (requestType) {
            case ALL_TASKS:
                mCurrentFilteringLabel.setValue(R.string.label_all);
                mNoTasksLabel.setValue(R.string.no_tasks_all);
                mNoTaskIconRes.setValue(R.drawable.ic_assignment_turned_in_24dp);
                mTasksAddViewVisible.setValue(true);
                break;
            case ACTIVE_TASKS:
                mCurrentFilteringLabel.setValue(R.string.label_active);
                mNoTasksLabel.setValue(R.string.no_tasks_active);
                mNoTaskIconRes.setValue(R.drawable.ic_check_circle_24dp);
                mTasksAddViewVisible.setValue(false);
                break;
            case COMPLETED_TASKS:
                mCurrentFilteringLabel.setValue(R.string.label_completed);
                mNoTasksLabel.setValue(R.string.no_tasks_completed);
                mNoTaskIconRes.setValue(R.drawable.ic_verified_user_24dp);
                mTasksAddViewVisible.setValue(false);
                break;
        }

    }

    public LiveData<List<User>> getItems() {
        return mItems;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mDataLoading.setValue(true);
        }
        if (forceUpdate) {

//            mTasksRepository.refreshTasks();
        }

        mTasksRepository.getUsers(new UserDataSource.LoadUserCallback() {


            @Override
            public void onUsersLoaded(List<User> tasks) {
                if (showLoadingUI) {
                    mDataLoading.setValue(false);
                }
                mItems.setValue(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.setValue(true);
            }
        });
    }

    public void start() {
        loadTasks(false);
    }

    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate, true);
    }

}

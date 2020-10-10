package com.example.mvvmdemo.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    @Override
    public void getUsers(@NonNull final LoadUserCallback callback) {
        Preconditions.checkNotNull(callback);
        // Respond immediately with cache if available and not dirty
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onUsersLoaded(new ArrayList<>(mCachedTasks.values()));
            return;
        }
        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
//            getTasksFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getUsers(new LoadUserCallback() {
                @Override
                public void onUsersLoaded(List<User> tasks) {
                    refreshCache(tasks);
                    callback.onUsersLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadUserCallback callback) {
        mTasksRemoteDataSource.getUsers(new LoadUserCallback() {

            @Override
            public void onUsersLoaded(List<User> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onUsersLoaded(new ArrayList<>(mCachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshLocalDataSource(List<User> tasks) {
//        mTasksLocalDataSource.deleteAllTasks();
        for (User task : tasks) {
            mTasksLocalDataSource.saveUser(task);
        }
    }

    @Override
    public void getUser(@NonNull final String userId, @NonNull final GetUserCallback callback) {
        Preconditions.checkNotNull(userId);
        Preconditions.checkNotNull(callback);
        User cachedTask = getTaskWithId(userId);
        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onUserLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mTasksLocalDataSource.getUser(userId, new GetUserCallback() {

            @Override
            public void onUserLoaded(User task) {
// Do in memory cache update to keep the app UI up to date
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(task.getId(), task);
                callback.onUserLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getUser(userId, new GetUserCallback() {


                    @Override
                    public void onUserLoaded(User task) {
                        if (task == null) {
                            onDataNotAvailable();
                            return;
                        }
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedTasks == null) {
                            mCachedTasks = new LinkedHashMap<>();
                        }
                        mCachedTasks.put(task.getId(), task);

                        callback.onUserLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {

                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    @Nullable
    private User getTaskWithId(@NonNull String id) {
        Preconditions.checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }

    private void refreshCache(List<User> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (User task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, User> mCachedTasks;

    @Override
    public void saveUser(@NonNull User user) {
        Preconditions.checkNotNull(user);
        mTasksRemoteDataSource.saveUser(user);
        mTasksLocalDataSource.saveUser(user);
        // Do in memory cache update to keep the app UI up to date
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(user.getId(), user);
    }

    @Override
    public void deleteUser(@NonNull String userId) {

    }
}

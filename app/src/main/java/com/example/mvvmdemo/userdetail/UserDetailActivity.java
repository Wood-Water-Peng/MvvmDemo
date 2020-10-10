package com.example.mvvmdemo.userdetail;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmdemo.Event;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.ViewModelFactory;
import com.example.mvvmdemo.addedituser.AddEditUserActivity;
import com.example.mvvmdemo.addedituser.AddEditUserFragment;
import com.example.mvvmdemo.util.ActivityUtils;

import static com.example.mvvmdemo.userdetail.UserDetailFragment.REQUEST_EDIT_TASK;
import static com.example.mvvmdemo.users.UsersActivity.obtainViewModel;

/**
 * 详情页
 */
public class UserDetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "USER_ID";

    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;

    public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 3;

    private UserDetailViewModel mUserDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdetail_act);

        UserDetailFragment taskDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                taskDetailFragment, R.id.contentFrame);

        mUserDetailViewModel = obtainViewModel(this);

        mUserDetailViewModel.getEditTaskCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskEvent) {
                if (taskEvent.getContentIfNotHandled() != null) {
                    UserDetailActivity.this.onStartEditTask();
                }
            }
        });
    }

    private UserDetailFragment findOrCreateViewFragment() {
        String taskId = getIntent().getStringExtra(EXTRA_USER_ID);

        UserDetailFragment taskDetailFragment = (UserDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = UserDetailFragment.newInstance(taskId);
        }
        return taskDetailFragment;
    }

    @NonNull
    public static UserDetailViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(UserDetailViewModel.class);
    }

    public void onStartEditTask() {
        String taskId = getIntent().getStringExtra(EXTRA_USER_ID);
        Intent intent = new Intent(this, AddEditUserActivity.class);
        intent.putExtra(AddEditUserFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }
}

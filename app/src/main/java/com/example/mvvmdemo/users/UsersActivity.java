package com.example.mvvmdemo.users;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mvvmdemo.Event;
import com.example.mvvmdemo.R;
import com.example.mvvmdemo.ViewModelFactory;
import com.example.mvvmdemo.addedituser.AddEditUserActivity;
import com.example.mvvmdemo.userdetail.UserDetailActivity;
import com.example.mvvmdemo.util.ActivityUtils;

public class UsersActivity extends AppCompatActivity {
    private UsersViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_act);

        setupViewFragment();
        mViewModel = obtainViewModel(this);
        mViewModel.getOpenUserEvent().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> taskIdEvent) {
                String taskId = taskIdEvent.getContentIfNotHandled();
                if (taskId != null) {
                    openUserDetails(taskId);
                }

            }
        });

        // Subscribe to "new task" event
        mViewModel.getNewUserEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskIdEvent) {
                if (taskIdEvent.getContentIfNotHandled() != null) {
                    addNewUser();
                }
            }
        });
    }

    private void setupViewFragment() {
        UsersFragment tasksFragment =
                (UsersFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = UsersFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }
    }

    private void addNewUser() {
        Intent intent = new Intent(this, AddEditUserActivity.class);
        startActivityForResult(intent, AddEditUserActivity.REQUEST_CODE);
    }

    private void openUserDetails(String userId) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(UserDetailActivity.EXTRA_USER_ID, userId);
        startActivityForResult(intent, AddEditUserActivity.REQUEST_CODE);
    }

    public static UsersViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        UsersViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(UsersViewModel.class);

        return viewModel;
    }
}

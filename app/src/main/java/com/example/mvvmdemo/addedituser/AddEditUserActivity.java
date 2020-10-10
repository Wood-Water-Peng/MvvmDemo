package com.example.mvvmdemo.addedituser;

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
import com.example.mvvmdemo.util.ActivityUtils;

public class AddEditUserActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adduser_act);

        AddEditUserFragment addEditTaskFragment = obtainViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                addEditTaskFragment, R.id.contentFrame);

        obtainViewModel(this).getUserUpdatedEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskIdEvent) {
                if (taskIdEvent.getContentIfNotHandled() != null) {
                    AddEditUserActivity.this.onTaskSaved();
                }
            }
        });

    }

    private void onTaskSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }

    public static AddEditUserViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(AddEditUserViewModel.class);
    }

    @NonNull
    private AddEditUserFragment obtainViewFragment() {
        // View Fragment
        AddEditUserFragment addEditTaskFragment = (AddEditUserFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditUserFragment.newInstance();

            // Send the task ID to the fragment
            Bundle bundle = new Bundle();
            bundle.putString(AddEditUserFragment.ARGUMENT_EDIT_TASK_ID,
                    getIntent().getStringExtra(AddEditUserFragment.ARGUMENT_EDIT_TASK_ID));
            addEditTaskFragment.setArguments(bundle);
        }
        return addEditTaskFragment;
    }
}

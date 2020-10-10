package com.example.mvvmdemo.userdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.databinding.UserdetailFragBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserDetailFragment extends Fragment {

    public static final String ARGUMENT_TASK_ID = "USER_ID";

    public static final int REQUEST_EDIT_TASK = 1;
    private UserDetailViewModel mViewModel;

    public static UserDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(getArguments().getString(ARGUMENT_TASK_ID));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.userdetail_frag, container, false);
        UserdetailFragBinding viewDataBinding = UserdetailFragBinding.bind(view);
        mViewModel = UserDetailActivity.obtainViewModel(getActivity());
        viewDataBinding.setViewmodel(mViewModel);
        viewDataBinding.setLifecycleOwner(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab();
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.editUser();
            }
        });
    }
}

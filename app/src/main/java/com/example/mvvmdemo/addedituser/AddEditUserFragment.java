package com.example.mvvmdemo.addedituser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.databinding.AdduserFragBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditUserFragment extends Fragment {
    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditUserViewModel mViewModel;
    private AdduserFragBinding mViewDataBinding;

    public static AddEditUserFragment newInstance() {
        return new AddEditUserFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab();
        loadData();
    }

    private void loadData() {
        // Add or edit an existing task?
        if (getArguments() != null) {
            mViewModel.start(getArguments().getString(ARGUMENT_EDIT_TASK_ID));
        } else {
            mViewModel.start(null);
        }
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveUser();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.adduser_frag, container, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = AdduserFragBinding.bind(root);
        }
        mViewModel = AddEditUserActivity.obtainViewModel(getActivity());

        mViewDataBinding.setViewmodel(mViewModel);
        mViewDataBinding.setLifecycleOwner(getActivity());
        return mViewDataBinding.getRoot();
    }
}

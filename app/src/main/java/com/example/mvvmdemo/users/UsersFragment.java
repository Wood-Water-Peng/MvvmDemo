package com.example.mvvmdemo.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.ScrollChildSwipeRefreshLayout;
import com.example.mvvmdemo.databinding.UsersFragBinding;
import com.example.mvvmdemo.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private UsersViewModel mUsersViewModel;
    private UsersFragBinding mUsersFragBinding;
    private UsersAdapter mListAdapter;

    public static UsersFragment newInstance() {

        return new UsersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUsersFragBinding = UsersFragBinding.inflate(inflater, container, false);

        mUsersViewModel =UsersActivity.obtainViewModel(getActivity());
        mUsersFragBinding.setViewmodel(mUsersViewModel);
        mUsersFragBinding.setLifecycleOwner(getActivity());

        return mUsersFragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFab();
        setupListAdapter();
        mUsersViewModel.start();
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsersViewModel.addNewUser();
            }
        });
    }

    private void setupListAdapter() {
        ListView listView =  mUsersFragBinding.tasksList;

        mListAdapter = new UsersAdapter(
                new ArrayList<User>(0),
                mUsersViewModel,
                getActivity()
        );
        listView.setAdapter(mListAdapter);
    }
    private void setupRefreshLayout() {
//        ListView listView =  mTasksFragBinding.tasksList;
//        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTasksFragBinding.refreshLayout;
//        swipeRefreshLayout.setColorSchemeColors(
//                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
//                ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
//        );
//        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);
    }
}

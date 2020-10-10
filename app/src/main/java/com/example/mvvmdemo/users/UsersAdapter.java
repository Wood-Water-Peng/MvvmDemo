/*
 *  Copyright 2017 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.mvvmdemo.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.example.mvvmdemo.databinding.UserItemBinding;
import com.example.mvvmdemo.model.User;

import java.util.List;


public class UsersAdapter extends BaseAdapter {

    private final UsersViewModel mTasksViewModel;

    private List<User> mTasks;

    private LifecycleOwner mLifecycleOwner;

    public UsersAdapter(List<User> tasks,
                        UsersViewModel tasksViewModel, LifecycleOwner activity) {
        mTasksViewModel = tasksViewModel;
        setList(tasks);
        mLifecycleOwner = activity;

    }

    public void replaceData(List<User> tasks) {
        setList(tasks);
    }

    @Override
    public int getCount() {
        return mTasks != null ? mTasks.size() : 0;
    }

    @Override
    public User getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View view, final ViewGroup viewGroup) {
        UserItemBinding binding;
        if (view == null) {
            // Inflate
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            // Create the binding
            binding = UserItemBinding.inflate(inflater, viewGroup, false);
        } else {
            // Recycling view
            binding = DataBindingUtil.getBinding(view);
        }

        TaskItemUserActionsListener userActionsListener = new TaskItemUserActionsListener() {
            @Override
            public void onCompleteChanged(User task, View v) {
                boolean checked = ((CheckBox)v).isChecked();
//                mTasksViewModel.completeTask(task, checked);
            }

            @Override
            public void onTaskClicked(User task) {
                mTasksViewModel.openUser(task.getId());
            }
        };

        binding.setTask(mTasks.get(position));
        binding.setLifecycleOwner(mLifecycleOwner);

        binding.setListener(userActionsListener);

        binding.executePendingBindings();
        return binding.getRoot();
    }

    private void setList(List<User> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }
}

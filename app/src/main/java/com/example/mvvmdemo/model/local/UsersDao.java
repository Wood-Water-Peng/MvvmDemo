package com.example.mvvmdemo.model.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvmdemo.model.User;

import java.util.List;

@Dao
public interface UsersDao {
    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM Users")
    List<User> getTasks();

    /**
     * Select a task by id.
     *
     * @param userId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM Users WHERE entryid = :userId")
    User getUserById(String userId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(User task);

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    int updateTask(User task);
}

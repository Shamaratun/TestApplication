package com.example.testapplication.util;

import androidx.recyclerview.widget.DiffUtil;

import com.example.testapplication.model.Employee;

import java.util.List;
import java.util.Objects;

public class EmployeeDiffCallback extends DiffUtil.Callback {
    private  final List<Employee> oldList;

    private  final List<Employee> newList;

    public EmployeeDiffCallback(List<Employee> oldList, List<Employee> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return   oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(oldList.get(oldItemPosition).getId(),newList.get(oldItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}

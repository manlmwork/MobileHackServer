package com.fu.database.dao;

import com.fu.database.entity.Employee;

import java.util.List;

/**
 * Created by manlm on 7/25/2016.
 */
public interface EmployeeDao extends GenericDao<Employee, Integer> {

    /**
     * Get Admin Account by username
     *
     * @param username
     * @return
     */
    Employee getByUsername(String username);

    List<Employee> getAll();

    int updateAccount(String username, String firstName, String lastName, String phone);

    boolean checkEmailExist(String email);
}

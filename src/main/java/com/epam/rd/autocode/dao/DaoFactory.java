package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.dao.implementations.DepartmentDaoIml;
import com.epam.rd.autocode.dao.implementations.EmployeeDaoIml;

public class DaoFactory {
    public EmployeeDao employeeDAO() {
        return new EmployeeDaoIml();
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDaoIml();
    }
}

package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.dao.implementations.DepartmentDaoIml;
import com.epam.rd.autocode.dao.implementations.EmployeeDaoIml;
import com.epam.rd.autocode.dao.interfaces.DepartmentDao;
import com.epam.rd.autocode.dao.interfaces.EmployeeDao;

public class DaoFactory {
    public EmployeeDao employeeDAO() {
        return new EmployeeDaoIml();
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDaoIml();
    }
}

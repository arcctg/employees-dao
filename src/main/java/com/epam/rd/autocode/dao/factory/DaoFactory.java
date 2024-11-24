package com.epam.rd.autocode.dao.factory;

import com.epam.rd.autocode.dao.implementations.DepartmentDaoImpl;
import com.epam.rd.autocode.dao.implementations.EmployeeDaoImpl;
import com.epam.rd.autocode.dao.interfaces.DepartmentDao;
import com.epam.rd.autocode.dao.interfaces.EmployeeDao;

public class DaoFactory {
    public EmployeeDao employeeDAO() {
        return new EmployeeDaoImpl();
    }

    public DepartmentDao departmentDAO() {
        return new DepartmentDaoImpl();
    }
}

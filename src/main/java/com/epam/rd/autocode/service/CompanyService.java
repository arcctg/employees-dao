package com.epam.rd.autocode.service;

import com.epam.rd.autocode.dao.factory.DaoFactory;
import com.epam.rd.autocode.dao.interfaces.DepartmentDao;
import com.epam.rd.autocode.dao.interfaces.EmployeeDao;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;

import java.math.BigInteger;
import java.util.List;

public class CompanyService {
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;

    public CompanyService() {
        this.employeeDao = DaoFactory.createEmployeeDao();
        this.departmentDao = DaoFactory.createDepartmentDao();
    }

    public void printAllDepartments() {
        List<Department> departments = departmentDao.getAll();
        System.out.println("Departments:");
        departments.forEach(System.out::println);
        System.out.println();
    }

    public void addDepartment(Department department) {
        System.out.println("Adding department: " + department.getName());
        departmentDao.save(department);
        printAllDepartments();
    }

    public void deleteDepartment(Department department) {
        System.out.println("Deleting department: " + department.getName());
        departmentDao.delete(department);
        printAllDepartments();
    }

//    public void transferEmployee(String employeeId, String newDepartmentId) {
//        Employee employee = employeeDao.getById();
//        if (employee != null) {
//            employee.setDepartmentId(newDepartmentId);
//            employeeDao.update(employee);
//        } else {
//            throw new IllegalArgumentException("Employee not found: " + employeeId);
//        }
//    }

    public void printAllEmployees() {
        List<Employee> employees = employeeDao.getAll();
        System.out.println("Employees:");
        employees.forEach(System.out::println);
        System.out.println();
    }

    public void getEmployeesByDepartment(Department department) {
        System.out.printf("Employees by department %s:\n", department.getName());
        employeeDao.getByDepartment(department).forEach(System.out::println);
        System.out.println();
    }

    public void getEmployeesByManager(Employee employee) {
        System.out.printf("Employees by manager %s:\n", employee.getFullName().toString());
        employeeDao.getByManager(employee).forEach(System.out::println);
        System.out.println();
    }

    public void getEmployeesByManagerId(Employee employee) {
        System.out.printf("Employees by manager %s:\n", employee.getFullName().toString());
        employeeDao.getByManager(employee).forEach(System.out::println);
        System.out.println();
    }

    public void addEmployee(Employee employee) {
        System.out.println("Adding employee: " + employee.getId());
        employeeDao.save(employee);
        printAllEmployees();
    }

    public void deleteEmployee(Employee employee) {
        System.out.println("Deleting employee: " + employee.getId());
        employeeDao.delete(employee);
        printAllEmployees();
    }
}

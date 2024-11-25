package com.epam.rd.autocode;

import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;
import com.epam.rd.autocode.service.CompanyService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        CompanyService companyService = new CompanyService();
        Department department = new Department(BigInteger.valueOf(1), "IT", "New York");
        Employee employee = new Employee(
                BigInteger.valueOf(1),
                new FullName("John", "Doe", "Middle"),
                Position.MANAGER,
                LocalDate.now(),
                BigDecimal.valueOf(60000),
                null,
                BigInteger.valueOf(1)
        );

        companyService.addDepartment(department);

        companyService.addEmployee(employee);
        companyService.getEmployeesByDepartment(department);

        companyService.deleteEmployee(employee);
        companyService.deleteDepartment(department);
    }
}

package com.epam.rd.autocode.dao.implementations;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.dao.EmployeeDao;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoIml implements EmployeeDao {
    private final ConnectionSource connectionSource = ConnectionSource.instance();

    private final String GET_ONE = "SELECT * FROM EMPLOYEE WHERE ID = ?";
    private final String GET_ALL = "SELECT * FROM EMPLOYEE";
    private final String GET_ALL_BY_Manager = "SELECT * FROM EMPLOYEE WHERE MANAGER = ?";
    private final String GET_ALL_BY_DEPARTMENT = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ?";

    private final String INSERT = "INSERT INTO EMPLOYEE VALUES(?,?,?,?,?,?,?,?,?)";
    private final String DELETE = "DELETE FROM EMPLOYEE WHERE ID = ?";

    @Override
    public List<Employee> getByDepartment(Department department) {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_DEPARTMENT)) {
            statement.setInt(1, department.getId().intValue());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        new BigInteger(resultSet.getString("ID")),
                        new FullName(
                                resultSet.getString("FIRSTNAME"),
                                resultSet.getString("LASTNAME"),
                                resultSet.getString("MIDDLENAME")
                        ),
                        Position.valueOf(resultSet.getString("POSITION")),
                        resultSet.getDate("HIREDATE").toLocalDate(),
                        resultSet.getBigDecimal("SALARY"),
                        new BigInteger(resultSet.getString("MANAGER")),
                        new BigInteger(resultSet.getString("DEPARTMENT"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_Manager)) {
            statement.setInt(1, employee.getId().intValue());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        new BigInteger(resultSet.getString("ID")),
                        new FullName(
                                resultSet.getString("FIRSTNAME"),
                                resultSet.getString("LASTNAME"),
                                resultSet.getString("MIDDLENAME")
                        ),
                        Position.valueOf(resultSet.getString("POSITION")),
                        resultSet.getDate("HIREDATE").toLocalDate(),
                        resultSet.getBigDecimal("SALARY"),
                        new BigInteger(resultSet.getString("MANAGER")),
                        new BigInteger(resultSet.getString("DEPARTMENT"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Optional<Employee> getById(BigInteger Id) {
        Employee employee = null;

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ONE)) {
            statement.setInt(1, Id.intValue());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String manager = resultSet.getString("MANAGER");
                String department = resultSet.getString("DEPARTMENT");

                employee = new Employee(
                        new BigInteger(resultSet.getString("ID")),
                        new FullName(
                                resultSet.getString("FIRSTNAME"),
                                resultSet.getString("LASTNAME"),
                                resultSet.getString("MIDDLENAME")
                        ),
                        Position.valueOf(resultSet.getString("POSITION")),
                        resultSet.getDate("HIREDATE").toLocalDate(),
                        resultSet.getBigDecimal("SALARY"),
                        manager == null ? BigInteger.ZERO : new BigInteger(manager),
                        department == null ? BigInteger.ZERO : new BigInteger(department)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String manager = resultSet.getString("MANAGER");
                String department = resultSet.getString("DEPARTMENT");

                employees.add(new Employee(
                        new BigInteger(resultSet.getString("ID")),
                        new FullName(
                                resultSet.getString("FIRSTNAME"),
                                resultSet.getString("LASTNAME"),
                                resultSet.getString("MIDDLENAME")
                        ),
                        Position.valueOf(resultSet.getString("POSITION")),
                        resultSet.getDate("HIREDATE").toLocalDate(),
                        resultSet.getBigDecimal("SALARY"),
                        manager == null ? BigInteger.ZERO : new BigInteger(manager),
                        department == null ? BigInteger.ZERO : new BigInteger(department)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        Employee empl;

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (getById(employee.getId()).isPresent()) delete(employee);

            FullName fullName = employee.getFullName();
            int id = employee.getId().intValue();

            statement.setInt(1, id);
            statement.setString(2, fullName.getFirstName());
            statement.setString(3, fullName.getLastName());
            statement.setString(4, fullName.getMiddleName());
            statement.setString(5, employee.getPosition().toString());
            statement.setInt(6, employee.getManagerId().intValue());
            statement.setDate(7, Date.valueOf(employee.getHired()));
            statement.setBigDecimal(8, employee.getSalary());
            statement.setInt(9, employee.getDepartmentId().intValue());

            statement.execute();

            empl = getById(employee.getId()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return empl;
    }

    @Override
    public void delete(Employee employee) {
        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, employee.getId().intValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

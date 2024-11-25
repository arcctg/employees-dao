package com.epam.rd.autocode.dao.implementations;

import com.epam.rd.autocode.connection.ConnectionSource;
import com.epam.rd.autocode.dao.interfaces.EmployeeDao;
import com.epam.rd.autocode.dao.interfaces.StatementSetter;
import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {
    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();

    private static final String GET_ONE = "SELECT * FROM EMPLOYEE WHERE ID = ?";
    private static final String GET_ALL = "SELECT * FROM EMPLOYEE";
    private static final String GET_ALL_BY_MANAGER = "SELECT * FROM EMPLOYEE WHERE MANAGER = ?";
    private static final String GET_ALL_BY_DEPARTMENT = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ?";
    private static final String INSERT = "INSERT INTO EMPLOYEE VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String DELETE = "DELETE FROM EMPLOYEE WHERE ID = ?";

    @Override
    public List<Employee> getByDepartment(Department department) {
        return getEmployees(GET_ALL_BY_DEPARTMENT, ps -> ps.setObject(1, department.getId()));
    }

    @Override
    public List<Employee> getByManager(Employee employee) {
        return getEmployees(GET_ALL_BY_MANAGER, ps -> ps.setObject(1, employee.getId()));
    }

    @Override
    public Optional<Employee> getById(BigInteger id) {
        return getEmployee(GET_ONE, ps -> ps.setObject(1, id));
    }

    @Override
    public List<Employee> getAll() {
        return getEmployees(GET_ALL, ps -> {});
    }

    @Override
    public Employee save(Employee employee) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (getById(employee.getId()).isPresent()) {
                delete(employee);
            }

            FullName fullName = employee.getFullName();

            statement.setObject(1, employee.getId());
            statement.setString(2, fullName.getFirstName());
            statement.setString(3, fullName.getLastName());
            statement.setString(4, fullName.getMiddleName());
            statement.setString(5, employee.getPosition().toString());
            statement.setObject(6, employee.getManagerId());
            statement.setDate(7, Date.valueOf(employee.getHired()));
            statement.setBigDecimal(8, employee.getSalary());
            statement.setObject(9, employee.getDepartmentId());

            statement.execute();

            return getById(employee.getId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve the saved employee"));
        } catch (SQLException e) {
            throw new RuntimeException("Error saving employee: " + employee, e);
        }
    }

    @Override
    public void delete(Employee employee) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setObject(1, employee.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting employee: " + employee, e);
        }
    }

    private List<Employee> getEmployees(String query, StatementSetter setter) {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            setter.set(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(setToEmployee(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving employees", e);
        }
        return employees;
    }

    private Optional<Employee> getEmployee(String query, StatementSetter setter) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            setter.set(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(setToEmployee(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving employee", e);
        }
        return Optional.empty();
    }

    private Employee setToEmployee(ResultSet resultSet) throws SQLException {
        String manager = resultSet.getString("MANAGER");
        String department = resultSet.getString("DEPARTMENT");

        return new Employee(
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
}

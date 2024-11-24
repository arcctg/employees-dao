package com.epam.rd.autocode.dao.implementations;

import com.epam.rd.autocode.connection.ConnectionSource;
import com.epam.rd.autocode.dao.interfaces.DepartmentDao;
import com.epam.rd.autocode.domain.Department;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {
    private static final ConnectionSource CONNECTION_SOURCE = ConnectionSource.instance();

    private static final String GET_ONE = "SELECT * FROM DEPARTMENT WHERE ID = ?";
    private static final String GET_ALL = "SELECT * FROM DEPARTMENT";
    private static final String INSERT = "INSERT INTO DEPARTMENT VALUES(?,?,?)";
    private static final String DELETE = "DELETE FROM DEPARTMENT WHERE ID = ?";

    @Override
    public Optional<Department> getById(BigInteger id) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ONE)) {
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(setToDepartment(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving department with ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                departments.add(setToDepartment(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all departments", e);
        }

        return departments;
    }

    @Override
    public Department save(Department department) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (getById(department.getId()).isPresent()) {
                delete(department);
            }

            statement.setObject(1, department.getId());
            statement.setString(2, department.getName());
            statement.setString(3, department.getLocation());
            statement.execute();

            return getById(department.getId())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve the saved department"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Department department) {
        try (Connection connection = CONNECTION_SOURCE.createConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setObject(1, department.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting department: " + department, e);
        }
    }

    private Department setToDepartment(ResultSet resultSet) throws SQLException {
        return new Department(
                new BigInteger(resultSet.getString("ID")),
                resultSet.getString("NAME"),
                resultSet.getString("LOCATION")
        );
    }
}

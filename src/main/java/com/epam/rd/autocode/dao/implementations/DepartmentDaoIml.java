package com.epam.rd.autocode.dao.implementations;

import com.epam.rd.autocode.ConnectionSource;
import com.epam.rd.autocode.dao.DepartmentDao;
import com.epam.rd.autocode.domain.Department;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoIml implements DepartmentDao {
    private final ConnectionSource connectionSource = ConnectionSource.instance();

    private final String GET_ONE = "SELECT * FROM DEPARTMENT WHERE ID = ?";
    private final String GET_ALL = "SELECT * FROM DEPARTMENT";

    private final String INSERT = "INSERT INTO DEPARTMENT VALUES(?,?,?)";
    private final String UPDATE = "UPDATE DEPARTMENT SET ID = ?, NAME = ?, LOCATION = ? WHERE ID = ?";
    private final String DELETE = "DELETE FROM DEPARTMENT WHERE ID = ?";

    @Override
    public Optional<Department> getById(BigInteger Id) {
        Department department = null;

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ONE)) {
            statement.setInt(1, Id.intValue());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                department = new Department(
                        new BigInteger(resultSet.getString("ID")),
                        resultSet.getString("NAME"),
                        resultSet.getString("LOCATION")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(department);
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                departments.add(new Department(
                        new BigInteger(resultSet.getString("ID")),
                        resultSet.getString("NAME"),
                        resultSet.getString("LOCATION")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return departments;
    }

    @Override
    public Department save(Department department) {
        Department dep;

        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            if (getById(department.getId()).isPresent()) delete(department);

            statement.setInt(1, department.getId().intValue());
            statement.setString(2, department.getName());
            statement.setString(3, department.getLocation());

            statement.execute();

            dep = getById(department.getId()).get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dep;
    }

    @Override
    public void delete(Department department) {
        try (Connection connection = connectionSource.createConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, department.getId().intValue());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

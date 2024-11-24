package com.epam.rd.autocode.dao.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementSetter {
    void set(PreparedStatement ps) throws SQLException;
}

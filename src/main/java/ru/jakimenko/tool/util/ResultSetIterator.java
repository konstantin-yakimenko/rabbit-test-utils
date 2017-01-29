package ru.jakimenko.tool.util;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kyyakime
 */
public class ResultSetIterator implements Closeable {

    private static final Logger LOG = LogManager.getLogger();

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public ResultSetIterator(Connection connection, Statement statement, ResultSet resultSet) {
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    @Override
    public void close() throws IOException {
        DbUtils.closeQuietly(connection, statement, resultSet);
        resultSet = null;
        statement = null;
        connection = null;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }
    
    public ResultSet set() {
        return resultSet;
    }
    
    public Long getLong(int columnIndex) throws SQLException {
        return resultSet.getLong(columnIndex);
    }

    public String getString(int columnIndex) throws SQLException {
        return resultSet.getString(columnIndex);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return resultSet.getTimestamp(columnIndex);
    }
    
}

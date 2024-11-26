package io.reactivestax.utility.database;

import io.reactivestax.types.exception.TransactionHandlingException;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilsTest {

    @InjectMocks
    private DBUtils dbUtils;  // Class containing the startTransaction method

    @Mock
    private Logger log;

    @Mock
    private Connection mockConnection;


    @BeforeEach
    void setUp() {
        ApplicationPropertiesUtils.setApplicationResource("application-test.properties");
    }


    @Test
    void getInstance() {
        DBUtils dbInstance = DBUtils.getInstance();
        assertNotNull(dbInstance);
        DBUtils dbInstance1 = DBUtils.getInstance();
        assertNotNull(dbInstance1);
        assertSame(dbInstance, dbInstance1);
    }

    @Test
    void getConnection() {
        DBUtils dbInstance = DBUtils.getInstance();
        Connection connection = dbInstance.getConnection();
        assertNotNull(connection);
    }


    @Test
    void startTransaction() throws SQLException {
        DBUtils instance = DBUtils.getInstance();
        instance.startTransaction();
        Connection connection = instance.getConnection();
        assertFalse(connection.getAutoCommit());
    }

    @Test
    void commitTransaction() throws SQLException {
        DBUtils instance = DBUtils.getInstance();
        instance.startTransaction();
        Connection connection = instance.getConnection();
        assertFalse(connection.getAutoCommit());
        instance.commitTransaction();
        assertTrue(instance.getConnection().getAutoCommit());
    }

    @Test
    void commitTransactionCatchBlockTest() throws SQLException {
        DBUtils instance = DBUtils.getInstance();
        instance.startTransaction();
        Connection connection = instance.getConnection();
        connection.close();
        TransactionHandlingException thrownException = assertThrows(TransactionHandlingException.class, instance::commitTransaction);
        assertEquals("error committing transaction", thrownException.getMessage());
    }

    @Test
    void rollbackTransaction() throws SQLException {
        DBUtils instance = DBUtils.getInstance();
        instance.rollbackTransaction();
        assertTrue(instance.getConnection().getAutoCommit());
    }
}
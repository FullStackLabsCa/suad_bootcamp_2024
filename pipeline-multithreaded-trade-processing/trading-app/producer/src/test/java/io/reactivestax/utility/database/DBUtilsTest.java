package io.reactivestax.utility.database;

import io.reactivestax.types.exceptions.TransactionHandlingException;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBUtilsTest {

    @InjectMocks
    private DBUtils dbUtils;  // Class containing the startTransaction method

    @Mock
    private Logger log;

    @Mock
    private Connection mockConnection;



    @Test
    void getInstance() {
        DBUtils dbInstance = DBUtils.getInstance();
        assertNotNull(dbInstance);
        DBUtils dbInstance1 = DBUtils.getInstance();
        assertNotNull(dbInstance1);
        assertSame(dbInstance, dbInstance1);
    }

    @Test
    void getConnection() throws IOException {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.url")).thenReturn("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.username")).thenReturn("");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.password")).thenReturn("");
            DBUtils dbInstance = DBUtils.getInstance();
            Connection connection = dbInstance.getConnection();
            assertNotNull(connection);
        }
    }

    //    @Test
    //TODO look into it not completed
    void testGetConnectionCatchBlock() throws SQLException, IOException {
//        DBUtils dbInstance = DBUtils.getInstance();
//        when(dataSource.getConnection()).thenThrow(new RuntimeException("Mocked SQL Exception"));
//        HikariCPConnectionException hikariCPConnectionException = assertThrows(HikariCPConnectionException.class, dbInstance::getConnection);
//        // Verify the exception message
//        assertEquals("Error getting connection from HikkariCp", hikariCPConnectionException.getMessage());
        // Spy on your class to mock the getConnection method
        DBUtils spyClass = Mockito.spy(dbUtils);

        // Mock getConnection to return a connection that throws SQLException when setAutoCommit is called
        doReturn(mockConnection).when(spyClass).getConnection();
        doThrow(new SQLException("Test SQLException")).when(mockConnection).setAutoCommit(false);

        // Call startTransaction
        spyClass.startTransaction();

        // Verify that log.error was called with the exception message
        verify(log).info("Test SQLException");
    }

    @Test
    void startTransaction() throws SQLException, IOException {

        DBUtils instance = DBUtils.getInstance();
        instance.startTransaction();
        Connection connection = instance.getConnection();
        assertFalse(connection.getAutoCommit());
    }

    @Test
    void commitTransaction() throws SQLException, IOException {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.url")).thenReturn("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.username")).thenReturn("");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.password")).thenReturn("");
            DBUtils instance = DBUtils.getInstance();
            instance.startTransaction();
            Connection connection = instance.getConnection();
            assertFalse(connection.getAutoCommit());
            instance.commitTransaction();
            assertTrue(instance.getConnection().getAutoCommit());
        }
    }

    @Test
    void commitTransactionCatchBlockTest() throws SQLException, IOException {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.url")).thenReturn("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.username")).thenReturn("");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.password")).thenReturn("");
            DBUtils instance = DBUtils.getInstance();
            instance.startTransaction();
            Connection connection = instance.getConnection();
            connection.close();
            TransactionHandlingException thrownException = assertThrows(TransactionHandlingException.class, instance::commitTransaction);
            assertEquals("error committing transaction", thrownException.getMessage());
        }
    }

    @Test
    void rollbackTransaction() throws IOException, SQLException {
        try (MockedStatic<ApplicationPropertiesUtils> mocked = Mockito.mockStatic(ApplicationPropertiesUtils.class)) {
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.url")).thenReturn("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.username")).thenReturn("");
            mocked.when(() -> readFromApplicationPropertiesStringFormat("db.password")).thenReturn("");
            DBUtils instance = DBUtils.getInstance();
            instance.rollbackTransaction();
            assertTrue(instance.getConnection().getAutoCommit());
        }
    }
}
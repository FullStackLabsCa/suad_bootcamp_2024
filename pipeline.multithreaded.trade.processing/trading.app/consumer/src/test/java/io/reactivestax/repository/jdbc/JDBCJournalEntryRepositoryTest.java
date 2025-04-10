package io.reactivestax.repository.jdbc;

import io.reactivestax.types.dto.Trade;
import io.reactivestax.utility.database.DBUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.stream.Stream;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JDBCJournalEntryRepositoryTest {


    @Mock
    DBUtils mockDbUtils;

    @InjectMocks
    JDBCJournalEntryRepository journalEntryRepository;

    @Mock
    Connection mockConnection;

    @Mock
    PreparedStatement mockPreparedStatement;

    @Mock
    ResultSet resultSet;

    @Mock
    CallableStatement mockCallableStatement;

    @Test
    void getInstance() {
        JDBCJournalEntryRepository instance = JDBCJournalEntryRepository.getInstance();
        JDBCJournalEntryRepository instance1 = JDBCJournalEntryRepository.getInstance();
        assertSame(instance, instance1);
    }

    @Test
    void saveJournalEntry() throws SQLException {
        final String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
        final Trade trade = prepareTrade(payload);

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            //act
            journalEntryRepository.saveJournalEntry(trade);

            verify(mockConnection, atLeastOnce()).prepareStatement(any());

            verify(mockPreparedStatement, times(5)).setString(anyInt(), anyString());
            verify(mockPreparedStatement, times(1)).setInt(anyInt(), anyInt());
            verify(mockPreparedStatement, times(1)).setDouble(anyInt(), anyDouble());

            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();
        }
    }


    @ParameterizedTest
    @MethodSource("journalEntriesCount")
    void testGetJournalEntriesCount(boolean isResultSet, int callerCount) throws Exception {
        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(isResultSet);
            if (isResultSet) {
                when(resultSet.getInt(anyInt())).thenReturn(1);
            }
            Integer journalEntriesCount = journalEntryRepository.getJournalEntriesCount();
            verify(resultSet, times(callerCount)).getInt(1);
            assertEquals(callerCount, journalEntriesCount);
        }
    }


    private static Stream<Arguments> journalEntriesCount() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(false, 0));
    }


    @Test
    void callStoredProcedureForJournalAndPositionUpdate() throws Exception {

        String payload = "TDB_00000000,2024-09-19 22:16:18,TDB_CUST_5214938,V,SELL,683,638.02";
        Trade trade = prepareTrade(payload);

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareCall(anyString())).thenReturn(mockCallableStatement);
            //act
            journalEntryRepository.callStoredProcedureForJournalAndPositionUpdate(trade);

            ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<Integer> intCaptor = ArgumentCaptor.forClass(Integer.class);
            ArgumentCaptor<Double> doubleCaptor = ArgumentCaptor.forClass(Double.class);


            // Verify
            verify(mockCallableStatement).setString(eq(1), stringCaptor.capture());
            verify(mockCallableStatement).setString(eq(2), stringCaptor.capture());
            verify(mockCallableStatement).setString(eq(3), stringCaptor.capture());
            verify(mockCallableStatement).setString(eq(4), stringCaptor.capture());
            verify(mockCallableStatement).setString(eq(5), stringCaptor.capture());
            verify(mockCallableStatement).setInt(eq(6), intCaptor.capture());

            verify(mockCallableStatement).setDouble(eq(7), doubleCaptor.capture());

            verify(mockConnection, atLeastOnce()).prepareCall(any());
            verify(mockCallableStatement, atLeastOnce()).execute();
            verify(mockCallableStatement, atLeastOnce()).getString(9);

            assertEquals("TDB_00000000", stringCaptor.getAllValues().get(0));
            assertEquals("2024-09-19 22:16:18", stringCaptor.getAllValues().get(1));
            assertEquals("TDB_CUST_5214938", stringCaptor.getAllValues().get(2));
            assertEquals("V", stringCaptor.getAllValues().get(3));
            assertEquals("SELL", stringCaptor.getAllValues().get(4));
            assertEquals(683, intCaptor.getValue());
            assertEquals(638.02, doubleCaptor.getValue(), 0.001);

        }
    }


}
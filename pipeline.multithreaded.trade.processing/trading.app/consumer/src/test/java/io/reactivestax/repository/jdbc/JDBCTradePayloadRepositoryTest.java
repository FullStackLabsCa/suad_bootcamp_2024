package io.reactivestax.repository.jdbc;

import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.utility.database.DBUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JDBCTradePayloadRepositoryTest {


    @Mock
    Connection mockConnection;

    @Mock
    DBUtils mockDbUtils;

    @Mock
    PreparedStatement mockPreparedStatement;


    @Mock
    ResultSet mockResultSet;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<Integer> intCaptor;


    @InjectMocks
    JDBCTradePayloadRepository jdbcTradePayloadRepository;

    final String tradeNumber = "TDB_0000003";


    @Test
    void testUpdateLookUpStatus() throws SQLException {
        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            //act
            jdbcTradePayloadRepository.updateLookUpStatus(tradeNumber);

            verify(mockConnection, atLeastOnce()).prepareStatement(any());
            verify(mockPreparedStatement).setString(eq(1), stringCaptor.capture());
            verify(mockPreparedStatement).setString(eq(2), stringCaptor.capture());
            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();

            assertEquals(String.valueOf(LookUpStatusEnum.PASS), stringCaptor.getAllValues().get(0));
            assertEquals(tradeNumber, stringCaptor.getAllValues().get(1));
        }
    }

    @Test
    void testUpdateJournalStatus() throws SQLException {
        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            //act
            jdbcTradePayloadRepository.updateJournalStatus(tradeNumber);

            verify(mockConnection, atLeastOnce()).prepareStatement(any());

            // Verify
            verify(mockPreparedStatement).setString(eq(1), stringCaptor.capture());
            verify(mockPreparedStatement).setString(eq(2), stringCaptor.capture());

            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();

            assertEquals(String.valueOf(PostedStatusEnum.POSTED), stringCaptor.getAllValues().get(0));
            assertEquals(tradeNumber, stringCaptor.getAllValues().get(1));
        }
    }

    @Test
    void testInsertTradeIntoTradePayloadTable() throws Exception {
       final String payload = "TDB_00000003,2024-09-21 22:24:28,TDB_CUST_7788605,GOOGL,SELL,860,485.93";

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            //act
            Optional<String> tradeNumber = jdbcTradePayloadRepository.insertTradeIntoTradePayloadTable(payload);

            verify(mockConnection, atLeastOnce()).prepareStatement(any());

            // Verify
            verify(mockPreparedStatement, times(6)).setString(anyInt(), any());

            verify(mockPreparedStatement, atLeastOnce()).executeUpdate();

            tradeNumber.ifPresent(td -> assertEquals("TDB_00000003", td));
        }
    }


    @ParameterizedTest
    @MethodSource("tradePayloadCount")
    void readTradePayloadByTradeId(boolean isResultSet, int callerCount) throws SQLException {

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(isResultSet);
            Optional<String> tradePayload = jdbcTradePayloadRepository.readTradePayloadByTradeId(tradeNumber);
            verify(mockResultSet, times(callerCount)).getString(1);
            if (!isResultSet) {
                assertEquals(tradePayload, Optional.empty());
            }
        }
    }

    private static Stream<Arguments> tradePayloadCount() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(false, 0));
    }

}
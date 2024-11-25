package io.reactivestax.repository.jdbc;

import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.utility.database.DBUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

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
    void testUpdateLookUpStatus() throws SQLException, IOException {
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
    void testUpdateJournalStatus() throws SQLException, IOException {
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


}
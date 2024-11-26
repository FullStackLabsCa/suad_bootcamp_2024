package io.reactivestax.repository.jdbc;

import io.reactivestax.types.dto.Trade;
import io.reactivestax.types.exception.OptimisticLockingException;
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
import java.util.stream.Stream;

import static io.reactivestax.utility.Utility.prepareTrade;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JDBCTradePositionRepositoryTest {


    @Mock
    Connection mockConnection;

    @Mock
    DBUtils mockDbUtils;

    @Mock
    PreparedStatement mockpreparedstatement;


    @Mock
    ResultSet mockResultSet;


    @InjectMocks
    JDBCTradePositionRepository jdbcTradePositionRepository;

    final String tradeNumber = "TDB_0000003";

    final String payload = "TDB_00000003,2024-09-21 22:24:28,TDB_CUST_7788605,GOOGL,SELL,860,485.93";

    @ParameterizedTest
    @MethodSource("cusipVersionProvider")
    void getCusipVersion(boolean isResultSet, int callerCount) throws SQLException {
        Trade trade = prepareTrade(payload);
        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockpreparedstatement);
            when(mockpreparedstatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(isResultSet);
            Integer cusipVersion = jdbcTradePositionRepository.getCusipVersion(trade);
            verify(mockResultSet, times(callerCount)).getInt(any());
            if (!isResultSet) {
                assertNull(cusipVersion);
            }
        }
    }

    private static Stream<Arguments> cusipVersionProvider() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(false, 0));
    }

    @Test
    void insertPosition() throws SQLException {
        Trade trade = prepareTrade(payload);

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockpreparedstatement);
            when(mockpreparedstatement.executeUpdate()).thenReturn(1);

            //act
            boolean isPositionInserted = jdbcTradePositionRepository.insertPosition(trade);

            verify(mockConnection, atLeastOnce()).prepareStatement(any());
            verify(mockConnection, atLeastOnce()).setAutoCommit(false);
            verify(mockpreparedstatement, times(2)).setString(anyInt(), anyString());
            verify(mockpreparedstatement, times(1)).setDouble(anyInt(), anyDouble());
            verify(mockpreparedstatement, atLeastOnce()).executeUpdate();
            verify(mockConnection, atLeastOnce()).commit();
            verify(mockConnection, atLeastOnce()).setAutoCommit(true);
            assertTrue(isPositionInserted);
        }
    }

    @ParameterizedTest
    @MethodSource("upsertSupplier")
    void upsertPosition(int version, int rowsUpdated) throws SQLException {

        Trade trade = prepareTrade(payload);

        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockpreparedstatement);
            when(mockpreparedstatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(version > 0);
            when(mockpreparedstatement.executeUpdate()).thenReturn(rowsUpdated);

            if (rowsUpdated == 0) {
                assertThrows(OptimisticLockingException.class, () -> jdbcTradePositionRepository.upsertPosition(trade, version));
            } else {
                boolean isPositionUpserted = jdbcTradePositionRepository.upsertPosition(trade, version);
                verify(mockConnection, times(2)).prepareStatement(any());
                verify(mockConnection, atLeastOnce()).setAutoCommit(false);
                verify(mockpreparedstatement, times(4)).setString(anyInt(), anyString());
                verify(mockpreparedstatement, times(1)).setDouble(anyInt(), anyDouble());
                verify(mockpreparedstatement, times(1)).setInt(anyInt(), anyInt());
                verify(mockpreparedstatement, atLeastOnce()).executeUpdate();
                assertTrue(isPositionUpserted);
                verify(mockConnection, atLeastOnce()).commit();
                verify(mockConnection, atLeastOnce()).setAutoCommit(false);
                assertTrue(isPositionUpserted);
            }
        }

    }


    private static Stream<Arguments> upsertSupplier() {
        return Stream.of(
                Arguments.of(1, 0),
                Arguments.of(1, 1)
        );
    }
}
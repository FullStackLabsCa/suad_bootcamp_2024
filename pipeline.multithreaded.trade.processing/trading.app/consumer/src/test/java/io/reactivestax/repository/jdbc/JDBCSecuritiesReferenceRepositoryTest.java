package io.reactivestax.repository.jdbc;

import io.reactivestax.utility.database.DBUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JDBCSecuritiesReferenceRepositoryTest {

    @Mock
    Connection mockConnection;

    @Mock
    DBUtils mockDbUtils;

    @Mock
    PreparedStatement mockPreparedStatement;

    @Mock
    ResultSet mockResultSet;

    @InjectMocks
    JDBCSecuritiesReferenceRepository jdbcSecuritiesReferenceRepository;

    @ParameterizedTest
    @MethodSource("cusipProvider")
    void testLookUpSecurities(String cusip, String validity) throws SQLException {
        try (MockedStatic<DBUtils> dbUtilsMockedStatic = Mockito.mockStatic(DBUtils.class)) {
            dbUtilsMockedStatic.when(DBUtils::getInstance).thenReturn(mockDbUtils);
            when(mockDbUtils.getConnection()).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            if(validity.equalsIgnoreCase("valid")){
                when(mockResultSet.next()).thenReturn(true);
            }
            //act
            boolean resultCusip = jdbcSecuritiesReferenceRepository.lookUpSecurities(cusip);
            verify(mockPreparedStatement, atLeastOnce()).setString(1, cusip);
            assertEquals(resultCusip, validity.equalsIgnoreCase("valid"));
        }
    }
    private static Stream<Arguments> cusipProvider() {
        return Stream.of(
                Arguments.of("GOOGL", "valid"),
                Arguments.of("TESLA", "valid"),
                Arguments.of("V", "inValid"));
    }
}
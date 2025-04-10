package io.reactivestax.repository.jdbc;

import io.reactivestax.types.contract.repository.SecuritiesReferenceRepository;
import io.reactivestax.utility.database.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCSecuritiesReferenceRepository implements SecuritiesReferenceRepository {
    private static JDBCSecuritiesReferenceRepository instance;

    public static synchronized JDBCSecuritiesReferenceRepository getInstance() {
        if (instance == null) {
            instance = new JDBCSecuritiesReferenceRepository();
        }
        return instance;
    }


    @Override
    public boolean lookUpSecurities(String cusip) throws SQLException {
        Connection connection = DBUtils.getInstance().getConnection();
        String lookupQueryForSecurity = "SELECT 1 FROM securities_reference WHERE cusip = ?";
        try (PreparedStatement lookUpStatement = connection.prepareStatement(lookupQueryForSecurity)) {
            lookUpStatement.setString(1, cusip);
            ResultSet resultSet = lookUpStatement.executeQuery();
            return resultSet.next();
        }
    }
}

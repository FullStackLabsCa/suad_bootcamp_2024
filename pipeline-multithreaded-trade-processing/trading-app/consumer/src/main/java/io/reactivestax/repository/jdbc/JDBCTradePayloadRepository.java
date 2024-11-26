package io.reactivestax.repository.jdbc;

import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.enums.LookUpStatusEnum;
import io.reactivestax.types.enums.PostedStatusEnum;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.types.enums.StatusReasonEnum;
import io.reactivestax.types.enums.ValidityStatusEnum;
import io.reactivestax.utility.database.DBUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

import static io.reactivestax.utility.Utility.checkValidity;
import static io.reactivestax.utility.Utility.prepareTrade;


@Slf4j
public class JDBCTradePayloadRepository implements PayloadRepository {
    private static JDBCTradePayloadRepository instance;

    private JDBCTradePayloadRepository() {
    }

    public static JDBCTradePayloadRepository getInstance() {
        if (instance == null) {
            instance = new JDBCTradePayloadRepository();
        }
        return instance;
    }

    @Override
    public void updateLookUpStatus(String tradeNumber) throws SQLException {
        Connection connection = DBUtils.getInstance().getConnection();
        String updateQuery = "UPDATE trade_payloads SET lookup_status  = ? WHERE trade_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, String.valueOf(LookUpStatusEnum.PASS));
            stmt.setString(2, tradeNumber);
            stmt.executeUpdate();
            log.info("lookup updated successfully for: {}", tradeNumber);
        }
    }


    @Override
    public void updateJournalStatus(String tradeId) throws SQLException {
        Connection connection = DBUtils.getInstance().getConnection();
        String updateQuery = "UPDATE trade_payloads SET je_status  = ? WHERE trade_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, String.valueOf(PostedStatusEnum.POSTED));
            stmt.setString(2, tradeId);
            stmt.executeUpdate();
        }
    }


    @Override
    public Optional<String> insertTradeIntoTradePayloadTable(String payload) throws Exception {
        Connection connection = DBUtils.getInstance().getConnection();
        String insertQuery = "INSERT INTO trade_payloads (trade_id, validity_status, status_reason, lookup_status, je_status, payload) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            Trade trade = prepareTrade(payload);
            statement.setString(1, trade.getTradeIdentifier());
            statement.setString(2, String.valueOf(checkValidity(payload.split(",")) ? ValidityStatusEnum.VALID : ValidityStatusEnum.INVALID));
            statement.setString(3, checkValidity(payload.split(",")) ? String.valueOf(StatusReasonEnum.ALL_FIELDS_PRESENT) : String.valueOf(StatusReasonEnum.FIELDS_MISSING));
            statement.setString(4, "fail");
            statement.setString(5, "not_posted");
            statement.setString(6, payload);
            statement.executeUpdate();
            return Optional.ofNullable(trade.getTradeIdentifier());
        }
    }

    @Override
    public Optional<String> readTradePayloadByTradeId(String tradeNumber) throws SQLException {
        String insertQuery = "SELECT payload FROM trade_payloads WHERE trade_id = ?";
        Connection connection = DBUtils.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, tradeNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(resultSet.getString(1));
            }
            return Optional.empty();
        }
    }

//    public Integer selectTradePayload() throws Exception {
//        Connection connection = DBUtils.getInstance().getConnection();
//        String insertQuery = "SELECT count(*) FROM trade_payloads";
//        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt(1);
//            }
//            return 0;
//        }
//    }

}

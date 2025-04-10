package io.reactivestax.types.contract.repository;

import io.reactivestax.repository.hibernate.entity.TradePayload;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface PayloadRepository {
    void updateLookUpStatus(String tradeId) throws SQLException, IOException;

    Optional<String> insertTradeIntoTradePayloadTable(String payload) throws Exception;

    void updateJournalStatus(String tradeId) throws SQLException, IOException;

//     Optional<TradePayload> readTradePayloadByTradeId(String tradeId) throws IOException, SQLException;
}
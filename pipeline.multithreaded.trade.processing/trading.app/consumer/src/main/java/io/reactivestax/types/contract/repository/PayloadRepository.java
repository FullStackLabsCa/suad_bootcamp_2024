package io.reactivestax.types.contract.repository;

import java.sql.SQLException;
import java.util.Optional;

public interface PayloadRepository {
    void updateLookUpStatus(String tradeNumber) throws SQLException;

    Optional<String>  insertTradeIntoTradePayloadTable(String payload) throws Exception;

    void updateJournalStatus(String tradeId) throws SQLException;

     Optional<String> readTradePayloadByTradeId(String tradeNumber) throws SQLException;
}
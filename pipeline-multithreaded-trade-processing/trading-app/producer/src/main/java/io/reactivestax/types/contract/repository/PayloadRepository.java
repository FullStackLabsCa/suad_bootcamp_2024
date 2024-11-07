package io.reactivestax.types.contract.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface PayloadRepository {
    void updateLookUpStatus(String tradeId) throws SQLException, IOException;

    void insertTradeIntoTradePayloadTable(String payload) throws Exception;

    void updateJournalStatus(String tradeId) throws SQLException, IOException;

     String readTradePayloadByTradeId(String tradeId) throws IOException, SQLException;
}
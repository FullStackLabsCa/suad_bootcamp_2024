package io.reactivestax.types.contract.repository;

import io.reactivestax.types.dto.Trade;

import java.sql.SQLException;

public interface JournalEntryRepository {

    void saveJournalEntry(Trade trade) throws SQLException;
}

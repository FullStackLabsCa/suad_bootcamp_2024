package io.reactivestax.types.contract.repository;

import io.reactivestax.types.dto.Trade;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface PositionRepository {
    Integer getCusipVersion(Trade trade) throws SQLException;

    boolean insertPosition(Trade trade) throws SQLException;

    boolean upsertPosition(Trade trade, int version) throws SQLException;
}

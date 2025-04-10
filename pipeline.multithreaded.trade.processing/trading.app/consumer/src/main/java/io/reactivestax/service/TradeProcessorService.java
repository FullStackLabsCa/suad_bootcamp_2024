package io.reactivestax.service;

import io.reactivestax.types.contract.QueueLoader;
import io.reactivestax.types.contract.TradeProcessor;
import io.reactivestax.types.contract.repository.JournalEntryRepository;
import io.reactivestax.types.contract.repository.PayloadRepository;
import io.reactivestax.types.contract.repository.PositionRepository;
import io.reactivestax.types.contract.repository.SecuritiesReferenceRepository;
import io.reactivestax.types.dto.Trade;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static io.reactivestax.factory.BeanFactory.*;
import static io.reactivestax.utility.Utility.prepareTrade;

@Slf4j
public class TradeProcessorService implements TradeProcessor {
    private static final AtomicInteger countSec = new AtomicInteger(0);
    private static TradeProcessorService instance;

    private TradeProcessorService() {
    }

    public static synchronized TradeProcessorService getInstance() {
        if (instance == null) {
            instance = new TradeProcessorService();
        }
        return instance;
    }

    @Override
    public void processTrade(String tradeId) throws SQLException {
        PayloadRepository tradePayloadRepository = getTradePayloadRepository();
        SecuritiesReferenceRepository lookupSecuritiesRepository = getLookupSecuritiesRepository();
        JournalEntryRepository journalEntryRepository = getJournalEntryRepository();
        tradePayloadRepository.readTradePayloadByTradeId(tradeId).ifPresent(payload -> {
            Trade trade = prepareTrade(payload);
            log.info("result journal{}", payload);
            try {
                boolean validSecurity = lookupSecuritiesRepository.lookUpSecurities(trade.getCusip());
                if (validSecurity) {
                    journalEntryRepository.saveJournalEntry(trade);
                    tradePayloadRepository.updateLookUpStatus(tradeId);
                    tradePayloadRepository.updateJournalStatus(tradeId);
                    executePositionTransaction(trade);
                } else {
                    log.debug("times {} {}", trade.getCusip(), countSec.incrementAndGet());
                    throw new RuntimeException(); // For checking the max retry mechanism throwing error and catching it in retry mechanism.....
                }
            } catch (SQLException e) {
                log.info("Exception while processing journal entries");
                throw new RuntimeException(e);
            }
        });
    }

    public void executePositionTransaction(Trade trade) throws SQLException {
        PositionRepository positionsRepository = getPositionsRepository();
        Integer version = positionsRepository.getCusipVersion(trade);
        if (version != null) {
            positionsRepository.upsertPosition(trade, version);
        } else {
            positionsRepository.insertPosition(trade);
        }
    }
}

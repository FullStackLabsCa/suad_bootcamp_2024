package io.reactivestax.repository.hibernate;

import io.reactivestax.types.contract.repository.JournalEntryRepository;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.repository.hibernate.entity.JournalEntries;
import io.reactivestax.utility.database.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;


@Slf4j
public class HibernateJournalEntryRepository implements JournalEntryRepository {

    private static HibernateJournalEntryRepository instance;

    private HibernateJournalEntryRepository() {
    }

    public static synchronized HibernateJournalEntryRepository getInstance() {
        if (instance == null) {
            instance = new HibernateJournalEntryRepository();
        }
        return instance;
    }

    @Override
    public void saveJournalEntry(Trade trade) {
        Session session = HibernateUtil.getInstance().getConnection();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            getJournalEntries(trade).ifPresent(session::persist);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.error(e.getMessage());
            }
        }
    }

    private static Optional<JournalEntries> getJournalEntries(Trade trade) {
        JournalEntries journalEntries = new JournalEntries();
        journalEntries.setTradeId(trade.getTradeIdentifier());
        journalEntries.setTradeDate(trade.getTradeDateTime());
        journalEntries.setAccountNumber(trade.getAccountNumber());
        journalEntries.setCusip(trade.getCusip());
        journalEntries.setDirection(trade.getDirection());
        journalEntries.setQuantity(trade.getQuantity());
        journalEntries.setPrice(trade.getPrice());
        return Optional.of(journalEntries);
    }


}


package io.reactivestax.repository.hibernate;

import io.reactivestax.types.contract.repository.PositionRepository;
import io.reactivestax.types.dto.Trade;
import io.reactivestax.repository.hibernate.entity.Position;
import io.reactivestax.utility.database.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.math.BigInteger;
import java.sql.SQLException;

@Slf4j
public class HibernateTradePositionRepository implements PositionRepository {

    private static HibernateTradePositionRepository instance;

    private HibernateTradePositionRepository() {
    }

    public static synchronized HibernateTradePositionRepository getInstance() {
        if (instance == null) {
            instance = new HibernateTradePositionRepository();
        }
        return instance;
    }

    @Override
    public boolean insertPosition(Trade trade) throws SQLException {
        Session session = HibernateUtil.getInstance().getConnection();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Position position = getPosition(trade);
            session.persist(position);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.error("Transaction Failed", e); // Ensure this line exists
                return false;
            }
        }
        return true;
    }

    private Position getPosition(Trade trade) {
        return Position.builder().
                accountNumber(trade.getAccountNumber())
                .cusip(trade.getCusip())
                .position(BigInteger.valueOf(trade.getQuantity()))
                .direction(trade.getDirection())
                .build();
    }

    public boolean upsertPosition(Trade trade, int version) {
        Session session = HibernateUtil.getInstance().getConnection();
        Transaction transaction = null;
        try {
            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Position> query = cb.createQuery(Position.class);
            Root<Position> root = query.from(Position.class);

            Predicate accountNumberClause = cb.equal(root.get("accountNumber"), trade.getAccountNumber());
            Predicate cusipClause = cb.equal(root.get("cusip"), trade.getCusip());
            Predicate versionClause = cb.equal(root.get("version"), version);
            query.select(root).where(cb.and(accountNumberClause, cusipClause, versionClause)); //for OR we can use cb.or clause
            Position position = session.createQuery(query).uniqueResult();
            transaction = session.beginTransaction();
            if (position.getDirection().toString().equalsIgnoreCase("BUY")) {
                position.getPosition().add(BigInteger.valueOf(trade.getPosition()));
            } else {
                position.getPosition().subtract(BigInteger.valueOf(trade.getPosition()));
            }

            position.setVersion(version + 1);
            session.persist(position);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                log.error(e.getMessage());
                return false;
            }
        }
        return true;
    }


    //using the criteria api for returning the payloadByTradeId
    public Integer getCusipVersion(Trade trade) {
        Session session = HibernateUtil.getInstance().getConnection();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Integer> query = criteriaBuilder.createQuery(Integer.class);
        Root<Position> root = query.from(Position.class);
        query.select(root.get("version"));

        // Add multiple where conditions using criteriaBuilder.and()
        Predicate accountNumberPredicate = criteriaBuilder.equal(root.get("accountNumber"), trade.getAccountNumber());
        Predicate cusipPredicate = criteriaBuilder.equal(root.get("cusip"), trade.getCusip());

        // Combine predicates with AND
        query.where(criteriaBuilder.and(accountNumberPredicate, cusipPredicate));
        return session.createQuery(query).uniqueResult();
    }
}


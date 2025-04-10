package io.reactivestax.repository.hibernate;

import io.reactivestax.repository.hibernate.entity.SecuritiesReference;
import io.reactivestax.utility.database.HibernateUtil;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HibernateSecuritiesReferenceRepositoryTest {

    @Mock
    Session mockSession;

    @Mock
    HibernateUtil mockHibernateUtil;

    @Mock
    HibernateCriteriaBuilder mockCriteriaBuilder;

    @Mock
    JpaCriteriaQuery<SecuritiesReference> mockCriteriaQuery;

    @Mock
    JpaRoot<SecuritiesReference> mockRoot;

    @Test
    void getInstance() {
        HibernateSecuritiesReferenceRepository instance = HibernateSecuritiesReferenceRepository.getInstance();
        HibernateSecuritiesReferenceRepository instance1 = HibernateSecuritiesReferenceRepository.getInstance();
        assertEquals(instance.hashCode(), instance1.hashCode());
    }

    @Test
    void lookUpSecurities() {
        try (MockedStatic<HibernateUtil> hibernateUtilMockedStatic = Mockito.mockStatic(HibernateUtil.class)) {
            hibernateUtilMockedStatic.when(HibernateUtil::getInstance).thenReturn(mockHibernateUtil);
            when(mockHibernateUtil.getConnection()).thenReturn(mockSession);
            Query mockQuery = Mockito.mock(Query.class);

            when(mockSession.getCriteriaBuilder()).thenReturn(mockCriteriaBuilder);
            when(mockCriteriaBuilder.createQuery(SecuritiesReference.class)).thenReturn(mockCriteriaQuery);
            when(mockCriteriaQuery.from(SecuritiesReference.class)).thenReturn(mockRoot);

            // Mock the select and where clause
            when(mockCriteriaQuery.select(mockRoot)).thenReturn(mockCriteriaQuery);
            when(mockCriteriaQuery.where(Mockito.nullable(Predicate.class))).thenReturn(mockCriteriaQuery);


            when(mockSession.createQuery(mockCriteriaQuery)).thenReturn(mockQuery);
            when(mockQuery.getResultList()).thenReturn(Collections.singletonList(new SecuritiesReference()));

            // Test method
            boolean result = HibernateSecuritiesReferenceRepository.getInstance().lookUpSecurities("TSLA");
            assertTrue(result);

            verify(mockSession).getCriteriaBuilder();
            verify(mockCriteriaBuilder).createQuery(SecuritiesReference.class);
            verify(mockCriteriaQuery).from(SecuritiesReference.class);
            verify(mockCriteriaQuery).select(mockRoot);
            verify(mockQuery).getResultList();

        }
    }
}
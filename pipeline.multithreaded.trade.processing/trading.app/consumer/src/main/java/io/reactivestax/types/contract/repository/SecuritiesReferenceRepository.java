package io.reactivestax.types.contract.repository;

import java.sql.SQLException;

public interface SecuritiesReferenceRepository {
    boolean lookUpSecurities(String cusip) throws  SQLException;
}

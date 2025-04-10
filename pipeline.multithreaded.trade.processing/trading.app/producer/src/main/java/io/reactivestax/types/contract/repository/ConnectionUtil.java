package io.reactivestax.types.contract.repository;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ConnectionUtil<T> {
    T getConnection() throws IOException;
}

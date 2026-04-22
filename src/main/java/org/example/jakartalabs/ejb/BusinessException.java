package org.example.jakartalabs.ejb;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}

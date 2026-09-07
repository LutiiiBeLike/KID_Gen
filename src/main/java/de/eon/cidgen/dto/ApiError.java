package de.eon.cidgen.dto;

/**
 * Keeps client-facing API errors small and consistent.
 */
public class ApiError {

    private final String error;

    public ApiError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

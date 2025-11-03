package com.example.bank.model;

import java.time.Instant;

public class ErrorResponse {
    private String error;
    private String message;
    private Instant timestamp = Instant.now();

    public ErrorResponse() {}
    public ErrorResponse(String error, String message) {
        this.error = error; this.message = message;
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public Instant getTimestamp() { return timestamp; }
    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

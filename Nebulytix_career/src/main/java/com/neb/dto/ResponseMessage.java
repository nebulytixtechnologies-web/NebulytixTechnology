package com.neb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * ---------------------------------------------------------------
 * File Name   : ResponseMessage.java
 * Package     : com.neb.dto
 * ---------------------------------------------------------------
 * Purpose :
 *   This class is used as a standard response wrapper for all
 *   API responses in the application.
 *
 * Description :
 *   - It provides a consistent structure for sending responses
 *     from the backend to the frontend.
 *   - It can hold success or error messages, along with any type
 *     of data object (single record or list).
 *
 * Fields :
 *   statusCode → Represents the HTTP status code (e.g., 200, 400, 500)
 *   status     → Textual representation of the status (e.g., OK, ERROR)
 *   message    → Custom message describing the response result
 *   data       → Generic type T that can hold any object or list
 *
 * Constructors :
 *   - ResponseMessage(int, String, String)
 *       → Used when only message details are needed.
 *   - ResponseMessage(int, String, String, T)
 *       → Used when response also includes data.
 *
 * Result :
 *   This class helps in returning uniform and structured responses
 *   across all REST API endpoints.
 * ---------------------------------------------------------------
 */

@Setter
@Getter
@AllArgsConstructor
public class ResponseMessage<T> {

    private int statusCode;
    private String status;
    private String message;
    private T data; // for single object OR list
    
    public ResponseMessage(int statusCode, String status, String message) {
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
    }
}

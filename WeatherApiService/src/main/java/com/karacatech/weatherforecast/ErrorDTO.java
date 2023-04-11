package com.karacatech.weatherforecast;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ErrorDTO {
    private Date timestamp;
    private int status;
    private String path;
    private List<String> errors;

    public ErrorDTO(Date timestamp, int status, String path, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
        this.errors = errors;
    }


    public ErrorDTO() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String message) {
        this.errors.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDTO errorDTO = (ErrorDTO) o;
        return status == errorDTO.status && Objects.equals(timestamp, errorDTO.timestamp) && Objects.equals(path, errorDTO.path) && Objects.equals(errors, errorDTO.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, path, errors);
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", path='" + path + '\'' +
                ", errors=" + errors +
                '}';
    }
}

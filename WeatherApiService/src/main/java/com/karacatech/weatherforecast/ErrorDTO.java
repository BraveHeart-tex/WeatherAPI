package com.karacatech.weatherforecast;

import java.util.Date;
import java.util.Objects;

public class ErrorDTO {
    private Date timestamp;
    private int status;
    private String path;
    private String error;

    public ErrorDTO(Date timestamp, int status, String path, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.path = path;
        this.error = error;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorDTO errorDTO = (ErrorDTO) o;
        return status == errorDTO.status && Objects.equals(timestamp, errorDTO.timestamp) && Objects.equals(path, errorDTO.path) && Objects.equals(error, errorDTO.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, path, error);
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", path='" + path + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}

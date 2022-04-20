package com.example.mentalhealthsupport;

import java.io.Serializable;

public class User  implements Serializable {
    String id, fullName, email, status, isolationDate;
    long isolationStart, isolationEnd;
    String contact;

    public User(){

    }

    public User(String id, String email, String fullName, String status, String isolationDate, long isolationStart, long isolationEnd, String contact) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.status = status;
        this.isolationDate = isolationDate;
        this.isolationStart = isolationStart;
        this.isolationEnd = isolationEnd;
        this.contact = contact;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsolationDate() {
        return isolationDate;
    }

    public void setIsolationDate(String isolationDate) {
        this.isolationDate = isolationDate;
    }

    public long getIsolationStart() {
        return isolationStart;
    }

    public void setIsolationStart(long isolationStart) {
        this.isolationStart = isolationStart;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getIsolationEnd() {
        return isolationEnd;
    }

    public void setIsolationEnd(long isolationEnd) {
        this.isolationEnd = isolationEnd;
    }
}

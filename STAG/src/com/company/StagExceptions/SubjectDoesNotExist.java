package com.company.StagExceptions;

public class SubjectDoesNotExist extends StagException {

    private final String subject;

    public SubjectDoesNotExist(String subject) {
        this.subject = subject;
    }

    public String toString() {
        return ("Subject does not exist " + subject);
    }
}

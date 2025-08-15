package util;

public class AuthResult {
    public enum Status { SUCCESS, FAILED, BLOCKED }
    private final Status status;

    public AuthResult(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

}
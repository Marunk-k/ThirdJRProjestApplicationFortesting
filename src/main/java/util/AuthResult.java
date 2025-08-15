package util;

import lombok.Getter;

@Getter
public class AuthResult {

    private final Status status;

    public AuthResult(Status status) {
        this.status = status;
    }

}
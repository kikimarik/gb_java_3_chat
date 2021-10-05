package ru.geekbrains.auth;

public class AuthFailedError extends Error {
    public AuthFailedError() {
        super("Invalid login or password.");
    }
}

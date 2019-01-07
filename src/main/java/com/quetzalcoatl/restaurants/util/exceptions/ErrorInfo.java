package com.quetzalcoatl.restaurants.util.exceptions;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;

    public ErrorInfo(CharSequence url,  Throwable ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        detail = ex.getLocalizedMessage();
    }
}

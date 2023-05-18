package com.Ronenii.Kaplat_server_exercise.Model;

public class Result<T> {
    private T result;
    private String errorMessage;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString(){
        return "Result(result=" + result + ", errorMessage=" + errorMessage + ")";
    }
}

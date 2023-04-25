package com.example.shopofmasters.enumm;

public enum Status {
    Принят ("Принат"),
    Оформлен("Оформлен"),
    Ожидает ("Ожидает"),
    Получен ("Получен");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

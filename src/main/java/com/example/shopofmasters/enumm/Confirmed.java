package com.example.shopofmasters.enumm;

public enum Confirmed {
    Администратор("Администратор"),
    НародныйМастер ("Народный мастер"),
    Мастер ("Мастер"),
    Продавец ("Продавец"),
    Пользователь("Пользователь");

    private final String confirmedValue;

    Confirmed(String confirmedValue) {
        this.confirmedValue = confirmedValue;
    }

    public String getConfirmedValue() {
        return confirmedValue;
    }
}

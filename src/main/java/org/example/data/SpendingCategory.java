package org.example.data;

public enum SpendingCategory {
    FOOD("еда"),
    RENT("квартплата"),
    HEALING("лечение"),
    CHILD("ребенок"),
    ALCOHOL("алкоголь"),
    SPORT("спорт"),
    TRANSPORT("транспорт"),
    STUFF("вещи"),
    CARD2CARD("перевод_на_карту"),
    CREDIT("кредитная_карта"),
    CLOTH("одежда"),
    ENTERTAINMENT("развлечение"),
    ANOTHER("другое");
    private final String name;


    SpendingCategory(String name) {
        this.name = name;
    }
}

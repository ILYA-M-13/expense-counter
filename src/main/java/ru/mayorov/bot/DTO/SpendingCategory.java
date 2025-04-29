package ru.mayorov.bot.DTO;

public enum SpendingCategory {
    FOOD("еда"),
    RENT("жкх"),
    HEALING("здоровье"),
    CHILD("ребенок"),
    ALCOHOL("алкоголь"),
    SPORT("спорт"),
    TRANSPORT("транспорт"),
    STUFF("вещи"),
    CARD2CARD("перевод на карту"),
    CREDIT("кредитная карта"),
    CLOTH("одежда"),
    ENTERTAINMENT("развлечение"),
    ANOTHER("другое");
    private final String name;


    SpendingCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

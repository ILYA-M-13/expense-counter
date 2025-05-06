package ru.mayorov.bot.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum SpendingCategory {
    FOOD("продукты"),
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
    COMMUNICATION("связь"),
    REPAIR("ремонт"),
    HOBBY("хобби"),
    ANOTHER("другое");
    private final String name;

}

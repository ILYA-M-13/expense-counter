package ru.mayorov.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KeyboardHandler {

    public InlineKeyboardMarkup getFirstKeyboardMarkup() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
                createButton("\uD83D\uDC68\u200D\uD83C\uDF7C Про Марка", "MARK"),
                createButton("\uD83D\uDCB0 Бюджет", "BUDGET")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getFinanceMenuKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
                createButton("\uD83D\uDCB5 Внести расходы", "EXPENSE"),
                createButton("\uD83D\uDCCA Статистика", "STATISTICS")
        ));
        rows.add(Arrays.asList(
                createButton("\uD83D\uDD19 Назад", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getStatMenuKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(
                createButton("\uD83D\uDCC9 По годам", "BYYEAR")
        ));
        rows.add(List.of(
                createButton("\uD83D\uDD19 Назад", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);

    }

    public InlineKeyboardMarkup getStatMenuByYearKeyboard(int year) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        if (year < currentYear) {
            int previous = year - 1;
            int next = year + 1;
            rows.add(Arrays.asList(
                    createButton("◀\uFE0F " + previous, "BYYEAR_" + previous),
                    createButton(next + " ▶\uFE0F", "BYYEAR_" + next)
            ));
        }
        if (year == currentYear) {
            int previous = year - 1;
            rows.add(Arrays.asList(
                    createButton("◀\uFE0F " + previous, "BYYEAR_" + previous),
                    createButton(year + " ▶\uFE0F", "ignore")
            ));
        }

        rows.add(List.of(
                createButton("\uD83D\uDCC9 По категориям", "CATEGORY")
        ));
        rows.add(List.of(
                createButton("\uD83D\uDD19 Назад", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getInfoMarkMenuKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
                createButton("\uD83D\uDCCC Сделать запись", "7"),
                createButton("\uD83D\uDC6A Фото Марка", "8")
        ));
        rows.add(List.of(
                createButton("\uD83D\uDD19 Назад", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getExpenseCategoriesKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(Arrays.asList(
                createButton("\uD83C\uDF4F Еда", "FOOD"),
                createButton("🚕 Транспорт", "TRANSPORT")
        ));
        rows.add(Arrays.asList(
                createButton("💊 Лечение", "HEALING"),
                createButton("🏠 Квартплата", "RENT")
        ));
        rows.add(Arrays.asList(
                createButton("\uD83D\uDC76 Ребенок", "CHILD"),
                createButton("\uD83D\uDCB8 Переводы", "CARD2CARD")
        ));
        rows.add(Arrays.asList(
                createButton("\uD83C\uDF77 Алкоголь", "ALCOHOL"),
                createButton("\uD83C\uDFC3 Спорт", "SPORT")
        ));
        rows.add(Arrays.asList(
                createButton("\uD83D\uDCE6 Вещи", "STUFF"),
                createButton("\uD83D\uDC56 Одежда", "CLOTH")
        ));
        rows.add(Arrays.asList(
                createButton("\uD83C\uDFAD Развлечение", "ENTERTAINMENT"),
                createButton("\uD83D\uDCB3 Кредитка", "CREDIT")
        ));
        rows.add(Arrays.asList(
                createButton("✨ Прочее", "ANOTHER"),
                createButton("🔙 Назад", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getCommentKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(
                createButton("Далее ➡\uFE0F", "SKIP")));
        rows.add(List.of(
                createButton("❌ Отмена", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getDateKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(List.of(
                createButton("Позавчера", "BEFORE_YESTERDAY")));
        rows.add(List.of(
                createButton("Вчера", "YESTERDAY")));
        rows.add(List.of(
                createButton("•Сегодня", "TODAY")));
        rows.add(List.of(
                createButton("❌ Отмена", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getCancelKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(
                createButton("❌ Отмена", "CANCEL")
        ));
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardButton createButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }
}

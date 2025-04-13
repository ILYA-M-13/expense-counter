package org.example.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExpenseCounterBot extends TelegramLongPollingBot {

   private final InlineKeyboard inlineKeyboard;
    private final Map<Long, UserState> userStates = new HashMap<>(); // chatId -> состояние
    private final Map<Long, String> tempCategory = new HashMap<>(); // chatId -> выбранная категория
    private final Map<Long, ExpenseData> tempExpenses = new HashMap<>();

    public ExpenseCounterBot(@Value("${bot.token}") String botToken, InlineKeyboard inlineKeyboard) {
        super(botToken);
        this.inlineKeyboard = inlineKeyboard;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Проверяем состояние пользователя
            UserState state = userStates.get(chatId);

           if (state == UserState.WAITING_FOR_AMOUNT) {
                try {
                    double amount = Double.parseDouble(messageText);
                    String category = tempCategory.get(chatId);

                    // Сохраняем расход в базу данных
                   // expenseService.saveExpense(chatId, amount, category);

                    // Сбрасываем состояние
                    userStates.remove(chatId);
                    tempCategory.remove(chatId);

                    SendMessage response = new SendMessage();
                    response.setChatId(chatId);
                   // response.setText(String.format("✅ Расход %.2f ₽ на %s сохранён", amount, getCategoryName(category)));
                   // execute(response);

                } catch (NumberFormatException e) {
                    SendMessage error = new SendMessage();
                    error.setChatId(chatId);
                    error.setText("❌ Неверный формат суммы. Введите число, например: 1500 или 99.90");
                  //  execute(error);
                }
            }

                if (messageText.equals("/start")) {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setReplyMarkup(inlineKeyboard.getFirstKeyboardMarkup());
                    message.setText("Выберите команду \uD83C\uDFA8");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }

        } else if (update.hasCallbackQuery()) {
            String call = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            switch (call) {
                case "1" -> {
                    EditMessageText editMessage = getEditMessage(
                            "Здесь можно узнать про Марка \uD83E\uDDF8",chatId,messageId);
                    editMessage.setReplyMarkup(inlineKeyboard.getInfoMarkMenuKeyboard());

                    try {
                        execute(editMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                case "2" -> {
                    EditMessageText editMessage = getEditMessage(
                            "\uD83D\uDCB2 Внесение расходов",chatId,messageId);
                    editMessage.setReplyMarkup(inlineKeyboard.getFinanceMenuKeyboard());
                    try {
                        execute(editMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "4" ->{
                    EditMessageText editMessage = getEditMessage(
                            "Выберите категорию трат ⬇\uFE0F",chatId,messageId);
                    editMessage.setReplyMarkup(inlineKeyboard.getExpenseCategoriesKeyboard());
                    try {
                        execute(editMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "6", "9" -> {
                    EditMessageText editMessage = getEditMessage(
                            "Выберите команду \uD83C\uDFA8",chatId,messageId);
                    editMessage.setReplyMarkup(inlineKeyboard.getFirstKeyboardMarkup());
                    try {
                        execute(editMessage);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "FOOD" -> {
                    try {
                        execute(setCategoryStage(
                                "FOOD",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "TRANSPORT" -> {
                    try {
                        execute(setCategoryStage(
                                "TRANSPORT",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "HEALING" -> {
                    try {
                        execute(setCategoryStage(
                                "HEALING",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "RENT" -> {
                    try {
                        execute(setCategoryStage(
                                "RENT",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CARD2CARD" -> {
                    try {
                        execute(setCategoryStage(
                                "CARD2CARD",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CHILD" -> {
                    try {
                        execute(setCategoryStage(
                                "CHILD",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "ALCOHOL" -> {
                    try {
                        execute(setCategoryStage(
                                "ALCOHOL",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "STUFF" -> {
                    try {
                        execute(setCategoryStage(
                                "STUFF",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CLOTH" -> {
                    try {
                        execute(setCategoryStage(
                                "CLOTH",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "ENTERTAINMENT" -> {
                    try {
                        execute(setCategoryStage(
                                "ENTERTAINMENT",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "CREDIT" -> {
                    try {
                        execute(setCategoryStage(
                                "CREDIT",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "ANOTHER" -> {
                    try {
                        execute(setCategoryStage(
                                "ANOTHER",
                                chatId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
                default -> {
                    try {
                        execute(getEditMessage("⚠\uFE0F В разработке...",chatId,messageId));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
    }

    private SendMessage setCategoryStage(String category, long chatId){
        ExpenseData data = new ExpenseData();
        data.setCategory(category);
        tempExpenses.put(chatId, data);
        userStates.put(chatId, UserState.WAITING_FOR_AMOUNT);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Введите сумму расхода:\n\rНапример 1700");
        message.setReplyMarkup(inlineKeyboard.getCancelKeyboard());
        return message;
    }

private EditMessageText getEditMessage(String message, long chatId, int messageId){
    EditMessageText editMessage = new EditMessageText();
    editMessage.setChatId(chatId);
    editMessage.setMessageId(messageId);
    editMessage.setText(message);
    return editMessage;
}

    @Override
    public String getBotUsername() {
        return "YourGrandsonInfoBot";
    }
}

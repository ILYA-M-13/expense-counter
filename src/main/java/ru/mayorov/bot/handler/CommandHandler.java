package ru.mayorov.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.model.Expenditure;
import ru.mayorov.service.BotService;
import ru.mayorov.service.WhitelistService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CommandHandler {
    private final MessageService messageService;
    private final KeyboardHandler inlineKeyboard;
    private final BotService service;
    private final UserStateManager userState;
    private final WhitelistService whitelistService;

    private void handleUserCallback(String call, long userId, int messageId) {
        if (userState.getState(userId) != null) {
            UserState state = userState.getState(userId);
            handleStateCallback(state, call, userId, messageId);

        } else {
            handleCallback(call, userId, messageId);
        }
    }

    private void handleAdminCallback(String call, long userId, int messageId) {

        if (call.equals("REJECT")) {
            messageService.sendEditMessage("Отмена", userId, messageId, null);
        } else {
            String[] strings = call.split("_");
            String id = strings[1];
            String name = strings[2];
            try {
                Long newUserId = Long.valueOf(id);
                String result = whitelistService.addToWhitelist(newUserId, name);
                whitelistService.notifyUser("Доступ предоставлен!",newUserId);
                messageService.sendEditMessage(result, userId, messageId, null);
            } catch (NumberFormatException ignore) {
                messageService.sendEditMessage("NumberFormatException", userId, messageId, null);
            }
        }
    }

    public void handleCallback(String call, long userId, int messageId) {
        switch (call) {
            case "STATISTICS", "CATEGORY", "TOSTAT" -> {
                String mes = service.getAllStatisticsByCategory(userId);
                messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuKeyboard());
            }
            case "BYYEAR" -> {
                int year = LocalDate.now().getYear();
                userState.setUserState(UserState.WAITING_FOR_PRESS_BTN_YEAR, userId);
                String mes = service.getStatisticsByCurrentYear(userId, year);
                messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(year));
            }
            case "DELETE" -> {
                String mes = service.getLastExp(userId);
                if (mes != null) {
                    messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getConfirmDeletionMenuKeyboard());
                } else {
                    mes = "Не найдено ни одной записи \uD83E\uDD37\u200D♂\uFE0F";
                    messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuKeyboard());
                }
            }
            case "YES" -> {
                String mes = service.delLastExp(userId);
                messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
            }
            case "EXPENSE" -> messageService.sendEditMessage(
                    "Выберите категорию трат ⬇\uFE0F", userId, messageId, inlineKeyboard.getExpenseCategoriesKeyboard());
            case "FOOD" -> setCategory("FOOD", messageId, userId);
            case "COMMUNICATION" -> setCategory("COMMUNICATION", messageId, userId);
            case "TRANSPORT" -> setCategory("TRANSPORT", messageId, userId);
            case "SPORT" -> setCategory("SPORT", messageId, userId);
            case "HEALING" -> setCategory("HEALING", messageId, userId);
            case "RENT" -> setCategory("RENT", messageId, userId);
            case "CARD2CARD" -> setCategory("CARD2CARD", messageId, userId);
            case "CHILD" -> setCategory("CHILD", messageId, userId);
            case "ALCOHOL" -> setCategory("ALCOHOL", messageId, userId);
            case "STUFF" -> setCategory("STUFF", messageId, userId);
            case "CLOTH" -> setCategory("CLOTH", messageId, userId);
            case "ENTERTAINMENT" -> setCategory("ENTERTAINMENT", messageId, userId);
            case "REPAIR" -> setCategory("REPAIR", messageId, userId);
            case "HOBBY" -> setCategory("HOBBY", messageId, userId);
            case "CREDIT" -> setCategory("CREDIT", messageId, userId);
            case "ANOTHER" -> setCategory("ANOTHER", messageId, userId);
            case "CANCEL" -> {
                userState.clearState(userId);
                messageService.sendEditMessage(
                        "Выберите команду \uD83C\uDFA8", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
            }
            default ->
                    messageService.sendEditMessage("⚠\uFE0F Неизвестная команда!", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());

        }
    }

    public void handleStateCallback(UserState state, String call, long userId, int messageId) {
        switch (call) {
            case "CANCEL" -> {
                userState.clearState(userId);
                messageService.sendEditMessage(
                        "Выберите команду \uD83C\uDFA8", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
            }
            case "CATEGORY" -> {
                userState.clearState(userId);
                String mes = service.getAllStatisticsByCategory(userId);
                messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuKeyboard());
            }
            default -> {
                if (state == UserState.WAITING_FOR_DATE) {
                    setDate(call, userId, messageId);
                }
                if (state == UserState.WAITING_FOR_PRESS_BTN_YEAR) {
                    String year = call.substring(call.indexOf("_") + 1);
                    if (!year.equals("ignore")) {

                        int yearInt = 0;
                        try {
                            yearInt = Integer.parseInt(year);
                        } catch (NumberFormatException e) {
                            yearInt = LocalDate.now().getYear();
                        }

                        String mes = service.getStatisticsByCurrentYear(userId, yearInt);
                        messageService.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(yearInt));
                    }
                }
            }
        }
    }

    private void setCategory(String category, int messageId, long userId) {
        ExpenseCounterDTO data = new ExpenseCounterDTO();
        data.setCategory(category);
        data.setUserUID(userId);
        userState.setUserState(UserState.WAITING_FOR_AMOUNT, userId);
        userState.setDTO(data, userId);
        messageService.addTempMessageId(messageId, userId);
        messageService.sendEditMessage("Введите сумму ❗(◕‿◕)❗:\n\rНапример 1700",
                userId, messageId, inlineKeyboard.getCancelKeyboard());

    }

    private void createNewExpense(long userId, int messageId) {
        if (userState.getDTO(userId) != null) {
            ExpenseCounterDTO dto = userState.getDTO(userId);
            Expenditure exp = service.addExpense(dto);
            String message = exp != null ? "Запись создана ✅" : "Неудачная попытка записи ❌\r\nПопробуйте позже!";

            messageService.sendEditMessage(message + "\r\nВыберите команду \uD83C\uDFA8",
                    userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
        } else {
            messageService.sendMessage("Выберите команду \uD83C\uDFA8",
                    userId, inlineKeyboard.getFirstKeyboardMarkup());

        }
        messageService.deleteTempMessages(userId);
        userState.clearExpenseCounterDTO(userId);
        userState.clearState(userId);
    }

    private void amountInputProcessing(String messageText, long userId, int messageId, long chatId) {
        double amount = 0;
        try {
            amount = Double.parseDouble(messageText);
        } catch (NumberFormatException ignored) {
        }
        messageService.addTempMessageId(messageId, chatId);
        if (amount > 1)
            setAmount(amount, userId);
    }

    public void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        int messageId = update.getMessage().getMessageId();
        long chatId = update.getMessage().getChatId();
        long userId = update.getMessage().getFrom().getId();
        String userName = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        if (userState.getState(userId) != null) {
            UserState state = userState.getState(userId);

            if (state == UserState.WAITING_FOR_AMOUNT) {
                amountInputProcessing(messageText, userId, messageId, chatId);
            }
        }
        if (messageText.equals("/start")) {
            if (whitelistService.isAdmin(userId) || whitelistService.isAllowed(userId)) {
                userState.clearState(userId);
                messageService.sendMessage("Выберите команду \uD83C\uDFA8", userId, inlineKeyboard.getFirstKeyboardMarkup());

            } else {
                messageService.sendMessage("⛔ Дождитесь сообщения о доступе!", userId, null);
                String message = String.format(
                        "Новый пользователь:\nID: %d\nUsername: @%s\nFirstName: %s\nLastName: %s\nДобавить в белый список?",
                        userId, userName, firstName, lastName
                );
                whitelistService.notifyAdmin(message, userId, userName);
            }

        }
    }

    public void handleCallbackQuery(Update update) {
        String call = update.getCallbackQuery().getData();
        long userId = update.getCallbackQuery().getFrom().getId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        if (whitelistService.isAdmin(userId)) {
            if (call.startsWith("APPROVE_") || call.startsWith("REJECT")) {
                handleAdminCallback(call, userId, messageId);
            } else {
                handleUserCallback(call, userId, messageId);
            }


        } else if (whitelistService.isAllowed(userId)) {
            handleUserCallback(call, userId, messageId);
        }

    }


    private void setAmount(double amount, long userId) {
        ExpenseCounterDTO data = userState.getDTO(userId);
        userState.setUserState(UserState.WAITING_FOR_DATE, userId);
        data.setExpend(amount);
        messageService.sendMessage("✅Расходы записаны\r\n\uD83D\uDCC5Введите дату:", userId, inlineKeyboard.getDateKeyboard());

    }

    private void setDate(String call, long userId, int messageId) {
        ExpenseCounterDTO data = userState.getDTO(userId);

        LocalDate date = switch (call) {
            case "BEFORE_YESTERDAY" -> LocalDate.now().minusDays(2);
            case "YESTERDAY" -> LocalDate.now().minusDays(1);
            case "TODAY" -> LocalDate.now();
            default -> LocalDate.now();
        };
        data.setDate(date);

        createNewExpense(userId, messageId);

    }
}

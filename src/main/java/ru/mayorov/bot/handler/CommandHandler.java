package ru.mayorov.bot.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.bot.DTO.UserState;
import ru.mayorov.model.Expenditure;
import ru.mayorov.service.BotService;

import java.time.LocalDate;

@Component
public class CommandHandler {
    private final MessageSender messageSender;
    private final KeyboardHandler inlineKeyboard;
    private final BotService service;
    private final UserStateManager userState;

    public CommandHandler(MessageSender messageSender,
                          KeyboardHandler inlineKeyboard, BotService service, UserStateManager userState) {
        this.messageSender = messageSender;
        this.inlineKeyboard = inlineKeyboard;
        this.service = service;
        this.userState = userState;
    }


    public void handleCallback(String call, long userId, int messageId){
        switch (call) {
          case "STATISTICS", "CATEGORY" ,"TOSTAT" ->{
                String mes = service.getAllStatisticsByCategory(userId);
                messageSender.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuKeyboard());
            }
            case "BYYEAR" ->{
                int year = LocalDate.now().getYear();
                userState.setUserState(UserState.WAITING_FOR_PRESS_BTN_YEAR,userId);
                String mes = service.getStatisticsByCurrentYear(userId,year);
                messageSender.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(year));
            }
            case "EXPENSE" -> messageSender.sendEditMessage(
                    "Выберите категорию трат ⬇\uFE0F", userId, messageId, inlineKeyboard.getExpenseCategoriesKeyboard());
            case "FOOD" -> setCategory("FOOD",  messageId, userId);
            case "COMMUNICATION" -> setCategory("COMMUNICATION",  messageId, userId);
            case "TRANSPORT" -> setCategory("TRANSPORT",  messageId, userId);
            case "SPORT" -> setCategory("SPORT",  messageId, userId);
            case "HEALING" -> setCategory("HEALING",  messageId, userId);
            case "RENT" -> setCategory("RENT", messageId, userId);
            case "CARD2CARD" -> setCategory("CARD2CARD", messageId, userId);
            case "CHILD" -> setCategory("CHILD",  messageId, userId);
            case "ALCOHOL" -> setCategory("ALCOHOL",  messageId, userId);
            case "STUFF" -> setCategory("STUFF",  messageId, userId);
            case "CLOTH" -> setCategory("CLOTH",  messageId, userId);
            case "ENTERTAINMENT" -> setCategory("ENTERTAINMENT",  messageId, userId);
            case "CREDIT" -> setCategory("CREDIT",  messageId, userId);
            case "ANOTHER" -> setCategory("ANOTHER",  messageId, userId);
            case "CANCEL" -> {
                userState.clearState(userId);
                messageSender.sendEditMessage(
                        "Выберите команду \uD83C\uDFA8", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
            }
            default ->
                    messageSender.sendEditMessage("⚠\uFE0F Неизвестная команда!", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());

        }
    }

    public void handleStateCallback(UserState state, String call, long userId, int messageId){
        switch (call){
            case "CANCEL" ->{
                userState.clearState(userId);
                messageSender.sendEditMessage(
                        "Выберите команду \uD83C\uDFA8", userId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
            }
            case "CATEGORY" -> {
                userState.clearState(userId);
                String mes = service.getAllStatisticsByCategory(userId);
                messageSender.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuKeyboard());
            }
            default -> {
                if (state == UserState.WAITING_FOR_DATE) {
                    setDate(call, userId, messageId);
                }if (state == UserState.WAITING_FOR_PRESS_BTN_YEAR){
                    String year = call.substring(call.indexOf("_") + 1);
                    int yearInt = 0;
                    try{
                        yearInt = Integer.parseInt(year);
                    } catch (NumberFormatException e) {
                        yearInt = LocalDate.now().getYear();
                    }
                    String mes = service.getStatisticsByCurrentYear(userId,yearInt);
                    messageSender.sendEditMessage(mes, userId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(yearInt));
                }
            }
        }
    }

    private void setCategory(String category, int messageId, long userId) {
        ExpenseCounterDTO data = new ExpenseCounterDTO();
        data.setCategory(category);
        data.setUserUID(userId);
        userState.setUserState(UserState.WAITING_FOR_AMOUNT,userId);
        userState.setDTO(data,userId);

        messageSender.sendEditMessage("❗❗❗Введите сумму расхода:\n\rНапример 1700",
                userId, messageId, inlineKeyboard.getCancelKeyboard());

    }

    private void createNewExpense(long userId, int messageId) {
        if (userState.getDTO(userId) != null) {
            ExpenseCounterDTO dto = userState.getDTO(userId);

            Expenditure exp = service.addExpense(dto);
            String message = exp != null ? "Запись создана ✅" : "Неудачная попытка записи ❌\r\nПопробуйте позже!";

            messageSender.sendEditMessage(message+"\r\nВыберите команду \uD83C\uDFA8",
                    userId,messageId, inlineKeyboard.getFirstKeyboardMarkup());
        } else {
            messageSender.sendMessage("Выберите команду \uD83C\uDFA8",
                    userId, inlineKeyboard.getFirstKeyboardMarkup());

        }
        userState.clearExpenseCounterDTO(userId);
        userState.clearState(userId);
    }

    private void amountInputProcessing(String messageText, long userId) {
        double amount = 0;
        try {
            amount = Double.parseDouble(messageText);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        if (amount < 1) {
            messageSender.sendMessage("❌ Неверный формат суммы. Введите число, например: 1500 или 99.90", userId, null);
        } else {
            setAmount(amount, userId);
        }
    }

    public void handleTextMessage(Update update) {
        String messageText = update.getMessage().getText();
        long userId = update.getMessage().getFrom().getId();

        if (userState.getState(userId) != null) {
            UserState state = userState.getState(userId);

            if (state == UserState.WAITING_FOR_AMOUNT) {
                amountInputProcessing(messageText, userId);
            }
        }
        if (messageText.equals("/start")) {
            userState.clearState(userId);
            messageSender.sendMessage("Выберите команду \uD83C\uDFA8", userId, inlineKeyboard.getFirstKeyboardMarkup());
        }
    }

    private void setAmount(double amount, long userId) {
        ExpenseCounterDTO data = userState.getDTO(userId);
        userState.setUserState(UserState.WAITING_FOR_DATE,userId);
        data.setExpend(amount);
        messageSender.sendMessage("✅Расходы записаны\r\n\uD83D\uDCC5Введите дату:",userId,inlineKeyboard.getDateKeyboard());

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

        createNewExpense(userId ,messageId);

    }
}

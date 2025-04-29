package ru.mayorov.bot;

import ru.mayorov.bot.handler.KeyboardHandler;
import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.bot.DTO.UserState;
import ru.mayorov.model.Expenditure;
import ru.mayorov.service.BotService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExpenseCounterBot extends TelegramLongPollingBot {

    private final BotService service;
    private final KeyboardHandler inlineKeyboard;
    private final Map<Long, ExpenseCounterDTO> tempData = new HashMap<>();
    private final Map<Long, Integer> yearState= new HashMap<>();

    public ExpenseCounterBot(@Value("${telegram.bot.token}") String botToken,
                             DefaultBotOptions options, BotService service,
                             KeyboardHandler inlineKeyboard) {
        super(options, botToken);
        this.service = service;
        this.inlineKeyboard = inlineKeyboard;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            //long chatId = update.getMessage().getChatId();
            long userId = update.getMessage().getFrom().getId();
            String userName = update.getMessage().getFrom().getUserName();
            String lastName = update.getMessage().getFrom().getLastName();
            String firstName = update.getMessage().getFrom().getFirstName();


            if (tempData.get(userId) != null && tempData.get(userId).getState() != null) {
                UserState state = tempData.get(userId).getState();

                switch (state) {
                    case WAITING_FOR_AMOUNT -> amountInputProcessing(messageText, userId);
                    case WAITING_FOR_COMMENT -> createNewExpense(userId, messageText);
                }
            }
            if (messageText.equals("/start")) {
                tempData.clear();
                sendMessage("Выберите команду \uD83C\uDFA8", userId, inlineKeyboard.getFirstKeyboardMarkup());
            }

        } else if (update.hasCallbackQuery()) {
            String call = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long userId = update.getCallbackQuery().getFrom().getId();

            System.out.println(userId +" userId");
            if (tempData.get(chatId) != null && tempData.get(chatId).getState() != null) {
                UserState state = tempData.get(chatId).getState();

               if(call.equals("SKIP")){
                   createNewExpense(chatId, null);
               }
               else if(call.equals("CANCEL")){
                   tempData.clear();
                   yearState.clear();
                   sendEditMessage(
                           "Выберите команду \uD83C\uDFA8", chatId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
               }
                else if (state == UserState.WAITING_FOR_DATE) {
                    setDate(call, chatId, messageId);
                }
            }
            else if(yearState.get(userId) != null){
                if(call.equals("CATEGORY")){
                    yearState.clear();
                    tempData.clear();
                    String mes = service.getAllStatisticsByCategory(userId);
                    sendEditMessage(mes, chatId, messageId, inlineKeyboard.getStatMenuKeyboard());

                }
               if(call.equals("CANCEL")){
                   yearState.clear();
                   tempData.clear();
                   sendEditMessage(
                           "Выберите команду \uD83C\uDFA8", chatId, messageId, inlineKeyboard.getFirstKeyboardMarkup());

               }
                if(call.startsWith("BYYEAR")){
                    String year = call.substring(call.indexOf("_") + 1);
                    int yearInt = 0;
                   try{
                     yearInt = Integer.parseInt(year);
                   } catch (NumberFormatException e) {
                       System.out.println("не удалось парсить год!");
                       yearInt = LocalDate.now().getYear();
                   }
                    String mes = service.getStatisticsByCurrentYear(userId,yearInt);
                    sendEditMessage(mes, chatId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(yearInt));

                }
            }
            else {
                switch (call) {
                    case "MARK" -> sendEditMessage(
                            "Здесь можно узнать про Марка \uD83E\uDDF8", chatId, messageId, inlineKeyboard.getInfoMarkMenuKeyboard());
                    case "BUDGET" -> sendEditMessage(
                            "\uD83D\uDCB2 Внесение расходов", chatId, messageId, inlineKeyboard.getFinanceMenuKeyboard());
                    case "STATISTICS", "CATEGORY" ,"TOSTAT" ->{
                        String mes = service.getAllStatisticsByCategory(userId);
                        sendEditMessage(mes, chatId, messageId, inlineKeyboard.getStatMenuKeyboard());
                    }
                    case "BYYEAR" ->{
                        int year = LocalDate.now().getYear();
                        yearState.put(userId,year);
                        String mes = service.getStatisticsByCurrentYear(userId,year);
                        sendEditMessage(mes, chatId, messageId, inlineKeyboard.getStatMenuByYearKeyboard(year));
                    }
                    case "EXPENSE" -> sendEditMessage(
                            "Выберите категорию трат ⬇\uFE0F", chatId, messageId, inlineKeyboard.getExpenseCategoriesKeyboard());
                    case "FOOD" -> setCategory("FOOD", chatId, messageId, userId);
                    case "TRANSPORT" -> setCategory("TRANSPORT", chatId, messageId, userId);
                    case "SPORT" -> setCategory("SPORT", chatId, messageId, userId);
                    case "HEALING" -> setCategory("HEALING", chatId, messageId, userId);
                    case "RENT" -> setCategory("RENT", chatId, messageId, userId);
                    case "CARD2CARD" -> setCategory("CARD2CARD", chatId, messageId, userId);
                    case "CHILD" -> setCategory("CHILD", chatId, messageId, userId);
                    case "ALCOHOL" -> setCategory("ALCOHOL", chatId, messageId, userId);
                    case "STUFF" -> setCategory("STUFF", chatId, messageId, userId);
                    case "CLOTH" -> setCategory("CLOTH", chatId, messageId, userId);
                    case "ENTERTAINMENT" -> setCategory("ENTERTAINMENT", chatId, messageId, userId);
                    case "CREDIT" -> setCategory("CREDIT", chatId, messageId, userId);
                    case "ANOTHER" -> setCategory("ANOTHER", chatId, messageId, userId);
                    case "CANCEL" -> {
                        yearState.clear();
                        tempData.clear();
                        sendEditMessage(
                                "Выберите команду \uD83C\uDFA8", chatId, messageId, inlineKeyboard.getFirstKeyboardMarkup());
                    }
                    default ->
                            sendEditMessage("⚠\uFE0F В разработке...", chatId, messageId, inlineKeyboard.getFirstKeyboardMarkup());

                }
            }

        }
    }

    private void createNewExpense(long chatId, String messageText) {
        if (tempData.get(chatId) != null) {
            ExpenseCounterDTO dto = tempData.get(chatId);
            if (messageText != null) {
                dto.setComment(messageText);
            }
            Expenditure exp = service.addExpense(dto);
            String message = exp != null ? "Запись создана ✅" : "Неудачная попытка записи ❌\r\nПопробуйте позже!";
            sendMessage(message, chatId, null);
            sendMessage("Выберите команду \uD83C\uDFA8",
                    chatId, inlineKeyboard.getFirstKeyboardMarkup());
        } else {
            sendMessage("Выберите команду \uD83C\uDFA8",
                    chatId, inlineKeyboard.getFirstKeyboardMarkup());

        }
        tempData.clear();
    }


    private void setDate(String call, long chatId, int messageId) {
        ExpenseCounterDTO data = tempData.get(chatId);
        data.setState(UserState.WAITING_FOR_COMMENT);

        LocalDate date = switch (call) {
            case "BEFORE_YESTERDAY" -> LocalDate.now().minusDays(2);
            case "YESTERDAY" -> LocalDate.now().minusDays(1);
            case "TODAY" -> LocalDate.now();
            default -> LocalDate.now();
        };
        data.setDate(date);

        sendEditMessage("\uD83D\uDD39 Дата записана!\r\nДобавьте комментарий или нажмите Далее:",
                chatId, messageId, inlineKeyboard.getCommentKeyboard());

    }

    private void amountInputProcessing(String messageText, long chatId) {
        double amount = 0;
        try {
            amount = Double.parseDouble(messageText);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        if (amount < 1) {
            sendMessage("❌ Неверный формат суммы. Введите число, например: 1500 или 99.90", chatId, null);
        } else {
            setAmount(amount, chatId);
        }
    }

    private void setCategory(String category, long chatId, int messageId, long userId) {
        ExpenseCounterDTO data = new ExpenseCounterDTO();
        data.setCategory(category);
        data.setUserUID(userId);
        data.setState(UserState.WAITING_FOR_AMOUNT);
        tempData.put(chatId, data);

        sendEditMessage("❗❗❗Введите сумму расхода:\n\rНапример 1700",
                chatId, messageId, inlineKeyboard.getCancelKeyboard());

    }

    private void setAmount(double amount, long chatId) {
        ExpenseCounterDTO data = tempData.get(chatId);
        data.setState(UserState.WAITING_FOR_DATE);
        data.setExpend(amount);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("✅Расходы записаны\r\n\uD83D\uDCC5Введите дату:");
        message.setReplyMarkup(inlineKeyboard.getDateKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String message, long chatId, InlineKeyboardMarkup keyboard) {
        SendMessage mes = new SendMessage();
        mes.setChatId(chatId);
        if (keyboard != null) {
            mes.setReplyMarkup(keyboard);
        }
        mes.setText(message);
        try {
            execute(mes);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEditMessage(String message, long chatId, int messageId, InlineKeyboardMarkup keyboard) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(message);
        editMessage.setReplyMarkup(keyboard);
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "YourGrandsonInfoBot";
    }
}

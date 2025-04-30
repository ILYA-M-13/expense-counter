package ru.mayorov.bot;

import ru.mayorov.bot.handler.CommandHandler;
import ru.mayorov.bot.DTO.UserState;
import ru.mayorov.bot.handler.UserStateManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

@Component
public class ExpenseCounterBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String botName;
    private final UserStateManager userState;
    private final CommandHandler commandHandler;

    public ExpenseCounterBot(@Value("${telegram.bot.token}") String botToken,
                             DefaultBotOptions options,
                             UserStateManager userState,
                             CommandHandler commandHandler) {
        super(options, botToken);
        this.userState = userState;
        this.commandHandler = commandHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

           commandHandler.handleTextMessage(update);

        } else if (update.hasCallbackQuery()) {

            String call = update.getCallbackQuery().getData();
            long userId = update.getCallbackQuery().getFrom().getId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            if (userState.getState(userId) != null) {
                UserState state = userState.getState(userId);
                commandHandler.handleStateCallback(state, call, userId, messageId);
            } else {
                commandHandler.handleCallback(call, userId, messageId);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}

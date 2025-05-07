package ru.mayorov.bot;

import ru.mayorov.bot.handler.CommandHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ExpenseCounterBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.name}")
    private String botName;
    private final CommandHandler commandHandler;

    public ExpenseCounterBot(@Value("${telegram.bot.token}") String botToken,
                             DefaultBotOptions options,
                             CommandHandler commandHandler) {
        super(options, botToken);
        this.commandHandler = commandHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandHandler.handleTextMessage(update);

        } else if (update.hasCallbackQuery()) {
            commandHandler.handleCallbackQuery(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}

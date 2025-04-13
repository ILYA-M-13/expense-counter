package org.example.config;

import org.example.bot.ExpenseCounterBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class ExpenseCounterBotConf {

    @Bean
    public TelegramBotsApi telegramBotsApi(ExpenseCounterBot expenseCounterBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(expenseCounterBot);
        return api;
    }
}

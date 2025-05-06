package ru.mayorov.bot.handler;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramMessageSender implements MessageSender {
    private final TelegramLongPollingBot bot;

    public TelegramMessageSender(@Lazy TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String message, long userId, InlineKeyboardMarkup keyboard) {
        SendMessage mes = new SendMessage();
        mes.setChatId(userId);
        if (keyboard != null) {
            mes.setReplyMarkup(keyboard);
        }
        mes.setText(message);
        try {
            bot.execute(mes);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEditMessage(String message, long userId, int messageId, InlineKeyboardMarkup keyboard) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(userId);
        editMessage.setMessageId(messageId);
        editMessage.setText(message);
        editMessage.setReplyMarkup(keyboard);
        try {
            bot.execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteMessage(int messageId,long chatId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(messageId);
        deleteMessage.setChatId(chatId);
        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

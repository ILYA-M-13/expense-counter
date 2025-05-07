package ru.mayorov.bot.handler;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageService {
    void sendMessage(String message, long userId, InlineKeyboardMarkup keyboard);
    void sendEditMessage(String message, long userId, int messageId, InlineKeyboardMarkup keyboard);
    void deleteTempMessages(Long chatId);
    void addTempMessageId(int messageId, long chatId);
}

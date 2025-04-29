package ru.mayorov.bot.handler;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface MessageSender {
    void sendMessage(String message, long userId, InlineKeyboardMarkup keyboard);
    void sendEditMessage(String message, long userId, int messageId, InlineKeyboardMarkup keyboard);
}

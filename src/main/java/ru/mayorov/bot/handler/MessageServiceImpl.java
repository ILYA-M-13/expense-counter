package ru.mayorov.bot.handler;

import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageServiceImpl implements MessageService {

    private final TelegramLongPollingBot bot;

    private final Map<Long, List<Integer>> chatTempMessages = new ConcurrentHashMap<>();

    public MessageServiceImpl(@Lazy TelegramLongPollingBot bot) {
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
        if(keyboard != null){
            editMessage.setReplyMarkup(keyboard);
        }
        try {
            bot.execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTempMessages(Long chatId) {
        List<Integer> messageIds = getTempMessageIds(chatId);
        for(Integer messageId : messageIds){
            DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), messageId);
            try {
                bot.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
        clearTempMessage(chatId);
    }

    @Override
    public void addTempMessageId(int messageId, long chatId) {
        chatTempMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).add(messageId);
    }

    public void clearTempMessage(long chatId) {
        chatTempMessages.remove(chatId);
    }

    public List<Integer> getTempMessageIds(Long chatId) {
        return chatTempMessages.getOrDefault(chatId, new ArrayList<>());
    }
}

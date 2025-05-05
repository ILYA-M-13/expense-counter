package ru.mayorov;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.mayorov.bot.DTO.UserState;
import ru.mayorov.bot.handler.CommandHandler;
import ru.mayorov.bot.handler.KeyboardHandler;
import ru.mayorov.bot.handler.MessageSender;
import ru.mayorov.bot.handler.UserStateManager;
import ru.mayorov.service.BotService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandHandlerTest {

    @InjectMocks
    private CommandHandler commandHandler;

    @Mock
    private MessageSender messageSender;
    @Mock
    private BotService service;
    @Mock
    private UserStateManager userState;
    @Mock
    private KeyboardHandler inlineKeyboard;

    private final long testUserId = 123L;
    private final int testMessageId = 456;

    @Test
    void handleCallback_ShouldHandleStatisticsCommands() {
        // Arrange
        String expectedResponse = "Статистика по категориям";
        when(service.getAllStatisticsByCategory(anyLong())).thenReturn(expectedResponse);
        when(inlineKeyboard.getStatMenuKeyboard()).thenReturn(new InlineKeyboardMarkup());

        commandHandler.handleCallback("STATISTICS", testUserId, testMessageId);

        verify(service).getAllStatisticsByCategory(testUserId);
        verify(messageSender).sendEditMessage(
                eq(expectedResponse),
                eq(testUserId),
                eq(testMessageId),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    void handleCallback_ShouldHandleYearlyStatistics() {
        // Arrange
        int currentYear = LocalDate.now().getYear();
        String expectedResponse = "Статистика за год";
        when(service.getStatisticsByCurrentYear(anyLong(), anyInt())).thenReturn(expectedResponse);
        when(inlineKeyboard.getStatMenuByYearKeyboard(anyInt())).thenReturn(new InlineKeyboardMarkup());
        commandHandler.handleCallback("BYYEAR", testUserId, testMessageId);
        verify(userState).setUserState(UserState.WAITING_FOR_PRESS_BTN_YEAR, testUserId);
        verify(service).getStatisticsByCurrentYear(testUserId, currentYear);
        verify(messageSender).sendEditMessage(
                eq(expectedResponse),
                eq(testUserId),
                eq(testMessageId),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    void handleCallback_ShouldHandleExpenseCommand() {
        when(inlineKeyboard.getExpenseCategoriesKeyboard()).thenReturn(new InlineKeyboardMarkup());
        commandHandler.handleCallback("EXPENSE", testUserId, testMessageId);
        verify(messageSender).sendEditMessage(
                eq("Выберите категорию трат ⬇️"),
                eq(testUserId),
                eq(testMessageId),
                any(InlineKeyboardMarkup.class)
        );
    }

    @Test
    void handleCallback_ShouldHandleUnknownCommand() {
        // Arrange
        when(inlineKeyboard.getFirstKeyboardMarkup()).thenReturn(new InlineKeyboardMarkup());

        // Act
        commandHandler.handleCallback("UNKNOWN_COMMAND", testUserId, testMessageId);

        // Assert
        verify(messageSender).sendEditMessage(
                eq("⚠️ Неизвестная команда!"),
                eq(testUserId),
                eq(testMessageId),
                any(InlineKeyboardMarkup.class)
        );
    }
}
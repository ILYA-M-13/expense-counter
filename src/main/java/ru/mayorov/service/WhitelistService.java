package ru.mayorov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mayorov.bot.handler.KeyboardHandler;
import ru.mayorov.bot.handler.MessageService;
import ru.mayorov.model.User;
import ru.mayorov.repository.UserRepository;

@Service
public class WhitelistService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private KeyboardHandler keyboardHandler;

    @Value("${telegram.bot.whitelist.admin-ids}")
    private long admin;

    public boolean isAllowed(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

    public boolean isAdmin(Long telegramId){
        return admin == telegramId;
    }

    public void notifyAdmin(String messageToAdmin, Long userId, String userName){
        messageService.sendMessage(messageToAdmin,admin, keyboardHandler.getAKB(userId,userName));
    }

    public String addToWhitelist(Long telegramId, String username) {
        if (userRepository.existsByTelegramId(telegramId)) {
           return "Юзер " + username + " уже в белом списке!";
        }

        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        return  userRepository.save(user).toString();

    }
}

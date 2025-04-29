package ru.mayorov.bot.handler;

import org.springframework.stereotype.Component;
import ru.mayorov.bot.DTO.ExpenseCounterDTO;
import ru.mayorov.bot.DTO.UserState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStateManager {
    private final Map<Long, ExpenseCounterDTO> dtoMap = new ConcurrentHashMap<>();
    private final Map<Long, UserState> stateMap = new ConcurrentHashMap<>();

    public UserState getState(long userId){
       return stateMap.get(userId);
    }

    public ExpenseCounterDTO getDTO(long userId){
        return dtoMap.get(userId);
    }

    public void setDTO(ExpenseCounterDTO dto, long userId){
        dtoMap.put(userId,dto);
    }

    public void setUserState(UserState userState, long userId){
        stateMap.put(userId,userState);
    }

    public void clearState(long userId){
        stateMap.remove(userId);
    }

    public void clearExpenseCounterDTO(long userId){
        dtoMap.remove(userId);
    }

}

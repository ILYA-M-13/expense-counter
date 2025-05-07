package ru.mayorov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mayorov.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByTelegramId(Long telegramId);
}

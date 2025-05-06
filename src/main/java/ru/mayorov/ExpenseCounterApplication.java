package ru.mayorov;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExpenseCounterApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );

        String[] requiredVars = {"SQL_USER", "SQL_PASS", "BOT_TOKEN", "BOT_NAME", "HOST", "PORT", "TYPE", "USERNAME", "PASSWORD"};
        for (String var : requiredVars) {
            if (dotenv.get(var) == null) {
                throw new IllegalStateException("Отсутствуют переменные окружения: " + var);
            }
        }
        SpringApplication.run(ExpenseCounterApplication.class, args);
    }
}

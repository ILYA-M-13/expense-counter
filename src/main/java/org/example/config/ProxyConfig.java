package org.example.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.example.bot.ExpenseCounterBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Profile("proxyON")
public class ProxyConfig {

    @Value("${telegram.bot.proxy.type}")
    private String type;

    @Value("${telegram.bot.proxy.host}")
    private String host;

    @Value("${telegram.bot.proxy.port}")
    private int port;

    @Value("${telegram.bot.proxy.username}")
    private String user;

    @Value("${telegram.bot.proxy.password}")
    private String password;


    @Bean
    public DefaultBotOptions botOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyType(DefaultBotOptions.ProxyType.valueOf(type.toUpperCase()));
        options.setProxyHost(host); // IP или домен прокси
        options.setProxyPort(port);                 // Порт прокси (обычно 3128, 8080)

        // Настройка аутентификации на прокси
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(options.getProxyHost(), options.getProxyPort()),
                new UsernamePasswordCredentials(user, password)
        );

        HttpClientContext httpContext = new HttpClientContext();
        httpContext.setCredentialsProvider(credsProvider);
        httpContext.setAuthCache(new BasicAuthCache());

        options.setHttpContext(httpContext); // Передаём контекст с логином/паролем

        return options;
    }



    @Bean
    public TelegramBotsApi telegramBotsApi(ExpenseCounterBot bot) throws TelegramApiException {
        var api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(bot);
        return api;
    }
}

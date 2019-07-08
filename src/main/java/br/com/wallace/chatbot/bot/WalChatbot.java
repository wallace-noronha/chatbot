package br.com.wallace.chatbot.bot;

import br.com.wallace.chatbot.controller.BotController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@EnableScheduling
public class WalChatbot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalChatbot.class);

    @Autowired
    private BotController botController;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String botUsername;

    private BotSession botSession = null;

    public String getBotUsername() {
        return this.botUsername;
    }

    public String getBotToken() {
        return this.token;
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage()){
            Message message = update.getMessage();
            LOGGER.info("MESSAGE SENDED: " + message.getText());
            SendMessage response = botController.controller(message);
            response.setChatId(message.getChatId());
            try {
                execute(response);
                LOGGER.info("Id do chat: {} responsta enviada {}",response.getChatId(), response.getText());
            }catch (TelegramApiException e){
                LOGGER.error("Failed to send command");
            }
        }
    }

    @PostConstruct
    public void registerBot(){
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botSession = botsApi.registerBot(new WalChatbot());

            LOGGER.info("username: {}, token: {}", this.botUsername, this.token);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop(){
        if (botSession != null) {
            botSession.stop();
        }
    }

    @Scheduled(cron = "0 0 0/8 * * *")
    private void task(){
        SendMessage message = new SendMessage();
        message.setText("Hora de acordar!!!!!!!");
        message.setChatId(873520134L);
        LOGGER.info("Mensage send: " + message.getText());

        try {
            execute(message);
            execute(message);
            execute(message);
            execute(message);
            execute(message);
        }catch (TelegramApiException e){
            LOGGER.error("ERROR IN SEND SCHEDULE MESSAGE");
        }
    }
}

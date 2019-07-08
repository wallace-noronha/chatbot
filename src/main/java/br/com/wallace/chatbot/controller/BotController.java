package br.com.wallace.chatbot.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotController {

    public SendMessage controller(Message message){
        SendMessage sendMessage = new SendMessage();

        if(message.isCommand()){
            sendMessage = new RestTemplate().postForObject("http://localhost:8080" + message.getText(),message,SendMessage.class);
        } else {
            sendMessage.setText("I dont understend you, try again!!!!");
        }
        return sendMessage;
    }

}

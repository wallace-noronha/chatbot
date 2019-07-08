package br.com.wallace.chatbot.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BotTextController {

    @PostMapping("hello")
    public SendMessage hello(@RequestBody Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Hello " + message.getChat().getFirstName() + " how are you? ");
        return sendMessage;
    }

    @PostMapping("start")
    public SendMessage start(@RequestBody Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Welcome " + message.getChat().getFirstName());
        return sendMessage;
    }

    @PostMapping("elastichealth")
    public SendMessage elasticsearchHealth(@RequestBody Message message) throws MalformedURLException, URISyntaxException {
        SendMessage sendMessage = new SendMessage();
        URL uri = new URL(" http://localhost:9200/_cat/health?format=json");
        String string = new RestTemplate().getForObject(uri.toURI(), String.class);
        JSONArray jsonArray = new JSONArray(string);
        for(int i = 0; jsonArray.length() > i ; i++){
            JSONObject json = jsonArray.getJSONObject(i);
            sendMessage.setText("Nome do cluster: " +  json.getString("cluster") +
                    " Status do cluster: " + json.getString("status"));
        }
        return sendMessage;
    }

    @PostMapping("elasticindex")
    public SendMessage elasticsearchIndex(@RequestBody Message message) throws MalformedURLException, URISyntaxException {
        SendMessage sendMessage = new SendMessage();
        URL url = new URL("http://localhost:9200/_cat/indices?format=json");
        String string = new RestTemplate().getForObject(url.toURI(), String.class);
        JSONArray jsonArray = new JSONArray(string);
        List<String> lista = new ArrayList<String>();
        for(int i = 0; jsonArray.length() > i ; i++){
            JSONObject json = jsonArray.getJSONObject(i);
            lista.add("Nome do indice: " +  json.getString("index") +
                    " Status do indice: " + json.getString("health") + "\r\n");
        }

        sendMessage.setText(lista.toString().replace("[","").replace("]","").replace(",",""));
        return sendMessage;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 *
 * @author Kevin Druet
 */
public class bot extends TelegramLongPollingBot{
    private int id_bot;
    private String alias;
    private String token;
    private boolean estado;

    public bot() {
    }

    public bot(String alias, String token, boolean estado) {
        this.alias = alias;
        this.token = token;
        this.estado = estado;
    }

    public int getId_bot() {
        return id_bot;
    }

    public void setId_bot(int id_bot) {
        this.id_bot = id_bot;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());
    }

    @Override
    public String getBotUsername() {
        return this.alias;
    }
    
    public void sendMag(String asunto,String chat_id) {
        SendMessage envio=new SendMessage();
        envio.enableMarkdown(true);
        envio.setChatId(chat_id);
        envio.setText(asunto);
        System.out.println(envio.enableNotification().getText());
        try{
            sendMessage(envio);
            envio.enableMarkdown(false);
        } catch (TelegramApiException ex) {
        }
    }
   
}

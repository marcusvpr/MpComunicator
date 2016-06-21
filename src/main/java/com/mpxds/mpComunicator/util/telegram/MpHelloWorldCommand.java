package com.mpxds.mpComunicator.util.telegram;

import io.github.nixtabyte.telegram.jtelebot.client.RequestHandler;
import io.github.nixtabyte.telegram.jtelebot.exception.JsonParsingException;
import io.github.nixtabyte.telegram.jtelebot.exception.TelegramServerException;
import io.github.nixtabyte.telegram.jtelebot.request.TelegramRequest;
import io.github.nixtabyte.telegram.jtelebot.request.factory.TelegramRequestFactory;
import io.github.nixtabyte.telegram.jtelebot.response.json.Message;
import io.github.nixtabyte.telegram.jtelebot.server.impl.AbstractCommand;
 
public class MpHelloWorldCommand extends AbstractCommand {
 
    public MpHelloWorldCommand(Message message, RequestHandler requestHandler) {
        super(message, requestHandler);
    }
 
    @Override
    public void execute() {
        try {
            TelegramRequest telegramRequest = TelegramRequestFactory.createSendMessageRequest(
            				message.getChat().getId(), "Hello World!", true, message.getId(), null);
            //
            requestHandler.sendRequest(telegramRequest);
            //
        } catch (JsonParsingException | TelegramServerException e) {
            e.printStackTrace();
        }
    }

}
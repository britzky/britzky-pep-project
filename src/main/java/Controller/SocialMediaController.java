package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registrationHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagesHandler);
        app.get("/messages", this::allMessagesHandler);
        app.get("/messages/{message_id}", this::messageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);

        return app;
    }


    private void registrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(context.body(), Account.class);
        Account addedUser = accountService.addUser(user);
        if (addedUser!=null){
            context.json(mapper.writeValueAsString(addedUser));
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account user = mapper.readValue(context.body(), Account.class);
        Account loggedInUser = accountService.loginUser(user);
        if (loggedInUser!=null){
            context.json(mapper.writeValueAsString(loggedInUser));
        } else {
            context.status(401);

        }
    }

    private void messagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage!=null){
            context.json(mapper.writeValueAsString(addedMessage));
            context.status(200);
        } else {
            context.status(400);
        }
    }

    
    private void messageByIdHandler(Context context) {
        String messageIdString = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdString);
        
        Message message = new Message(messageId, 0, "", 0L);
        
        Message retrievedMessage = messageService.getMessageById(message.getMessage_id());
        
        context.json(retrievedMessage);
    }
    
    private void deleteMessageHandler(Context context) {
        String messageIdString = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdString);
        
        Message message = new Message(messageId, 0, "", 0L);
        
        messageService.deleteMessage(message);
    }
    
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        String messageIdString = context.pathParam("message_id");
        int messageId = Integer.parseInt(messageIdString);
        
        ObjectMapper mapper = new ObjectMapper();
        
        Message partialMessage = mapper.readValue(context.body(), Message.class);
        
        Message existingMessage = messageService.getMessageById(messageId);
        
        Message newMessage = new Message(messageId, existingMessage.getPosted_by(), partialMessage.getMessage_text(), existingMessage.getTime_posted_epoch());
        
        Message updatedMessageText = messageService.updateMessageText(newMessage);
        
        if (updatedMessageText != null){
            context.json(mapper.writeValueAsString(updatedMessageText));
        } else {
            context.status(400);
        }
    }
    
    private void allMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void getAllMessagesByAccountIdHandler(Context context) {
        String accountIdString = context.pathParam("account_id");
        int accountId = Integer.parseInt(accountIdString);
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        context.json(messages);
    }
    
}
package Service;

import Model.Message;

import java.util.List;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message) {
        System.out.println("Received message " + message);
        if (message != null && message.getMessage_text().length() <= 255 && message.getMessage_text().length() > 0 && messageDAO.userExists(message.getPosted_by())) {
            System.out.println("Conditions met, adding message to the database.");
            return messageDAO.addMessage(message);
        }
        System.out.println("Conditions not met, returning null.");
        return null;
    }
    
    
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    
    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }
    
    public Message updateMessageText(Message message) {
        Message existingMessage = messageDAO.getMessageById(message.getMessage_id());
        if (existingMessage == null) {
            return null;
        }
        if (message != null && message.getMessage_text().length() <= 255 && message.getMessage_text().length() > 0){
            boolean isUpdated = messageDAO.updateMessage(message);
            if (isUpdated){
                return messageDAO.getMessageById(message.getMessage_id());
            } else {
                return null;
            }
        } else {
            return null;
        }
        
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByAccountId(int accountId) {
        return messageDAO.getAllMessagesByAccountId(accountId);
    }
}

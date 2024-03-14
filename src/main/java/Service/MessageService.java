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
        if (message != null && message.getMessage_text().length() <= 255 && messageDAO.userExists(message.getPosted_by())) {
            return messageDAO.addMessage(message);
        }
        return null;
    }
    
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(Message message) {
        return messageDAO.getMessageById(message);
    }
}

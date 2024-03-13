package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addUser(Account user) {
        if (accountDAO.usernameExists(user.getUsername())){
            System.out.println("Username is taken please try again.");
        }
        if (user.getUsername() != null && user.getPassword().length() >= 4){
            return accountDAO.addUser(user);
        } 
        return null;
    }

    public Account loginUser(Account user) {
        return accountDAO.loginUser(user);
    }
}


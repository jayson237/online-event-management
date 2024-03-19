package session;

import entity.Account;
import exception.AccountNotFoundException;
import exception.CreateAccountException;
import exception.InvalidLoginException;
import javax.ejb.Local;

/**
 *
 * @author jayso
 */
@Local
public interface AccountSessionLocal {

    public Long registerAccount(Account account);

    public boolean login(String email, String password) throws AccountNotFoundException, InvalidLoginException;

    public Account retrieveAccountById(Long accountId) throws AccountNotFoundException;

    public void updateAccount(Account a) throws AccountNotFoundException;

    public Account retrieveAccountByEmail(String email) throws AccountNotFoundException;

    public void putProfilePicture(Long accountId, String url) throws AccountNotFoundException;

    public void checkUniqueEmail(String email) throws CreateAccountException;

}

package session;

import entity.Account;
import exception.AccountNotFoundException;
import exception.CreateAccountException;
import exception.InvalidLoginException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jayso
 */
@Stateless
public class AccountSession implements AccountSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long registerAccount(Account account) {
        em.persist(account);
        em.flush();
        return account.getAccountId();
    }

    @Override
    public void checkUniqueEmail(String email) throws CreateAccountException {
        Query query = em.createQuery("SELECT a FROM Account a WHERE a.email = :email");
        query.setParameter("email", email);
        if (!query.getResultList().isEmpty()) {
            throw new CreateAccountException("Email is in use, please choose another email");
        }
    }

    @Override
    public boolean login(String email, String password) throws AccountNotFoundException, InvalidLoginException {
        Account c = retrieveAccountByEmail(email);
        if (c == null) {
            throw new AccountNotFoundException("Account with the email " + email + " does not exist");
        }

        if (!c.getPassword().equals(password)) {
            throw new InvalidLoginException("Incorrect credentials! Please try again...");
        }
        return true;
    }

    @Override
    public Account retrieveAccountByEmail(String email) throws AccountNotFoundException {
        try {
            Query q = em.createQuery("SELECT c FROM Account c WHERE c.email = :email");
            q.setParameter("email", email);
            Account e = (Account) q.getSingleResult();
            return e;
        } catch (NoResultException ex) {
            throw new AccountNotFoundException("Account with the email " + email + " does not exist");
        }
    }

    @Override
    public Account retrieveAccountById(Long accountId) throws AccountNotFoundException {
        Account c = em.find(Account.class,
                accountId);
        if (c != null) {
            return c;
        }
        throw new AccountNotFoundException("Account with the id: " + accountId + " does not exist\n");
    }

    @Override
    public void putProfilePicture(Long accountId, String url) throws AccountNotFoundException {
        Account c = retrieveAccountById(accountId);
        c.setProfileImage(url);
    }

    @Override
    public void updateAccount(Account a) throws AccountNotFoundException {
        Account accountToUpdate = retrieveAccountById(a.getAccountId());

        if (accountToUpdate != null) {
            accountToUpdate.setName(a.getName());
            accountToUpdate.setContactDetails(a.getContactDetails());
            accountToUpdate.setEmail(a.getEmail());
            accountToUpdate.setPassword(a.getPassword());

        } else {
            throw new AccountNotFoundException("Account with the id: " + a.getAccountId() + " does not exist");
        }

    }
}

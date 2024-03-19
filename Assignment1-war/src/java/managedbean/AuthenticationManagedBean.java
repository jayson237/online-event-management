package managedbean;

import entity.Account;
import entity.Event;
import entity.Registration;
import exception.AccountNotFoundException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.AccountSessionLocal;

@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private AccountSessionLocal accountSessionLocal;

    private String email = null;
    private String password = null;
    private String name = null;
    private String contactDetails = null;
    private Long userId = Long.MIN_VALUE;
    private List<Event> myEvents = new ArrayList<>();
    private List<Event> registeredEvents = new ArrayList<>();

    public AuthenticationManagedBean() {
    }

    public Account getAccount() throws AccountNotFoundException {
        return accountSessionLocal.retrieveAccountById(getUserId());
    }

    public void updateAccount() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Account a = getAccount();
            a.setName(getName());
            a.setContactDetails(getContactDetails());
            accountSessionLocal.updateAccount(a);
            context.addMessage(null, new FacesMessage("Success", "Account updated successfuly"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update account"));
        }
    }

    public void init() throws AccountNotFoundException {
        Account a = getAccount();
        setEmail(a.getEmail());
        setPassword(a.getPassword());
        setName(a.getName());
        setContactDetails(a.getContactDetails());
        setMyEvents(a.getOrganizedEvents());
        List<Registration> r = a.getRegisteredEvents();
        r.forEach(re -> registeredEvents.add(re.getEvent()));
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (accountSessionLocal.login(getEmail(), getPassword())) {
                Long id = accountSessionLocal.retrieveAccountByEmail(getEmail()).getAccountId();
                setUserId(id);
                init();
                return "/secret/index.xhtml?faces-redirect=true";
            } else {
                //login failed
                setEmail(null);
                setPassword(null);
                setName(null);
                setContactDetails(null);
                setUserId((Long) Long.MIN_VALUE);
                setMyEvents(new ArrayList<>());
                setRegisteredEvents(new ArrayList<>());
                return "login.xhtml";
            }
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to login", ex.getMessage()));
        }
        return "login.xhtml";
    }

    public String login(String email, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (accountSessionLocal.login(email, password)) {
                Long id = accountSessionLocal.retrieveAccountByEmail(email).getAccountId();
                setUserId(id);
                init();
                return "/secret/index.xhtml?faces-redirect=true";
            } else {
                setEmail(null);
                setPassword(null);
                setName(null);
                setContactDetails(null);
                setUserId((Long) Long.MIN_VALUE);
                setMyEvents(new ArrayList<>());
                setRegisteredEvents(new ArrayList<>());
                return "login.xhtml";
            }
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to login", ex.getMessage()));
        }
        return "login.xhtml";
    }

    public String logout() {
        setEmail(null);
        setPassword(null);
        setName(null);
        setContactDetails(null);
        setUserId((Long) Long.MIN_VALUE);
        setMyEvents(new ArrayList<>());
        setRegisteredEvents(new ArrayList<>());
        return "/login.xhtml?faces-redirect=true";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public List<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Event> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

}

package managedbean;

import entity.Account;
import exception.CreateAccountException;
import javax.faces.event.ActionEvent;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import session.AccountSessionLocal;

/**
 *
 * @author jayso
 */
@Named(value = "accountManagedBean")
@RequestScoped
public class AccountManagedBean {

    @EJB
    private AccountSessionLocal accountSessionLocal;

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    private String name;
    private String contactDetails;
    private String email;
    private String password;

    public AccountManagedBean() {
    }

    public void addAccount(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            accountSessionLocal.checkUniqueEmail(getEmail());
        } catch (CreateAccountException ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to create account", "Email is in use, please choose another email"));
            return;
        }

        try {
            Account a = new Account();
            a.setName(getName());
            a.setContactDetails(getContactDetails());
            a.setEmail(getEmail());
            a.setPassword(getPassword());
            accountSessionLocal.registerAccount(a);
            authenticationManagedBean.login(a.getEmail(), a.getPassword());
            context.getExternalContext().redirect("secret/index.xhtml");
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to create account", ex.getMessage()));
        }
    }

    public void updateEmail(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        try {
            Account a = authenticationManagedBean.getAccount();
            a.setEmail(getEmail());
            accountSessionLocal.updateAccount(a);
            authenticationManagedBean.setEmail(a.getEmail());
            context.addMessage("growl", new FacesMessage("Success", "Email updated successfuly"));
            context.getExternalContext().redirect("viewMyProfile.xhtml");
        } catch (Exception ex) {
            context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to update email"));
        }
    }

    public void updatePassword(ActionEvent e) {
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        try {
            Account a = authenticationManagedBean.getAccount();
            a.setPassword(getPassword());
            accountSessionLocal.updateAccount(a);
            authenticationManagedBean.setPassword(a.getPassword());
            context.addMessage("growl", new FacesMessage("Success", "Password updated successfuly"));
            context.getExternalContext().redirect("viewMyProfile.xhtml");
        } catch (Exception ex) {
            context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to reset password"));
        }
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

}

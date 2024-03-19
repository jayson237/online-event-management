package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author jayso
 */
@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(length = 32, nullable = false)
    private String name;

    @Column(length = 32, nullable = false)
    private String contactDetails;

    @Column(length = 32, unique = true, nullable = false)
    private String email;

    @Column(length = 32, nullable = false)
    private String password;

    @Column(length = 255)
    private String profileImage;

    @OneToMany(mappedBy = "organizer", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Event> organizedEvents;

    @OneToMany(mappedBy = "attendee", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Registration> registeredEvents;

    public Account() {
        this.profileImage = "";
        this.organizedEvents = new ArrayList<>();
        this.registeredEvents = new ArrayList<>();
    }

    public Account(String name, String contactDetails, String email, String password) {
        this.name = name;
        this.contactDetails = contactDetails;
        this.email = email;
        this.password = password;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public List<Registration> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<Registration> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }

}

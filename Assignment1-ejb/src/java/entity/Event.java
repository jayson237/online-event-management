package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jayso
 */
@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(length = 32, nullable = false)
    private String eventTitle;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date eventDate;

    @Column(length = 32, nullable = false)
    private String location;

    @Column(nullable = false)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date registrationDeadline;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Account organizer;

    @OneToMany(mappedBy = "event", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Registration> registrations;

    @Column(nullable = false)
    private boolean isCancelled;

    public Event() {
        this.registrations = new ArrayList<>();
        this.isCancelled = false;
    }

    public Event(String eventTitle, Date eventDate, String location, String description, Date registrationDeadline) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.location = location;
        this.description = description;
        this.registrationDeadline = registrationDeadline;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public Account getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Account organizer) {
        this.organizer = organizer;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public String toString() {
        return "entity.Event[ id=" + eventId + " ]";
    }

}

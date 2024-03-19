package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class Registration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "attendee", nullable = false)
    private Account attendee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event", nullable = false)
    private Event event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date registrationDate;

    @Column(nullable = false)
    private boolean attended;

    public Registration() {

    }

    public Registration(Account account, Event event) {
        this.attendee = account;
        this.event = event;
        this.registrationDate = new Date();
        this.attended = false;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public Account getAttendee() {
        return attendee;
    }

    public void setAttendee(Account attendee) {
        this.attendee = attendee;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }
}

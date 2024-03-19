package session;

import entity.Account;
import entity.Event;
import entity.Registration;
import exception.AccountNotFoundException;
import exception.EventNotFoundException;
import exception.RegistrationException;
import exception.RegistrationNotFoundException;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jayso
 */
@Stateless
public class RegistrationSession implements RegistrationSessionLocal {

    @EJB
    private EventSessionLocal eventSession;

    @EJB
    private AccountSessionLocal accountSession;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createRegistration(Long accountId, Long eventId) throws AccountNotFoundException, EventNotFoundException, RegistrationException {
        Account a = accountSession.retrieveAccountById(accountId);
        Event e = eventSession.retrieveEventById(eventId);
        Registration r = new Registration(a, e);
        if (e.getRegistrationDeadline().compareTo(r.getRegistrationDate()) > 0 && e.getEventDate().compareTo(e.getRegistrationDeadline()) > 0) {
            em.persist(r);
            a.getRegisteredEvents().add(r);
            e.getRegistrations().add(r);
            em.flush();
            return r.getRegistrationId();
        }
        throw new RegistrationException("Registration has closed");
    }

    @Override
    public boolean unregister(Long registrationId) throws RegistrationNotFoundException {
        Registration r = retrieveRegistrationById(registrationId);
        Date currDate = new Date();
        if (r != null && r.getEvent().getEventDate().compareTo(currDate) > 0) {
            Account a = r.getAttendee();
            Event e = r.getEvent();

            if (a != null) {
                a.getRegisteredEvents().remove(r);
            }
            if (e != null) {
                e.getRegistrations().remove(r);
            }

            em.remove(r);
            em.flush();
            return true;
        }
        return false;
    }

    @Override
    public Registration retrieveRegistrationById(Long registrationId) throws RegistrationNotFoundException {
        Registration c = em.find(Registration.class, registrationId);
        if (c != null) {
            return c;
        }
        throw new RegistrationNotFoundException("Registration with the id: " + registrationId + " does not exist\n");
    }

    @Override
    public void markAttendance(Long registrationId) throws RegistrationNotFoundException {
        Registration c = retrieveRegistrationById(registrationId);

        if (c.isAttended()) {
            c.setAttended(false);
        } else {
            c.setAttended(true);
        }
    }

}

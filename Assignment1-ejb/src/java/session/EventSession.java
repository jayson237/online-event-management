package session;

import entity.Account;
import entity.Event;
import exception.AccountNotFoundException;
import exception.CreateEventException;
import exception.EventNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jayso
 */
@Stateless
public class EventSession implements EventSessionLocal {

    @EJB
    private AccountSessionLocal accountSession;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Long createEvent(Event event, Long accountId) throws AccountNotFoundException, CreateEventException {
        Date now = new Date();
        Date eventDate = event.getEventDate();
        Date signUpBy = event.getRegistrationDeadline();
        if (eventDate.compareTo(signUpBy) >= 0 && eventDate.compareTo(now) >= 0 && signUpBy.compareTo(now) >= 0) {
            Account a = accountSession.retrieveAccountById(accountId);
            em.persist(event);
            event.setOrganizer(a);
            a.getOrganizedEvents().add(event);
            em.flush();
            return event.getEventId();
        }
        throw new CreateEventException("Invalid dates");
    }

    @Override
    public boolean deleteEvent(Long eventId) throws EventNotFoundException {
        Event e = retrieveEventById(eventId);
        if (e.getRegistrations().isEmpty()) {
            Account organizer = e.getOrganizer();
            boolean removed = organizer.getOrganizedEvents().remove(e);

            if (removed) {
                em.remove(e);
                em.flush();
                return true;
            }
        }
        e.setIsCancelled(true);
        return false;
    }
    
    @Override
    public void editEvent(Event e) throws EventNotFoundException {
        Event event = retrieveEventById(e.getEventId());
        
         if (event != null) {
            event.setEventTitle(e.getEventTitle());
            event.setEventDate(e.getEventDate());
            event.setRegistrationDeadline(e.getRegistrationDeadline());
            event.setLocation(e.getLocation());
            event.setDescription(e.getDescription());

        } else {
            throw new EventNotFoundException("Event with the id: " + e.getEventId() + " does not exist");
        }
                
    }

    @Override
    public Event retrieveEventById(Long eventId) throws EventNotFoundException {
        Event c = em.find(Event.class, eventId);
        if (c != null) {
            return c;
        }
        throw new EventNotFoundException("Event with the id: " + eventId + " does not exist\n");
    }

    @Override
    public List<Event> searchEvents(String query) {
        Query q = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.eventTitle) LIKE :query");
        q.setParameter("query", "%" + query + "%");

        return q.getResultList();
    }

    @Override
    public List<Event> getAllEvents() {
        return em.createQuery("SELECT e FROM Event e ORDER BY e.eventDate ASC").getResultList();
    }

}

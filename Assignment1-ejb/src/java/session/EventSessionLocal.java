/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package session;

import entity.Event;
import exception.AccountNotFoundException;
import exception.CreateEventException;
import exception.EventNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jayso
 */
@Local
public interface EventSessionLocal {

    public Event retrieveEventById(Long eventId) throws EventNotFoundException;

    public Long createEvent(Event event, Long accountId) throws AccountNotFoundException, CreateEventException;

    public boolean deleteEvent(Long eventId) throws EventNotFoundException;

    public List<Event> searchEvents(String query);

    public List<Event> getAllEvents();

    public void editEvent(Event e) throws EventNotFoundException;

}

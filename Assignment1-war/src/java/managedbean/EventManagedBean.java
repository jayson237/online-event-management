package managedbean;

import entity.Event;
import entity.Registration;
import exception.AccountNotFoundException;
import exception.EventNotFoundException;
import exception.RegistrationException;
import exception.RegistrationNotFoundException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import session.EventSessionLocal;
import session.RegistrationSessionLocal;

/**
 *
 * @author jayso
 */
@Named(value = "eventManagedBean")
@ViewScoped
public class EventManagedBean implements Serializable {

    @EJB
    private RegistrationSessionLocal registrationSession;

    @EJB
    private EventSessionLocal eventSessionBean;

    @Inject
    private AuthenticationManagedBean authenticationManagedBean;

    private String eventTitle;
    private Date eventDate;
    private String location;
    private String description;
    private Date registrationDeadline;

    private Long selectedEventId; // For viewing current event
    private Event selectedEvent;
    private Long organizerId;
    private List<Registration> selectedEventRegistrations;
    private boolean registered;
    private Long registrationId;

    private List<Event> events; // To list down all events
    private String searchTerm;
    private String viewMode = "allEvents";

    public EventManagedBean() {
    }

    @PostConstruct
    public void init() {
        setEvents(eventSessionBean.getAllEvents());
    }

    public void createEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(this.eventDate);
            cal.add(Calendar.HOUR, 12);
            this.eventDate = cal.getTime();
            cal.setTime(this.registrationDeadline);
            cal.add(Calendar.HOUR, 12);
            this.registrationDeadline = cal.getTime();
            Long currentUserId = authenticationManagedBean.getUserId();
            Event e = new Event();
            e.setEventTitle(getEventTitle());
            e.setEventDate(getEventDate());
            e.setLocation(getLocation());
            e.setDescription(getDescription());
            e.setRegistrationDeadline(getRegistrationDeadline());
            eventSessionBean.createEvent(e, currentUserId);
            authenticationManagedBean.getMyEvents().add(e);
            eventTitle = null;
            eventDate = null;
            location = null;
            description = null;
            registrationDeadline = null;
            context.addMessage("growl", new FacesMessage("Success", "Event created successfuly"));
            context.getExternalContext().redirect("browseEvents.xhtml");
        } catch (Exception ex) {
            context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }

    }

    public void deleteEvent(Event e) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            if (!isAbleToModify(e)) {
                return;
            }
            boolean isNotCancelled = eventSessionBean.deleteEvent(e.getEventId());
            if (!isNotCancelled) {
                for (Event event : authenticationManagedBean.getMyEvents()) {
                    if (event.getEventId().equals(e.getEventId())) {
                        event.setIsCancelled(true);
                        break;
                    }
                }
                context.addMessage(null, new FacesMessage("Success", "Since there are attendees to this event, the event will be cancelled instead"));
            } else {
                authenticationManagedBean.getMyEvents().remove(e);
                context.addMessage(null, new FacesMessage("Success", "Event deleted successfuly"));
            }
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }
    }

    public void deleteEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Flash flash = context.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        try {
            Event e = this.selectedEvent;
            if (!isAbleToModify(e)) {
                return;
            }
            boolean isNotCancelled = eventSessionBean.deleteEvent(e.getEventId());
            if (!isNotCancelled) {
                for (Event event : authenticationManagedBean.getMyEvents()) {
                    if (event.getEventId().equals(e.getEventId())) {
                        event.setIsCancelled(true);
                        context.addMessage("growl", new FacesMessage("Success", "Since there are attendees to this event, the event will be cancelled instead"));
                        context.getExternalContext().redirect("browseEvents.xhtml");
                    }
                }
            } else {
                Iterator<Event> iterator = authenticationManagedBean.getMyEvents().iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    if (event.getEventId().equals(selectedEventId)) {
                        iterator.remove();
                        break;
                    }
                }
                context.addMessage("growl", new FacesMessage("Success", "Event deleted successfuly"));
                context.getExternalContext().redirect("browseEvents.xhtml");
            }
        } catch (Exception ex) {
            context.addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }

    }

    public void editEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Calendar cal = Calendar.getInstance();
            Event toUpdate = eventSessionBean.retrieveEventById(selectedEventId);
            toUpdate.setEventTitle(getEventTitle());
            cal.setTime(getEventDate());
            cal.add(Calendar.HOUR, 12);
            this.eventDate = cal.getTime();
            toUpdate.setEventDate(getEventDate());
            cal.setTime(this.registrationDeadline);
            cal.add(Calendar.HOUR, 12);
            this.registrationDeadline = cal.getTime();
            toUpdate.setRegistrationDeadline(getRegistrationDeadline());
            toUpdate.setLocation(getLocation());
            toUpdate.setDescription(getDescription());
            eventSessionBean.editEvent(toUpdate);
            for (Event e : authenticationManagedBean.getMyEvents()) {
                if (e.getEventId().equals(toUpdate.getEventId())) {
                    e.setEventTitle(toUpdate.getEventTitle());
                    e.setEventDate(toUpdate.getEventDate());
                    e.setRegistrationDeadline(toUpdate.getRegistrationDeadline());
                    e.setLocation(toUpdate.getLocation());
                    e.setDescription(toUpdate.getDescription());
                }
            }
            context.addMessage(null, new FacesMessage("Success", "Event details updated successfuly"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
        }
    }

    public void search() {
        events = eventSessionBean.searchEvents(searchTerm);
    }

    public void updateEventList() {
        if (null != viewMode) {
            switch (viewMode) {
                case "allEvents":
                    events = eventSessionBean.getAllEvents();
                    break;
                case "registeredEvents":
                    events = authenticationManagedBean.getRegisteredEvents();
                    break;
                case "organizedEvents":
                    events = authenticationManagedBean.getMyEvents();
                    break;
                default:
                    break;
            }
        }
    }

    public void loadSelectedOrganizingEvent() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            this.selectedEvent = eventSessionBean.retrieveEventById(selectedEventId);
            eventTitle = this.selectedEvent.getEventTitle();
            eventDate = this.selectedEvent.getEventDate();
            location = this.selectedEvent.getLocation();
            description = this.selectedEvent.getDescription();
            registrationDeadline = this.selectedEvent.getRegistrationDeadline();
            organizerId = this.selectedEvent.getOrganizer().getAccountId();
            selectedEventRegistrations = this.selectedEvent.getRegistrations();
            checkAndSetRegistrationStatus();

        } catch (EventNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load event"));
        }
    }

    public void onAttendanceChange(AjaxBehaviorEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            UIComponent component = event.getComponent();
            Long currRegistrationId = (Long) component.getAttributes().get("currRegistrationId");
            registrationSession.markAttendance(currRegistrationId);
            context.addMessage(null, new FacesMessage("Success", "Attendance has been taken"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to mark attendance"));
        }
    }

    public String determineEventStatusMessage() {
        Date today = new Date();
        if (selectedEvent.getIsCancelled()) {
            return "(Cancelled)";
        } else if (today.compareTo(selectedEvent.getRegistrationDeadline()) > 0) {
            return "(The registration has closed)";
        }
        return null;
    }

    public boolean isStatusMessageDisplayed() {
        Date today = new Date();
        return selectedEvent.getIsCancelled() || today.compareTo(selectedEvent.getRegistrationDeadline()) > 0;
    }

    public boolean isAbleToModify(Event e) {
        Date today = new Date();
        return today.compareTo(e.getEventDate()) < 0;
    }

    public void checkAndSetRegistrationStatus() {
        for (Registration registration : selectedEventRegistrations) {
            if (registration.getAttendee().getAccountId().equals(authenticationManagedBean.getUserId())) {
                this.registered = true;
                this.registrationId = registration.getRegistrationId();
                return;
            }
        }
        this.registered = false;
    }

    public void register() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Long currUserId = authenticationManagedBean.getUserId();
            Long rId = registrationSession.createRegistration(currUserId, selectedEventId);
            Event e = registrationSession.retrieveRegistrationById(rId).getEvent();
            authenticationManagedBean.getRegisteredEvents().add(e);
            this.registered = true;
            context.addMessage(null, new FacesMessage("Success", "Registration successful"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register"));
        }
    }

    public void unregister() throws RegistrationNotFoundException {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Event e = registrationSession.retrieveRegistrationById(registrationId).getEvent();
            registrationSession.unregister(registrationId);
            Iterator<Event> iterator = authenticationManagedBean.getRegisteredEvents().iterator();
            while (iterator.hasNext()) {
                Event event = iterator.next();
                if (event.getEventId().equals(e.getEventId())) {
                    iterator.remove();
                    break;
                }
            }
            context.addMessage(null, new FacesMessage("Success", "Unregister successful"));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to unregister"));
        }
    }

    public void toggleRegistration() throws AccountNotFoundException, EventNotFoundException, RegistrationException, RegistrationNotFoundException {
        checkAndSetRegistrationStatus();
        if (this.registered) {
            unregister();
        } else {
            register();
        }
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEvent) {
        this.selectedEventId = selectedEvent;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public List<Registration> getSelectedEventRegistrations() {
        return selectedEventRegistrations;
    }

    public void setSelectedEventRegistrations(List<Registration> selectedEventRegistrations) {
        this.selectedEventRegistrations = selectedEventRegistrations;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

}

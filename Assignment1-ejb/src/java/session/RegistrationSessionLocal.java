package session;

import entity.Registration;
import exception.AccountNotFoundException;
import exception.EventNotFoundException;
import exception.RegistrationException;
import exception.RegistrationNotFoundException;
import javax.ejb.Local;

/**
 *
 * @author jayso
 */
@Local
public interface RegistrationSessionLocal {

    public Long createRegistration(Long accountId, Long eventId) throws AccountNotFoundException, EventNotFoundException, RegistrationException;

    public Registration retrieveRegistrationById(Long registrationId) throws RegistrationNotFoundException;

    public boolean unregister(Long registrationId) throws RegistrationNotFoundException;

    public void markAttendance(Long registrationId) throws RegistrationNotFoundException;

}

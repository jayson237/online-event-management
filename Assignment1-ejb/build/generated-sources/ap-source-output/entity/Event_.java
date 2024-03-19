package entity;

import entity.Account;
import entity.Registration;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2024-03-19T11:22:51")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, Long> eventId;
    public static volatile SingularAttribute<Event, String> eventTitle;
    public static volatile SingularAttribute<Event, Boolean> isCancelled;
    public static volatile ListAttribute<Event, Registration> registrations;
    public static volatile SingularAttribute<Event, Account> organizer;
    public static volatile SingularAttribute<Event, String> description;
    public static volatile SingularAttribute<Event, String> location;
    public static volatile SingularAttribute<Event, Date> registrationDeadline;
    public static volatile SingularAttribute<Event, Date> eventDate;

}
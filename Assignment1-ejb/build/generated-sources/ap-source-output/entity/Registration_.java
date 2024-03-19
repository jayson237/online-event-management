package entity;

import entity.Account;
import entity.Event;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2024-03-19T11:22:51")
@StaticMetamodel(Registration.class)
public class Registration_ { 

    public static volatile SingularAttribute<Registration, Boolean> attended;
    public static volatile SingularAttribute<Registration, Account> attendee;
    public static volatile SingularAttribute<Registration, Long> registrationId;
    public static volatile SingularAttribute<Registration, Date> registrationDate;
    public static volatile SingularAttribute<Registration, Event> event;

}
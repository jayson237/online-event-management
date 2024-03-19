package entity;

import entity.Event;
import entity.Registration;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2024-03-19T11:22:51")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, Long> accountId;
    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, String> name;
    public static volatile ListAttribute<Account, Registration> registeredEvents;
    public static volatile SingularAttribute<Account, String> profileImage;
    public static volatile ListAttribute<Account, Event> organizedEvents;
    public static volatile SingularAttribute<Account, String> contactDetails;
    public static volatile SingularAttribute<Account, String> email;

}
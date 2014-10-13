package showcase.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    @Basic(optional = false)
    private Long cooperationPartnerId;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    @Basic(optional = false)
    private String customerType;
    @Basic(optional = false)
    private String dispatchType;
    @ElementCollection
    private Map<String, String> properties = new HashMap<String, String>();

    @OneToMany(mappedBy = "customer")
    @MapKey(name = "contactType")
    private Map<ContactType, Contact> contacts;

}

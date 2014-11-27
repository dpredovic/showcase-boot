package showcase.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long cooperationPartnerId;
    @NonNull
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    @NonNull
    private String customerType;
    @NonNull
    private String dispatchType;

    @ElementCollection
    private Map<String, String> properties = new HashMap<String, String>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @MapKey(name = "contactType")
    private Map<ContactType, Contact> contacts = new EnumMap<ContactType, Contact>(ContactType.class);

}

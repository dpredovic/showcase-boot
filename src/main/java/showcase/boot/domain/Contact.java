package showcase.boot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "customer")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String street;
    @NonNull
    private String zipCode;
    @NonNull
    private String countryCode;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn
    @JsonIgnore
    private Customer customer;

    @NonNull
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @ElementCollection
    private Map<String, String> communications = new HashMap<String, String>();

}

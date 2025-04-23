package mks.myworkspace.crm.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "crm_address")
@Getter
@Setter
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street", length = 255)
    private String street;

    @Column(name = "ward", length = 255)
    private String ward;

    @Column(name = "district", length = 255)
    private String district;

    @Column(name = "state", length = 255)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postcode", length = 20)
    private String postcode;

    @Column(name = "latitude", precision = 10, scale = 7)
    private Double latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private Double longitude;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (country != null && country.trim().equalsIgnoreCase("Vietnam")) {
            if (street != null && !street.isEmpty()) sb.append(street);
            if (ward != null && !ward.isEmpty()) sb.append(", ").append(ward);
            if (district != null && !district.isEmpty()) sb.append(", ").append(district);
            if (state != null && !state.isEmpty()) sb.append(", ").append(state);
            sb.append(", Vietnam");
        } else {
            // Non-Vietnam format
            if (street != null && !street.isEmpty()) sb.append(street);
            if (district != null && !district.isEmpty()) sb.append(", ").append(district);
            if (state != null && !state.isEmpty()) sb.append(" ").append(state);
            if (postcode != null && !postcode.isEmpty()) sb.append(" ").append(postcode);
            if (country != null && !country.isEmpty()) sb.append(", ").append(country);
        }
        return sb.toString().trim();
    }


}


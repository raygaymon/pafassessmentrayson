package vttp2023.batch3.assessment.paf.bookings.models;
import org.bson.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String suburb;
    private String governmentArea;
    private String market;
    private String country;
    private String country_code;

    public static Address fromDoc (Document d) {
        Address a = new Address();
        a.setStreet(d.getString("street"));
        a.setSuburb(d.getString("suburb"));
        a.setGovernmentArea(d.getString("government_area"));
        a.setMarket(d.getString("market"));
        a.setCountry(d.getString("country"));
        a.setCountry_code(d.getString("country_code"));
        return a;
    }
}

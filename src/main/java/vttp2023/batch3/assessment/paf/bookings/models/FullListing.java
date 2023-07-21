package vttp2023.batch3.assessment.paf.bookings.models;
import java.util.List;

import org.bson.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullListing {
    private String id;
    private String name;
    private String description;
    private Address address;
    private String imageURL;
    private Integer price;
    private List<String> amenities;

    public static FullListing fromDoc (Document d){
        List<String> amenities = (List<String>) d.get("amenities");
        Document images = (Document) d.get("images");
        Document address = (Document) d.get("address");
        FullListing fl = new FullListing();
        fl.setId(d.getString("_id"));
        fl.setName(d.getString("name"));
        fl.setDescription(d.getString("description"));
        fl.setAddress(Address.fromDoc(address));
        fl.setImageURL(images.getString("picture_url"));
        fl.setPrice(d.getInteger("price"));
        fl.setAmenities(amenities);
        return fl;
    }
}

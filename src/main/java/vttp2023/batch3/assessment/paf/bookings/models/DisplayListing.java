package vttp2023.batch3.assessment.paf.bookings.models;
import org.bson.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayListing {
    private String name;
    private String imageURL;
    private Integer price;

    public static DisplayListing fromDoc(Document d){
        DisplayListing dl = new DisplayListing();
        Document address = (Document) d.get("images");
        dl.setImageURL(address.getString("picture_url"));
        dl.setName(d.getString("name"));
        dl.setPrice(d.getInteger("price"));
        return dl;
    }
}

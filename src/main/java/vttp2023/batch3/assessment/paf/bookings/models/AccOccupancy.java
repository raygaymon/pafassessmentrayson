package vttp2023.batch3.assessment.paf.bookings.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccOccupancy {
    private String acc_id;
    private Integer vacancy;
}

package vttp2023.batch3.assessment.paf.bookings.models;
import java.util.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    private String resv_id;
    
    @NotEmpty(message="please provide a valid name")
    private String name;

    @NotEmpty(message="please provide a valid enail")
    private String email;

    private String acc_id;

    @Future(message="you time traveller?")
    private Date arrivalDate;
    
    @NotNull(message="this not your house you have to leave at some point")
    private Integer duration;
}

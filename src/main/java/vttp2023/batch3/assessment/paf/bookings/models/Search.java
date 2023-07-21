package vttp2023.batch3.assessment.paf.bookings.models;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search implements Serializable {
    
    @NotNull(message="must pick a valid country")
    @NotEmpty(message="you can't stay in no country")
    private String country;

    @Max(value=10)
    @Min(value=1)
    private Integer numberOfPersons;

    @Min(value = 1)
    private Integer minPrice;
    @Max(value = 10000)
    private Integer maxPrice;

    private Integer priceRange;

}

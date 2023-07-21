package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import vttp2023.batch3.assessment.paf.bookings.models.AccOccupancy;
import vttp2023.batch3.assessment.paf.bookings.models.FullListing;
import vttp2023.batch3.assessment.paf.bookings.models.Reservation;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@Service
public class ListingsService {

	@Autowired
	private ListingsRepository repo;

	//TODO: Task 2

	public List<Document> getAllListings () {
		System.out.println("getting all listings");
		return repo.getAllListings();
	}

	public List<String> getAllCountries (List<Document> listings) {
		List<String> countries = new ArrayList<>();
		for (Document d : listings) {
			Document address = (Document) d.get("address");
			String country = address.getString("country");
			if (!countries.contains(country)){
				countries.add(country);
			}

			if(countries.isEmpty()){
				System.out.println("haha loser");
			}
		}
		return countries;
	}

	public List<ObjectError> validateSearch (Search s) {

		List<ObjectError> errors = new LinkedList<ObjectError>();
        //generate a new fielderror
        FieldError fe;
        if (s.getNumberOfPersons() < 1	) {
            fe = new FieldError ("search","numberOfPersons", "you can't book the room and not stay there");
            errors.add(fe);
        }       
        if (s.getMinPrice() < 1) {
            fe = new FieldError ("search","minPrice", "want free room live with your mother");
            errors.add(fe);
        }
        if (s.getMaxPrice() > 10000) {
            fe = new FieldError ("search","maxPrice", "just buy your own house");
            errors.add(fe);
        }

        return errors;
	}
	
	//TODO: Task 3
	public List<Document> getListingsBySearch (String country, Integer priceRange, Integer numberOfPersons) {
		return repo.getListingsThroughSearch(country, priceRange, numberOfPersons);
	}


	//TODO: Task 4
	public FullListing getListing (String name){
		return FullListing.fromDoc(repo.getListing(name));
	}
	

	//TODO: Task 5
	public AccOccupancy getListingDetails (String id) {
		return repo.getAccomDetails(id);
	}

	public Boolean updateVacancy (String id, Integer vacancy){
		return repo.update(id, vacancy);
	}

	public Boolean insertReservation (Reservation r){
		return repo.insert(r);
	}

}

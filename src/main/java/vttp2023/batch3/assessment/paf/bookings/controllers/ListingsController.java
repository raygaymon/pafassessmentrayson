package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch3.assessment.paf.bookings.models.AccOccupancy;
import vttp2023.batch3.assessment.paf.bookings.models.DisplayListing;
import vttp2023.batch3.assessment.paf.bookings.models.FullListing;
import vttp2023.batch3.assessment.paf.bookings.models.Reservation;
import vttp2023.batch3.assessment.paf.bookings.models.Search;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;

@Controller
@RequestMapping("/mybnb")
public class ListingsController {

	@Autowired
	private ListingsService service;
	//TODO: Task 2

	@GetMapping("/landingpage")
	public String landingPage (Model m, HttpSession session) {
		session.invalidate();
		List<Document> allListings = service.getAllListings();
		Search s = new Search();
		List<String> countries = service.getAllCountries(allListings);
		m.addAttribute("search", s);
		m.addAttribute("countries", countries);
		return "landingPage";
	}
	
	//TODO: Task 3
	@GetMapping("/searchListings")
	public String search (@ModelAttribute @Valid Search s, BindingResult br, HttpSession session, Model m){

		s.setPriceRange(s.getMaxPrice() - s.getMinPrice());
		System.out.println(s.toString());

		if (br.hasErrors()){
			return "landingPage";
		}

		List<ObjectError> errors = service.validateSearch(s);
		if (!errors.isEmpty()){
			for (ObjectError oe : errors){
				br.addError(oe);
			}
			List<Document> allListings = service.getAllListings();
			List<String> countries = service.getAllCountries(allListings);
			m.addAttribute("countries", countries);

			return "landingPage";
		}

		List<Document> matchingListings = new ArrayList<>();
		matchingListings = service.getListingsBySearch(s.getCountry(),s.getPriceRange(),s.getNumberOfPersons());
		
		List<DisplayListing> displayListings = matchingListings.stream()
												.map(DisplayListing::fromDoc)
												.toList();
		if (matchingListings.isEmpty()){
			System.out.println("haha loser");
		}
		if (displayListings.isEmpty()){
			m.addAttribute("empty", "there are no available listings for your criteria: Please check again");
			return "displayListings";
		}

		m.addAttribute("displayListings", displayListings);
		session.setAttribute("search", s);
		m.addAttribute("search", s);
		return "displayListings";
	}

	@GetMapping("/searchList")
	public String searchList (HttpSession session, Model m){
		Search s = (Search) session.getAttribute("search");
		List<Document> matchingListings = new ArrayList<>();
		matchingListings = service.getListingsBySearch(s.getCountry(),s.getPriceRange(),s.getNumberOfPersons());
		
		List<DisplayListing> displayListings = matchingListings.stream()
												.map(DisplayListing::fromDoc)
												.toList();
		if (matchingListings.isEmpty()){
			System.out.println("haha loser");
		}
		if (displayListings.isEmpty()){
			m.addAttribute("empty", "there are no available listings for your criteria: Please check again");
			return "displayListings";
		}

		m.addAttribute("displayListings", displayListings);
		session.setAttribute("search", s);
		m.addAttribute("search", s);
		return "displayListings";
	}


	//TODO: Task 4
	@GetMapping("/search/{name}")
	public String fullListing (@PathVariable("name") String name, Model m, HttpSession session){
		FullListing listingToView = service.getListing(name);
		Reservation r = new Reservation();
		r.setAcc_id(listingToView.getId());
		m.addAttribute("listing", listingToView);
		session.setAttribute("listingName", listingToView.getName());
		m.addAttribute("r", r);
		return "listing";
	}

	//TODO: Task 5
	@PostMapping(path="/listing/book/{id}", consumes = "application/x-www-form-urlencoded")
	public String booking (@PathVariable("id") String id, HttpSession session, @ModelAttribute @Valid Reservation r, BindingResult br, Model m){

		String placeName = (String) session.getAttribute("listingName");
		System.out.println(r.getArrivalDate());

		// if (br.hasErrors()){
		// 	System.out.println("something with br fucked up");
		// 	m.addAttribute("error", "Please enter booking details properly");
		// 	return "redirect:/mybnb/search/" + placeName;
		// }

		AccOccupancy ao = service.getListingDetails(id);
		System.out.println(ao.toString());
		Integer availableVacancy = ao.getVacancy();
		Integer durationOfStay = r.getDuration();
		Integer remainingVacancy = availableVacancy - durationOfStay;

		if (remainingVacancy < 0) {
			System.out.println("something with vacancy fucked up");
			m.addAttribute("error", "The accomodation is not available for the entire duration of your stay");
			return "redirect:/mybnb/listing/" + placeName;
		}

		r.setResv_id(UUID.randomUUID().toString().substring(0, 8));
		r.setAcc_id(id);
		Boolean vacancyUpdate = service.updateVacancy(r.getAcc_id(), remainingVacancy);
		Boolean submitReservation = service.insertReservation(r);
		session.setAttribute("resv_id", r.getResv_id());

		return "redirect:/mybnb/bookingsuccess";
	}

	@GetMapping("/bookingsuccess")
	public String bookingSuccess (Model m, HttpSession session){
		String resv_id = (String) session.getAttribute("resv_id");
		m.addAttribute("resv_id", resv_id);
		return "bookingsuccess";
	}


}

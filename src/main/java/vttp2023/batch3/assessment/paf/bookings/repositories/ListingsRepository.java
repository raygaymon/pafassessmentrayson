package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.AccOccupancy;
import vttp2023.batch3.assessment.paf.bookings.models.FullListing;
import vttp2023.batch3.assessment.paf.bookings.models.Reservation;
import vttp2023.batch3.assessment.paf.bookings.models.Search;

@Repository
public class ListingsRepository {

	@Autowired
	private MongoTemplate MGTemplate;

	@Autowired
	private JdbcTemplate jdbc;

	private static final String GETLISTINGFROMSQL = "select * from acc_occupancy where acc_id = ?";
	private static final String UPDATELISTINGFROMSQL = "update acc_occupancy set vacancy = ? where acc_id = ?";
	private final String INSERTRESERVATION = "insert into reservations (resv_id, name, email,acc_id, arrival_date, duration) values (?, ?, ?, ?, ?, ?)";

	//TODO: Task 2

	public List<Document> getAllListings () {

		/* accomodation.listings.find ({
			"name": { $exists: true }
		})
		*/

		Query q = Query.query(Criteria.where("name").exists(true));
		System.out.println("query running" + q);
		List<Document> output = MGTemplate.find(q, Document.class, "listings");
		return output;
	}
	
	//TODO: Task 3

	public List<Document> getListingsThroughSearch(String country, Integer priceRange, Integer numberOfPersons) {

		/*accomodation.listing.aggregate([
			{
				$match : {
					$and [
						{ "address.country" : country },
						{"accomodates" : {$gte : numberOfPersons }},
						{"price" : {$lte : priceRange }}
					]
				},
				{
					$project: {"name": 1, "price": 1, "images.picture_url": 1 }
				},
				{
					$sort: { "price" : 1 }
				}
			
		]) */

		Criteria c = new Criteria();
		MatchOperation matchCountry = Aggregation.match(
			c.andOperator(
				Criteria.where("address.country").is(country),
				Criteria.where("accommodates").gte(numberOfPersons),
				Criteria.where("price").lte(priceRange)
			)
		);
		System.out.println("matched..");

		ProjectionOperation listingFields = Aggregation.project("name", "price", "images");
		System.out.println("projecting..");

		SortOperation sortByPrice = Aggregation.sort(Sort.by(Direction.DESC, "price"));
		System.out.println("sorted..");

		Aggregation ap = Aggregation.newAggregation(matchCountry, listingFields, sortByPrice);
			
		AggregationResults<Document> results = MGTemplate.aggregate(ap, "listings", Document.class);
		return results.getMappedResults();
	}


	//TODO: Task 4

	public Document getListing(String name){

		/*
		 * accommodation.listings.find({
		 * 	"name" : name
		 * })
		 */
		
		Query q = Query.query(Criteria.where("name").is(name));
		
		Document d = MGTemplate.findOne(q, Document.class, "listings");
		if (d == null) {
			System.out.println("lmao");
		}
		return d;
	}
	

	//TODO: Task 5
	public AccOccupancy getAccomDetails (String id){
		System.out.println(id);
		return jdbc.queryForObject(GETLISTINGFROMSQL, BeanPropertyRowMapper.newInstance(AccOccupancy.class), id);
	}

	public Boolean update (String id, Integer remainingVacancy){
		return jdbc.update(UPDATELISTINGFROMSQL, remainingVacancy, id) > 0 ;
	}

	public Boolean insert (Reservation r) {
		return jdbc.update(INSERTRESERVATION, r.getResv_id(), r.getName(), r.getEmail(), r.getAcc_id(), r.getArrivalDate(), r.getDuration()) > 0;
	}

}

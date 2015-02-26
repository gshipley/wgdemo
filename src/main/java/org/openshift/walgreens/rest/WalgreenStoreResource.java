package org.openshift.walgreens.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.openshift.walgreens.domain.Store;
import org.openshift.walgreens.mongo.DBConnection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RequestScoped
@Path("/stores")
public class WalgreenStoreResource {

	@Inject
	private DBConnection dbConnection;

	private DBCollection getWalgreensStoresCollection() {
		DB db = dbConnection.getDB();
		DBCollection storeListCollection = db.getCollection("stores");
		return storeListCollection;
	}

	private Store populateStoreInformation(DBObject dataValue) {
		Store theStore = new Store();
		theStore.setPosition(dataValue.get("pos"));
		theStore.setId(dataValue.get("_id").toString());
        theStore.setAddress(dataValue.get("address"));
        theStore.setHasClinic(dataValue.get("hasClinic"));
        theStore.setIs24Hours(dataValue.get("24store"));
        theStore.setPhone(dataValue.get("phone"));

		return theStore;
	}

	// get all the mlb parks
	@GET()
	@Produces("application/json")
	public List<Store> getAllStores() {
		ArrayList<Store> allStoresList = new ArrayList<Store>();

		DBCollection walgreensStores = this.getWalgreensStoresCollection();
		DBCursor cursor = walgreensStores.find();
		try {
			while (cursor.hasNext()) {
				allStoresList.add(this.populateStoreInformation(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return allStoresList;
	}

	@GET
	@Produces("application/json")
	@Path("within")
	public List<Store> findStoresWithin(@QueryParam("lat1") float lat1,
                                        @QueryParam("lon1") float lon1, @QueryParam("lat2") float lat2,
                                        @QueryParam("lon2") float lon2) {

		ArrayList<Store> allStoresList = new ArrayList<Store>();
		DBCollection walgreensStores = this.getWalgreensStoresCollection();

		// make the query object
		BasicDBObject spatialQuery = new BasicDBObject();

		ArrayList<double[]> boxList = new ArrayList<double[]>();
		boxList.add(new double[] { new Float(lon2), new Float(lat2) });
		boxList.add(new double[] { new Float(lon1), new Float(lat1) });

		BasicDBObject boxQuery = new BasicDBObject();
		boxQuery.put("$box", boxList);

		spatialQuery.put("pos", new BasicDBObject("$within", boxQuery));
		System.out.println("Using spatial query: " + spatialQuery.toString());

		DBCursor cursor = walgreensStores.find(spatialQuery);
		try {
			while (cursor.hasNext()) {
				allStoresList.add(this.populateStoreInformation(cursor.next()));
			}
		} finally {
			cursor.close();
		}

		return allStoresList;
	}
}

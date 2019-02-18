package com.kamash.ccb;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/")
public class MovieResource {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @GET
    @Path("/film")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(
             @QueryParam("offset")  @DefaultValue("0")     String offset,
             @QueryParam("limit")   @DefaultValue("20")    String limit,
             @QueryParam("orderBy") @DefaultValue("title") String orderBy
            ) {
        logger.debug("Request for all films: offset={} limit={} orderBy={}", offset, limit, orderBy);
        JSONArray arr;
        try {
            StringBuilder sb = new StringBuilder()
                                  .append("SELECT * ")
                                  .append("  FROM film")
                                  .append(" ORDER BY ").append(orderBy)
                                  .append(" LIMIT ").append(limit)
                                  .append(" OFFSET ").append(offset)
                                  ;
            arr = SqlHelper.queryArray(sb.toString());
        }
        catch (Exception e) {
            logger.error("getAll ERROR: " + e);
            arr = new JSONArray()
                     .put("ERROR: " + e);
        }
        return arr.toString(2);
    }

    @GET
    @Path("/film/{film_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOne(@PathParam("film_id") String film_id) {
        logger.debug("Request for film #{}", film_id);
        JSONObject obj;
        try {
            StringBuilder sb = new StringBuilder()
                                  .append("SELECT * ")
                                  .append("  FROM film")
                                  .append(" WHERE film_id=").append(film_id);
            obj = SqlHelper.queryObject(sb.toString());
        }
        catch (Exception e) {
            logger.error("getOne ERROR: " + e);
            obj = new JSONObject()
                     .put("ERROR", e.toString());
        }
        return obj.toString(2);
    }

}

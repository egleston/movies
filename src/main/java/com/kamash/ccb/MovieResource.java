package com.kamash.ccb;

import java.util.ArrayList;
import java.util.List;

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
             @QueryParam("title")   @DefaultValue("")      String title,
             @QueryParam("rating")  @DefaultValue("")      String rating,
             @QueryParam("orderBy") @DefaultValue("title") String orderBy,
             @QueryParam("limit")   @DefaultValue("")      String limit,
             @QueryParam("offset")  @DefaultValue("")      String offset
            ) {
        logger.debug("Request for all films: title={} orderBy={} limit={} offset={}", title, orderBy, limit, offset);
        JSONArray arr;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM film");
            List<String> whereClause = new ArrayList<>();
            if (!title.equals("")) {
                whereClause.add(String.format("UPPER(title) like '%%%s%%'", title.toUpperCase()));
            }
            if (!rating.equals("")) {
                whereClause.add(String.format("rating='%s'", rating.toUpperCase()));
            }
            if (whereClause.size() > 0) {
                sb.append(" WHERE ").append(String.join(" AND ", whereClause));
            }
            if (!orderBy.equals("")) {
                sb.append(" ORDER BY ").append(orderBy);
            }
            if (!limit.equals("")) {
                sb.append(" LIMIT ").append(limit);
            }
            if (!offset.equals("")) {
                sb.append(" OFFSET ").append(offset);
            }
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
    @Path("/detail/{film_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilm(@PathParam("film_id") String film_id) {
        logger.debug("Request for film #{}", film_id);
        JSONObject obj;
        try {
            StringBuilder sb = new StringBuilder()
                                  .append("SELECT * ")
                                  .append("  FROM film")
                                  .append(" WHERE film_id=").append(film_id);
            obj = SqlHelper.queryObject(sb.toString());
            sb = new StringBuilder()
                    .append("SELECT actor.first_name, actor.last_name, CONCAT(first_name, ' ', last_name) full_name")
                    .append("  FROM actor")
                    .append(" INNER JOIN film_actor ON actor.actor_id = film_actor.actor_id")
                    .append(" INNER JOIN film       ON film.film_id   = film_actor.film_id")
                    .append(" WHERE film.film_id = ").append(film_id);
            obj.put("actors", SqlHelper.queryArray(sb.toString()));
        }
        catch (Exception e) {
            logger.error("getOne ERROR: " + e);
            obj = new JSONObject()
                     .put("ERROR", e.toString());
        }
        return obj.toString(2);
    }

}

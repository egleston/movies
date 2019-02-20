package com.kamash.ccb;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("/")
public class MovieResource {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    private static boolean pretty = false;

    @GET
    @Path("/pretty")
    @Produces(MediaType.APPLICATION_JSON)
    public Response togglePretty() throws Exception {
        pretty = !pretty;
        return Response.seeOther(new URI("..")).build();
    }

    @GET
    @Path("/film")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(
             @QueryParam("title")    @DefaultValue("")      String title,
             @QueryParam("rating")   @DefaultValue("")      String rating,
             @QueryParam("category") @DefaultValue("")      String category,
             @QueryParam("orderBy")  @DefaultValue("title") String orderBy,
             @QueryParam("limit")    @DefaultValue("")      String limit,
             @QueryParam("offset")   @DefaultValue("")      String offset
            ) {
        logger.debug("Request for all films: title={} rating={} category={} orderBy={} limit={} offset={}", title, rating, category, orderBy, limit, offset);
        JSONArray arr;
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("SELECT film.*")
              .append("  FROM film");

            List<String> whereClause = new ArrayList<>();
            if (!title.equals("")) {
                whereClause.add(String.format("UPPER(title) like '%%%s%%'", title.toUpperCase()));
            }
            if (!rating.equals("")) {
                whereClause.add(String.format("rating='%s'", rating.toUpperCase()));
            }
            if (!category.equals("")) {
                sb.append(" INNER JOIN film_category ON film.film_id=film_category.film_id")
                  .append(" INNER JOIN category      ON film_category.category_id=category.category_id");
                whereClause.add(String.format("UPPER(category.name)='%s'", category.toUpperCase()));
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
            logger.error("            : " + sb);
            arr = new JSONArray();
        }
        return (pretty ? arr.toString(2) : arr.toString());
    }

    @GET
    @Path("/detail/{film_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFilm(@PathParam("film_id") String film_id) {
        logger.debug("Request for film #{}", film_id);
        JSONObject obj;
        try {
            List<Object> varList = new ArrayList<>();
            varList.add(new Integer(film_id));
            obj = SqlHelper.queryObject("SELECT * FROM film WHERE film_id = ?", varList);
            StringBuilder sb = new StringBuilder()
                                  .append("SELECT actor.first_name, actor.last_name, CONCAT(first_name, ' ', last_name) full_name")
                                  .append("  FROM actor")
                                  .append(" INNER JOIN film_actor ON actor.actor_id = film_actor.actor_id")
                                  .append(" INNER JOIN film       ON film.film_id   = film_actor.film_id")
                                  .append(" WHERE film.film_id = ?");
            obj.put("actors", SqlHelper.queryArray(sb.toString(), varList));
        }
        catch (Exception e) {
            logger.error("getOne ERROR: " + e);
            obj = new JSONObject();
        }
        return obj.toString(2);
    }

}

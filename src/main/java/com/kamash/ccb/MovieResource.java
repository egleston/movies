package com.kamash.ccb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONArray;

@Path("/")
public class MovieResource {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    @GET
    @Path("/")
    @Produces("application/json")
    public String getAll() {
        JSONArray arr;
        try {
            arr = SqlHelper.doQuery("select * from film limit 5");
        }
        catch (Exception e) {
            logger.error("getAll ERROR: " + e);
            arr = new JSONArray();
            arr.put("ERROR: " + e);
        }
        return arr.toString(2);
    }

}

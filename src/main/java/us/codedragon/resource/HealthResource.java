package us.codedragon.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by dajacobsen on 4/1/2016.
 */
@Path("health")
public class HealthResource {
	@GET
	@Produces("text/html")
	public String getHealth() {
		return "ok";
	}
}

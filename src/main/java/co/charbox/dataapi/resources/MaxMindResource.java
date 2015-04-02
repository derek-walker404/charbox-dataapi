package co.charbox.dataapi.resources;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import co.charbox.core.utils.Config;
import co.charbox.domain.mm.MaxMindService;
import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;

@Path("/mm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaxMindResource {

	private final MaxMindService mms;
	
	public MaxMindResource(MaxMindService mms) {
		this.mms = mms;
	}
	
	@Path("/{ip}")
	@GET
	@Timed
	public JsonNode getIp(@PathParam("ip") String ip) {
		return getConnectionInfo(ip);
	}
	
	private JsonNode getConnectionInfo(String ip) {
		ConnectionInfoModel conInfo = mms.get(ip);
		return conInfo != null
				? ResponseUtils.success(ResponseUtils.modelData(conInfo))
						: ResponseUtils.failure("Could not retrieve info for " + ip, 500);
	}
	
	@Path("/self")
	@GET
	@Timed
	public JsonNode getRequestersIp(@Context HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		try {
			InetAddress addr = InetAddress.getByName(ip);
			if (addr.isLoopbackAddress()) {
				ip = Config.get().getString("client.ip.override");
			} else {
				ip = addr.getHostAddress();
			}
			return getConnectionInfo(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ResponseUtils.failure("Unexpected exception.", 500);
	}
}

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
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;
import co.charbox.domain.model.mm.ConnectionInfoModel;

import com.codahale.metrics.annotation.Timed;
import com.tpofof.core.utils.Config;
import com.tpofof.dwa.error.HttpInternalServerErrorException;
import com.tpofof.dwa.resources.IDwaResource;
import com.tpofof.dwa.utils.ResponseUtils;

@Path("/mm")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaxMindResource implements IDwaResource {

	private final MaxMindService mms;
	private final ResponseUtils responseUtils;
	private final String overrideIp;
	
	@Autowired
	public MaxMindResource(MaxMindService mms, ResponseUtils responseUtils, Config config) {
		this.mms = mms;
		this.responseUtils = responseUtils;
		this.overrideIp = config.getString("location.resource.overrideIp", "127.0.0.1");
	}
	
	@Path("/{ip}")
	@GET
	@Timed
	public Response getIp(@PathParam("ip") String ip) throws HttpInternalServerErrorException {
		return responseUtils.success(responseUtils.modelData(getConnectionInfo(ip)));
	}
	
	private Response getConnectionInfo(String ip) throws HttpInternalServerErrorException {
		ConnectionInfoModel conInfo = mms.get(ip);
		if (conInfo == null) {
			throw new HttpInternalServerErrorException("Could not retrieve info for " + ip);
		}
		return responseUtils.success(responseUtils.modelData(conInfo));
	}
	
	@Path("/self")
	@GET
	@Timed
	public Response getRequestersIp(@Context HttpServletRequest request) throws HttpInternalServerErrorException {
		String ip = request.getRemoteAddr();
		try {
			InetAddress addr = InetAddress.getByName(ip);
			if (addr.isLoopbackAddress()) {
				ip = this.overrideIp;
			} else {
				ip = addr.getHostAddress();
			}
			return getConnectionInfo(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		throw new HttpInternalServerErrorException("Unexpected exception.");
	}
}

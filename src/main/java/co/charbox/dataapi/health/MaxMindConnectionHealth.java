package co.charbox.dataapi.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;

import com.tpofof.dwa.resources.IDwaHealthCheck;

@Component
public class MaxMindConnectionHealth extends IDwaHealthCheck {

	@Autowired private MaxMindService mms;
	
	@Override
	public String getName() {
		return "MaxMind-connectionHealth";
	}
	
	@Override
	protected Result check() throws Exception {
		return mms.wasLastRequestFailure()
				? Result.unhealthy(getMessage())
				: Result.healthy(getMessage());
	}

	private String getMessage() {
		return "Last failed: " + mms.getLastFailedTime() + " Last Healthy: " + mms.getLastHealthyTime();
	}
}

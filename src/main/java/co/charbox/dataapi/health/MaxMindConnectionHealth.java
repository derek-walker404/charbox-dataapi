package co.charbox.dataapi.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.mm.MaxMindService;

import com.codahale.metrics.health.HealthCheck;

@Component
public class MaxMindConnectionHealth extends HealthCheck {

	@Autowired private MaxMindService mms;
	
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

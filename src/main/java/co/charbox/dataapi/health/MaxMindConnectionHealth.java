package co.charbox.dataapi.health;

import co.charbox.domain.mm.MaxMindService;

import com.codahale.metrics.health.HealthCheck;

public class MaxMindConnectionHealth extends HealthCheck {

	private final MaxMindService mms;
	
	public MaxMindConnectionHealth(MaxMindService mms) {
		this.mms = mms;
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

package co.charbox.dataapi.health;

import co.charbox.domain.mm.MaxMindService;

import com.codahale.metrics.health.HealthCheck;

public class MaxMindAccountHealth extends HealthCheck {

	private final MaxMindService mms;
	private final int minRequestThreshold;
	
	public MaxMindAccountHealth(MaxMindService mms, int minRequestThreshold) {
		this.mms = mms;
		this.minRequestThreshold = minRequestThreshold;
	}

	@Override
	protected Result check() throws Exception {
		return mms.getRemainingRequests() > minRequestThreshold
				? Result.healthy(mms.getRemainingRequests() + " remaining.")
				: Result.unhealthy(mms.getRemainingRequests() + " remaining!");
	}
}

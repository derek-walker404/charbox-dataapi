package co.charbox.dataapi.health;

import com.codahale.metrics.health.HealthCheck;
import com.pofof.conmon.mm.MaxMindService;

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

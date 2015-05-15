package co.charbox.dataapi.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.core.mm.MaxMindService;

import com.codahale.metrics.health.HealthCheck;
import com.tpofof.core.utils.Config;

@Component
public class MaxMindAccountHealth extends HealthCheck {

	private final MaxMindService mms;
	private final int minRequestThreshold;
	
	@Autowired
	public MaxMindAccountHealth(MaxMindService mms, Config config) {
		this.mms = mms;
		this.minRequestThreshold = config.getInt("location.api.requests.minThreshold", 500);
	}

	@Override
	protected Result check() throws Exception {
		return mms.getRemainingRequests() > minRequestThreshold
				? Result.healthy(mms.getRemainingRequests() + " remaining.")
				: Result.unhealthy(mms.getRemainingRequests() + " remaining!");
	}
}

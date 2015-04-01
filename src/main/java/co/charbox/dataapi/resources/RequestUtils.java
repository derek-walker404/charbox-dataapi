package co.charbox.dataapi.resources;

import com.google.common.base.Optional;

public final class RequestUtils {

	private RequestUtils() { }
	
	public static int limit(Optional<Integer> limit) {
		return getOptionalInt(limit, -1);
	}
	
	public static int offset(Optional<Integer> offset) {
		return getOptionalInt(offset, 0);
	}
	
	public static int getOptionalInt(Optional<Integer> val, int defaultVal) {
		return val != null && val.isPresent() && val.get() > 0 ? val.get() : defaultVal;
	}
}

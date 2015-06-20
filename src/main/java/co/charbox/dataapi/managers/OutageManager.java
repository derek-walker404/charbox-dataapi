package co.charbox.dataapi.managers;

import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.OutageDAO;
import co.charbox.domain.model.Outage;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SearchWindow;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class OutageManager extends CharbotModelManager<Outage, OutageDAO> {

	private int defaultLimit;
	
	@Autowired
	public OutageManager(OutageDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("outage.limit", 200);
	}

	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}
	
	public ResultsSet<Outage> getRecentOutages() {
		return getRecentOutages(new DateTime().minusDays(1));
	}
	
	public ResultsSet<Outage> getRecentOutages(DateTime startTime) {
		return getDao().getRecent(startTime, getDefualtWindow());
	}
	
	public ResultsSet<Outage> getRecentOutages(String deviceId, DateTime startTime) {
		return getDao().getRecent(deviceId, startTime, getDefualtWindow());
	}
	
	public ResultsSet<Outage> getRecentOutages(DateTime startTime, SearchWindow window) {
		window = validateAndAmmendWindow(window);
		return getDao().getRecent(startTime, window);
	}
	
	public ResultsSet<Outage> getRecentOutages(String deviceId, DateTime startTime, SearchWindow window) {
		window = validateAndAmmendWindow(window);
		return getDao().getRecent(deviceId, startTime, window);
	}

	@Override
	protected boolean hasDefaultSort() {
		return true;
	}
	
	@Override
	protected SimpleSort getDefaultSort() {
		return SimpleSort.builder()
				.field("startTime")
				.direction(-1)
				.build();
	}

	public ResultsSet<Outage> getOutagesByDeviceId(SimpleSearchContext context,
			String deviceId) {
		return getDao().getOutagesByDeviceId(context, deviceId);
	}
	
	@Override
	protected Set<String> getDefaultValidSorts() {
		return Sets.newHashSet("startTime", "duration");
	}
}

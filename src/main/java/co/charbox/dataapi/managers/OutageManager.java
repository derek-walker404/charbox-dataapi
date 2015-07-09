package co.charbox.dataapi.managers;

import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.mysql.OutageDAO;
import co.charbox.domain.model.OutageModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class OutageManager extends CharbotModelManager<OutageModel, OutageDAO> {

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

	public ResultsSet<OutageModel> getByDeviceId(SimpleSearchContext context, Integer deviceId) {
		return getDao().findByDeviceId(context, deviceId);
	}
	
	@Override
	protected Set<String> getDefaultValidSorts() {
		return Sets.newHashSet("startTime", "duration");
	}
}

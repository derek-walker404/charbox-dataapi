package co.charbox.dataapi.managers;

import java.util.HashSet;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.data.mysql.PingResultsDAO;
import co.charbox.domain.model.PingResultModel;
import co.charbox.domain.model.RoleModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class PingResultsManager extends CharbotModelManager<PingResultModel, PingResultsDAO> {

	private int defaultLimit;
	
	@Autowired
	public PingResultsManager(PingResultsDAO dao, Config config) {
		super(dao);
		this.defaultLimit = config.getInt("ping_result.limit", 10);
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
	
	@Override
	protected Set<String> getDefaultValidSorts() {
		return Sets.newHashSet("startTime", "packetLoss", "avgLatency");
	}

	public ResultsSet<PingResultModel> getByDeviceId(CharbotSearchContext context, Integer deviceId) {
		validateSearchContext(context);
		return getDao().findByDeviceId(context, deviceId);
	}
	
	@Override
	protected void checkCanInsert(CharbotSearchContext context, PingResultModel model) {
		HashSet<RoleModel> expectedRoles = Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole(model.getDevice().getId()));
		check(context, expectedRoles);
	}
}

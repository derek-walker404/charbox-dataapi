package co.charbox.dataapi.managers;

import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.data.mysql.SstResultsDAO;
import co.charbox.domain.model.SstResultsModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.context.SimpleSort;
import com.tpofof.core.utils.Config;

@Component
public class SstResultManager extends CharbotModelManager<SstResultsModel, SstResultsDAO> {

	private int defaultLimit;
	@Autowired private TokenAuthManager tokenManager;
	
	@Autowired
	public SstResultManager(SstResultsDAO esDao, Config config) {
		super(esDao);
		this.defaultLimit = config.getInt("sst_result.limit", 20);
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
		return Sets.newHashSet("startTime", "downloadSpeed", "uploadSpeed", "pingDuration");
	}
	
	@Override
	public SstResultsModel insert(SimpleSearchContext context, SstResultsModel model) {
		model = super.insert(context, model);
		if (model != null) {
			tokenManager.deleteByToken(model.getDeviceToken());
		}
		return model;
	}

	public ResultsSet<SstResultsModel> getByDeviceId(SimpleSearchContext context, Integer deviceId) {
		validateSearchContext(context);
		return getDao().findByDeviceId(context, deviceId);
	}
}

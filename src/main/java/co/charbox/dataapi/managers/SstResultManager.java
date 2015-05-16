package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.SstResultEsDAO;
import co.charbox.dataapi.managers.auth.TokenAuthManager;
import co.charbox.domain.model.SstResults;

import com.tpofof.core.managers.AbstractEsModelManager;
import com.tpofof.core.utils.Config;

@Component
public class SstResultManager extends AbstractEsModelManager<SstResults, SstResultEsDAO> {

	private int defaultLimit;
	@Autowired private TokenAuthManager tokenManager;
	
	@Autowired
	public SstResultManager(SstResultEsDAO esDao, Config config) {
		super(esDao);
		this.defaultLimit = config.getInt("sst_result.limit", 20);
	}
	
	@Override
	public int getDefualtLimit() {
		return defaultLimit;
	}
	
	@Override
	public String getDefaultId() {
		return "";
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}
	
	@Override
	public SstResults insert(SstResults model) {
		model = super.insert(model);
		if (model != null) {
			tokenManager.deleteByToken(model.getDeviceToken());
		}
		return model;
	}
}
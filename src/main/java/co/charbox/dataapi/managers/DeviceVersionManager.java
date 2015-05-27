package co.charbox.dataapi.managers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.dataapi.data.elasticsearch.DeviceVersionDAO;
import co.charbox.domain.model.DeviceVersionModel;

import com.tpofof.core.data.dao.SearchWindow;
import com.tpofof.core.data.dao.SimpleSort;
import com.tpofof.core.managers.AbstractEsModelManager;

@Component
public class DeviceVersionManager extends AbstractEsModelManager<DeviceVersionModel, DeviceVersionDAO> {

	@Autowired
	public DeviceVersionManager(DeviceVersionDAO deviceVersionDao) {
		super(deviceVersionDao);
	}

	@Override
	public String getDefaultId() {
		return "";
	}
	
	@Override
	public int getDefualtLimit() {
		return 10; // TODO: config or setting
	}

	@Override
	protected boolean hasDefaultSort() {
		return true;
	}
	
	@Override
	public SimpleSort getDefaultSort() {
		return SimpleSort.builder()
				.field("versionSort")
				.direction(-1)
				.build();
	}

	public boolean canUpgrade(String version) {
		DeviceVersionModel latestVersion = getDao().find(SearchWindow.builder()
				.limit(1)
				.offset(0)
				.build(), getDefaultSort())
				.getResults()
				.get(0);
		DeviceVersionModel queryVersion = DeviceVersionModel.builder().version(version).build();
		return queryVersion.compareTo(latestVersion) < 0;
	}
}

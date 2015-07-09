package co.charbox.dataapi.managers;

import java.util.HashSet;
import java.util.Set;

import org.elasticsearch.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.mysql.DeviceVersionDAO;
import co.charbox.domain.model.DeviceVersionModel;

import com.tpofof.core.data.dao.context.SearchWindow;
import com.tpofof.core.data.dao.context.SimpleSearchContext;
import com.tpofof.core.data.dao.context.SimpleSort;

@Component
public class DeviceVersionManager extends CharbotModelManager<DeviceVersionModel, DeviceVersionDAO> {

	@Autowired
	public DeviceVersionManager(DeviceVersionDAO deviceVersionDao) {
		super(deviceVersionDao);
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
	
	private static final HashSet<String> DEFAULT_VALID_SORTS = Sets.newHashSet("versionSort");

	protected Set<String> getDefaultValidSorts() {
		return DEFAULT_VALID_SORTS;
	}

	public DeviceVersionModel canUpgrade(String version) {
		DeviceVersionModel latestVersion = getDao().find(SimpleSearchContext.builder()
				.window(SearchWindow.builder() // TODO: pass in context
						.limit(1)
						.offset(0)
						.build())
				.sort(getDefaultSort())
				.build())
				.getResults()
				.get(0);
		DeviceVersionModel queryVersion = DeviceVersionModel.builder().version(version).build();
		return queryVersion.compareTo(latestVersion) < 0 ? latestVersion : null;
	}
}

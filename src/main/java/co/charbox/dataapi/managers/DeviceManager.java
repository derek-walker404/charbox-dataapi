package co.charbox.dataapi.managers;

import java.util.List;
import java.util.Map;

import org.elasticsearch.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.data.CharbotSearchContext;
import co.charbox.domain.data.mysql.DeviceDAO;
import co.charbox.domain.model.DeviceConfigurationModel;
import co.charbox.domain.model.DeviceModel;
import co.charbox.domain.model.HeartbeatModel;
import co.charbox.domain.model.JobSchedule;
import co.charbox.domain.model.OutageModel;
import co.charbox.domain.model.PingResultModel;
import co.charbox.domain.model.RoleModel;
import co.charbox.domain.model.SstResultsModel;

import com.google.api.client.util.Maps;
import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.utils.Config;

@Component
public class DeviceManager extends CharbotModelManager<DeviceModel, DeviceDAO> {

	private int defualtLimit;
	
	@Autowired
	public DeviceManager(DeviceDAO deviceDao, Config config) {
		super(deviceDao);
		this.defualtLimit = config.getInt("device.limit", 10);
	}
	
	public DeviceModel register(CharbotSearchContext authContext, DeviceModel device) {
		DeviceConfigurationModel conf = getManProvider().getDeviceConfigurationManager().findByDeviceId(authContext, device.getId());
		if (conf == null) {
			Map<String, String> schedules = Maps.newHashMap();
			List<JobSchedule> jobs = JobSchedule.getAllJobs();
			for (JobSchedule job : jobs) {
				schedules.put(job.getName(), job.getSchedule());
			}
			conf = getManProvider().getDeviceConfigurationManager().insert(authContext, DeviceConfigurationModel.builder()
					.device(device)
					.registered(true)
					.schedules(schedules)
					.build());
		} else if (!conf.isRegistered()) {
			conf.setRegistered(true);
			conf = getManProvider().getDeviceConfigurationManager().updateRegistered(authContext, conf);
		}
		return conf == null ? null : device;
	}

	@Override
	public int getDefualtLimit() {
		return defualtLimit;
	}
	
	public HeartbeatModel heartbeat(CharbotSearchContext context, Integer deviceId, DateTime time, String ipAdress) {
		HeartbeatManager man = getManProvider().getHeartbeatManager();
		return man.insert(context, deviceId, time, ipAdress);
	}
	
	public HeartbeatModel getHeartbeat(CharbotSearchContext context, Integer deviceId) {
		return getManProvider().getHeartbeatManager().findByDeviceId(context, deviceId);
	}

	@Override
	protected boolean hasDefaultSort() {
		return false;
	}

	public ResultsSet<PingResultModel> getPingResults(CharbotSearchContext context, Integer deviceId) {
		return getManProvider().getPingResultsManager().getByDeviceId(context, deviceId);
	}

	public ResultsSet<OutageModel> getOutages(CharbotSearchContext context, Integer deviceId) {
		return getManProvider().getOutageManager().getByDeviceId(context, deviceId);
	}

	public ResultsSet<SstResultsModel> getSstResults(CharbotSearchContext context, Integer deviceId) {
		return getManProvider().getSstResultManager().getByDeviceId(context, deviceId);
	}
	
	protected void checkCanView(CharbotSearchContext context, DeviceModel model) {
		check(context, Sets.newHashSet(RoleModel.getAdminRole(), RoleModel.getDeviceRole(model.getId())));
	}
}

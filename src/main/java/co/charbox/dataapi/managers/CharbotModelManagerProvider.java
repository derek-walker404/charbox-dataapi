package co.charbox.dataapi.managers;

import org.springframework.stereotype.Component;

import com.tpofof.core.App;

import co.charbox.dataapi.managers.auth.DeviceAuthManager;
import co.charbox.dataapi.managers.auth.ServerAuthManager;
import co.charbox.dataapi.managers.auth.TokenAuthManager;

@Component
public class CharbotModelManagerProvider {

	public DeviceAuthManager getDeviceAuthManager() {
		return App.getContext().getBean(DeviceAuthManager.class);
	}
	
	public ServerAuthManager getServerAuthManager() {
		return App.getContext().getBean(ServerAuthManager.class);
	}
	
	public TokenAuthManager getTokenAuthManager() {
		return App.getContext().getBean(TokenAuthManager.class);
	}
	
	public DeviceConfigurationManager getDeviceConfigurationManager() {
		return App.getContext().getBean(DeviceConfigurationManager.class);
	}
	
	public DeviceManager getDeviceManager() {
		return App.getContext().getBean(DeviceManager.class);
	}
	
	public DeviceVersionManager getDeviceVersionManager() {
		return App.getContext().getBean(DeviceVersionManager.class);
	}
	
	public HeartbeatManager getHeartbeatManager() {
		return App.getContext().getBean(HeartbeatManager.class);
	}
	
	public OutageManager getOutageManager() {
		return App.getContext().getBean(OutageManager.class);
	}
	
	public PingResultsManager getPingResultsManager() {
		return App.getContext().getBean(PingResultsManager.class);
	}
	
	public SstResultManager getSstResultManager() {
		return App.getContext().getBean(SstResultManager.class);
	}
}

package co.charbox.dataapi;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tpofof.core.utils.Config;

@Configuration
public class CharbotDataApiApplicationConfiguration {

	@Autowired Config config;
	
	@Bean(name="client")
	public Client client() {
		TransportClient esClient = new TransportClient();
		String esHost = config.getString("es.url", "localhost");
		int esPort = config.getInt("es.port", 9300);
		esClient.addTransportAddress(new InetSocketTransportAddress(esHost, esPort));
		return esClient;
	}
}

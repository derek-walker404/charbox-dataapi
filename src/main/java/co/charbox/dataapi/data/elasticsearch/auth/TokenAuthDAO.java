package co.charbox.dataapi.data.elasticsearch.auth;

import org.elasticsearch.action.deletebyquery.DeleteByQueryResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.charbox.domain.model.auth.TokenAuthModel;

import com.tpofof.core.data.dao.ResultsSet;
import com.tpofof.core.data.dao.context.SearchWindow;
import com.tpofof.core.data.dao.es.AbstractElasticsearchDAO;
import com.tpofof.core.data.dao.es.EsQuery;
import com.tpofof.core.io.IO;
import com.tpofof.core.utils.Config;

@Component
public class TokenAuthDAO extends AbstractElasticsearchDAO<TokenAuthModel> {

	private IO io;
	private String index;
	private String type;
	
	@Autowired
	public TokenAuthDAO(Config config, Client client, IO io) {
		super(config, client);
		this.io = io;
		init(true);
	}
	
	@Override
	protected String getIndex() {
		if (index == null) {
			index = getConfig().getString("es.token_auth.index", "charbot_v0.1_tokenauth");
		}
		return index;
	}

	@Override
	protected String getType() {
		if (type == null) {
			type = getConfig().getString("es.token_auth.type", "tokauth");
		}
		return type;
	}

	@Override
	protected Class<TokenAuthModel> getModelClass() {
		return TokenAuthModel.class;
	}
	
	@Override
	protected String getMapping() {
		String filename = getConfig().getString("es.token_auth.mapping.name", "mappings/es.token_auth.mapping.json");
		return io.getContents(filename);
	}

	@Override
	protected boolean isRequiredIndex() {
		return true;
	}

	// TODO: shouldCreate() boolean call
	
	// TODO: populate on create
	
	public TokenAuthModel find(TokenAuthModel auth) {
		BoolQueryBuilder q = QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery("token", auth.getToken()))
				.must(QueryBuilders.termQuery("authAssetId", auth.getAuthAssetId()))
				.must(QueryBuilders.termQuery("serviceId", auth.getServiceId()));
		EsQuery esQuery = EsQuery.builder()
				.constraints(q)
				.limit(1)
				.offset(0)
				.build();
		ResultsSet<TokenAuthModel> authResults = find(esQuery);
		return authResults.getTotal() == 1 ? authResults.getResults().get(0) : null;
	}
	
	public TokenAuthModel findByToken(String token) {
		EsQuery q = EsQuery.builder()
				.constraints(QueryBuilders.termQuery("token", token))
				.limit(1)
				.offset(0)
				.build();
		ResultsSet<TokenAuthModel> authResults = find(q);
		return authResults.getTotal() == 1 ? authResults.getResults().get(0) : null;
	}

	public ResultsSet<TokenAuthModel> findExpired(SearchWindow window) {
		return find(EsQuery.builder()
				.constraints(QueryBuilders.rangeQuery("expiration").lte(new DateTime()))
				.limit(window.getLimit())
				.offset(window.getOffset())
				.build());
	}
	
	public int deleteExpired(SearchWindow window) {
		DeleteByQueryResponse response = getClient().prepareDeleteByQuery(getIndex())
				.setTypes(getType())
				.setQuery(QueryBuilders.rangeQuery("expiration").lte(new DateTime()))
				.execute()
				.actionGet();
		return response.contextSize();
	}
}

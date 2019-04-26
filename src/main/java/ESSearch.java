import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookIndexTemplate;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

public class ESSearch {

    private static String INDEX_NAME = "book";

    private static String INDEX_TYPE = "novel";

    public static TransportClient esClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "myes")
                .build();

        InetSocketTransportAddress master = new InetSocketTransportAddress(
                InetAddress.getByName("127.0.0.1"), 9300
        );

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(master);

        return client;
    }

    public static void main(String[] args) throws Exception {

        TransportClient client = esClient();

        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                     .should(QueryBuilders.matchQuery("title","入门"))
                                     .should(QueryBuilders.termQuery("word_count",5000));

        //这两种写法一样的
        QueryBuilder queryBuilder1 = QueryBuilders.boolQuery()
                .must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title","入门"))
                .should(QueryBuilders.termQuery("word_count",5000)));

        SearchResponse response = client.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE).setQuery(queryBuilder1).get();
        for (SearchHit hit : response.getHits()) {
            Map<String ,Object> map = hit.getSourceAsMap();
            for (Map.Entry<String, Object> entry: map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            System.out.println("----------------");
        }


    }
}
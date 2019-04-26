import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class ESScroll {

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

        SearchResponse scrollResponse = client.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
               .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setSize(2)//每次分页查询五条
                .setScroll(TimeValue.timeValueMinutes(1))
                .get();
        for (SearchHit hit : scrollResponse.getHits()) {
            System.out.println("最开始数据的ID :" + hit.getId());
        }
        long count = scrollResponse.getHits().getTotalHits();
        String scrollId  = scrollResponse.getScrollId();
        //System.out.println("共有 " + count + "条数据:::" + "scrollId为 " +  scrollId);

        int sum = 0;
        while (true) {
            //System.out.println("开始scrollId : " + scrollId);
            scrollResponse = client.prepareSearchScroll(scrollId)
                    .setScroll(TimeValue.timeValueMinutes(1))
                    .execute().actionGet();
            scrollId = scrollResponse.getScrollId();
            //System.out.println("结束scrollId : " + scrollId);
            for (SearchHit hit : scrollResponse.getHits()) {
                System.out.println("数据的ID :" + hit.getId());
            }
            int num = scrollResponse.getHits().getHits().length;
            if (num == 0)
                break;
            sum += num;
            System.out.println("总量"+count+" 已经查到"+sum);

        }


    }
}
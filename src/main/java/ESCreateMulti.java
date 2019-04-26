import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookIndexTemplate;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;


/**
 * 单条：插入数据
 */
public class ESCreateMulti {

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
        ObjectMapper objectMapper = new ObjectMapper();


        BookIndexTemplate bookIndexTemplate = new BookIndexTemplate();
        bookIndexTemplate.setPublish_date(new Date());
        bookIndexTemplate.setAuthor("批量测试一");
        bookIndexTemplate.setTitle("2019年4月26日");
        bookIndexTemplate.setWord_count(3500);


        BookIndexTemplate bookIndexTemplat2 = new BookIndexTemplate();
        bookIndexTemplat2.setPublish_date(new Date());
        bookIndexTemplat2.setAuthor("批量测试一");
        bookIndexTemplat2.setTitle("今天天气好");
        bookIndexTemplat2.setWord_count(4500);

        BookIndexTemplate bookIndexTemplat3 = new BookIndexTemplate();
        bookIndexTemplat3.setPublish_date(new Date());
        bookIndexTemplat3.setAuthor("批量测试一");
        bookIndexTemplat3.setTitle("哈哈很高兴");
        bookIndexTemplat3.setWord_count(5500);


        BulkRequestBuilder bulkRequest=client.prepareBulk();
        bulkRequest.add(client.prepareIndex(INDEX_NAME,INDEX_TYPE).setSource(objectMapper.writeValueAsBytes(bookIndexTemplate), XContentType.JSON));
        bulkRequest.add(client.prepareIndex(INDEX_NAME,INDEX_TYPE).setSource(objectMapper.writeValueAsBytes(bookIndexTemplat2), XContentType.JSON));
        bulkRequest.add(client.prepareIndex(INDEX_NAME,INDEX_TYPE).setSource(objectMapper.writeValueAsBytes(bookIndexTemplat3), XContentType.JSON));

        BulkResponse bulkItemResponses = bulkRequest.execute().actionGet();



        // status has stored current instance statement.
        RestStatus status = bulkItemResponses.status();

        client.close();


    }
}
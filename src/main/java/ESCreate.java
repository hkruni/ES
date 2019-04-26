import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookIndexTemplate;
import org.elasticsearch.action.delete.DeleteResponse;
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
public class ESCreate {

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
        //bookIndexTemplate.setId(1L);
        bookIndexTemplate.setPublish_date(new Date());
        bookIndexTemplate.setAuthor("上班");
        bookIndexTemplate.setTitle("很辛苦");
        bookIndexTemplate.setWord_count(3500);

        IndexResponse indexResponse = client.prepareIndex(INDEX_NAME, INDEX_TYPE)
                .setSource(objectMapper.writeValueAsBytes(bookIndexTemplate), XContentType.JSON)
                .get();

        // Index name
        String _index = indexResponse.getIndex();
        // Type name
        String _type = indexResponse.getType();
        // Document ID (generated or not)
        String _id = indexResponse.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = indexResponse.getVersion();
        // status has stored current instance statement.
        RestStatus status = indexResponse.status();

        client.close();


    }
}
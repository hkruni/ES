import model.BookIndexTemplate;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

public class ES {

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
        //on startup
//        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        BookIndexTemplate bookIndexTemplate = new BookIndexTemplate();
//        bookIndexTemplate.setId(17L);
//        bookIndexTemplate.setPublishDate(new Date());
//        bookIndexTemplate.setAuthor("测试");
//        bookIndexTemplate.setTitle("标题的测试");
//        bookIndexTemplate.setWordCount(3500);
//
//        IndexResponse response = client.prepareIndex(INDEX_NAME, INDEX_TYPE)
//                .setSource(objectMapper.writeValueAsBytes(bookIndexTemplate), XContentType.JSON)
//                .get();
//
//        client.close();
        //on startup
        TransportClient client = esClient();
        //继续添加其他地址

        DeleteResponse response = client.prepareDelete("book", "novel", "1").get();
        System.out.println(response.toString());
        //on shutdown
        client.close();
    }
}
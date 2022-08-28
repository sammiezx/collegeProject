package database;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class Elastic {
    public RestClient restClient;
    private ElasticsearchTransport transport;
    public ElasticsearchClient client;

    public Elastic(){
        restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();

        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchClient(transport);
    }
}

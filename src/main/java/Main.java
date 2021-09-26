
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        // mapper
        ObjectMapper mapper = new ObjectMapper();
        // httpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();
        // стартовая строка запроса
        HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
        // заголовки запроса
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        // ответ от сервера
        CloseableHttpResponse response = httpClient.execute(request);
        // разбираем ответ, получаем отфильтрованный список объектов
        List<CatFact> facts =
                mapper.readValue(response.getEntity().getContent(), new TypeReference<List<CatFact>>() {})
                .stream()
                .filter(x->x.getUpvotes() > 0).collect(Collectors.toList());
        // вывод в консоль
        facts.forEach(System.out::println);
        // закрываем клиента
        httpClient.close();
    }

}

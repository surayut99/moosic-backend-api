package com.backend.service.services.http;

import com.backend.service.exceptions.MoosicException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class HttpRequestPost extends HttpPost {
  private final Logger logger = LoggerFactory.getLogger(HttpRequestGet.class);

  public HttpRequestPost(URI uri) {
    super(uri);
  }

  public HttpRequestPost(String uri) {
    super(uri);
  }

  public JSONObject execute() throws IOException, MoosicException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    JSONObject jsonObject = new JSONObject();

    try (CloseableHttpResponse response = httpClient.execute(this)) {
      String result = EntityUtils.toString(response.getEntity());
      jsonObject = new JSONObject(result);

      if (response.getStatusLine().getStatusCode() >= 400) {
        logger.info(String.format("URI: %s", this.getURI().toString()));
        logger.info(String.format("Header: %s", Arrays.toString(this.getAllHeaders())));
        logger.info(String.format("Body: %s", this.getEntity().toString()));
        logger.error(jsonObject.toString());
        throw new MoosicException("Something went wrong when connect to other service", HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } catch (IOException e) {
      logger.error(e.getMessage());
    } finally {
      httpClient.close();
    }
    return jsonObject;
  }
}

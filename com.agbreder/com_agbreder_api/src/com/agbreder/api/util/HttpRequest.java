package com.agbreder.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicNameValuePair;

import com.agbreder.api.ServerException;

/**
 * Classe responsável por realizar uma requisição http
 * 
 * 
 * @author Bernardo Breder
 */
public abstract class HttpRequest {

  /**
   * Envia uma notificação para um serviço http
   * 
   * @param client
   * @param path
   * @return objeto json
   * @throws URISyntaxException
   * @throws IOException
   * @throws ServerException
   */
  public static <E> E get(HttpClient client, String path)
    throws URISyntaxException, IOException, ServerException {
    return get(client, path, null, null);
  }

  /**
   * Envia uma notificação para um serviço http
   * 
   * @param client
   * @param path
   * @param input
   * @return objeto json
   * @throws URISyntaxException
   * @throws IOException
   * @throws ServerException
   */
  public static <E> E post(HttpClient client, String path, InputStream input)
    throws URISyntaxException, IOException, ServerException {
    return post(client, path, null, null, input);
  }

  /**
   * Envia uma notificação para um serviço http
   * 
   * @param client
   * @param path
   * @param keys
   * @param values
   * @return objeto json
   * @throws URISyntaxException
   * @throws IOException
   * @throws ServerException
   */
  @SuppressWarnings("unchecked")
  public static <E> E get(HttpClient client, String path, String[] keys,
    Object[] values) throws URISyntaxException, IOException, ServerException {
    URI uri = url(path, keys, values);
    HttpGet httpget = new HttpGet(uri);
    HttpResponse response = client.execute(httpget);
    HttpEntity entity = response.getEntity();
    if (entity != null) {
      InputStream entityInput = entity.getContent();
      String base64 = new String(InputStreamUtil.getBytes(entityInput));
      Object result = Json64.decode(base64);
      if (Throwable.class.isInstance(result)) {
        throw new ServerException((Throwable) result);
      }
      return (E) result;
    }
    return null;
  }

  /**
   * Envia uma notificação para um serviço http
   * 
   * @param client
   * @param path
   * @param keys
   * @param values
   * @param input
   * @return objeto json
   * @throws URISyntaxException
   * @throws IOException
   * @throws ServerException
   */
  @SuppressWarnings("unchecked")
  public static <E> E post(HttpClient client, String path, String[] keys,
    Object[] values, InputStream input) throws URISyntaxException, IOException,
    ServerException {
    URI uri = url(path, keys, values);
    HttpEntity entity = new InputStreamEntity(input, -1);
    HttpPost httppost = new HttpPost(uri);
    httppost.setEntity(entity);
    HttpResponse response = client.execute(httppost);
    entity = response.getEntity();
    if (entity != null) {
      InputStream entityInput = entity.getContent();
      String base64 = new String(InputStreamUtil.getBytes(entityInput));
      Object result = Json64.decode(base64);
      if (Throwable.class.isInstance(result)) {
        throw new ServerException((Throwable) result);
      }
      return (E) result;
    }
    return null;
  }

  /**
   * Constroi a url
   * 
   * @param path
   * @param keys
   * @param values
   * @return url
   * @throws URISyntaxException
   */
  private static URI url(String path, String[] keys, Object[] values)
    throws URISyntaxException {
    List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    if (keys != null) {
      if (values == null || keys.length != values.length) {
        throw new IllegalArgumentException(
          "values are null or with different number of elements");
      }
      for (int n = 0; n < keys.length; n++) {
        String key = keys[n];
        Object value = values[n];
        qparams.add(new BasicNameValuePair(key, value.toString()));
      }
    }
    String params = URLEncodedUtils.format(qparams, "UTF-8");
    String qpath = "/agbreder/" + path;
    URI uri =
      URIUtils.createURI("http", "localhost:8080", -1, qpath, params, null);
    return uri;
  }

}

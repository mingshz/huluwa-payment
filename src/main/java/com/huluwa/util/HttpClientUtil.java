package com.huluwa.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 */
public class HttpClientUtil {

	/**
	 * 普通http
	 */
	public final static String HTTP_TYPE_COMMON = "1";
	/**
	 * https无校验
	 */
	public final static String HTTP_TYPE_AUTH_NONE = "2";
	/**
	 * http单向认证
	 */
	public final static String HTTP_TYPE_AUTH_SINGLE = "3";
	/**
	 * http双向认证
	 */
	public final static String HTTP_TYPE_AUTH_TWO = "4";

	/**
	 * 获取httpClient
	 *
	 * @param httpType
	 * @return
	 */
	public static HttpClient createHttpClient(String httpType) {
		if (HttpClientUtil.HTTP_TYPE_COMMON.equals(httpType)) {
			return org.apache.http.impl.client.HttpClients.createDefault();
		} else if (HttpClientUtil.HTTP_TYPE_AUTH_NONE.equals(httpType)) {
			return createAuthNonHttpClient();
		} else if (HttpClientUtil.HTTP_TYPE_AUTH_SINGLE.equals(httpType)) {
			throw new UnsupportedOperationException("");
		} else if (HttpClientUtil.HTTP_TYPE_AUTH_TWO.equals(httpType)) {
			throw new UnsupportedOperationException("");
		}
		throw new UnsupportedOperationException("");
	}

	public static HttpClient createAuthNonHttpClient() {
		SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(100000)
				.build();
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
				.<ConnectionSocketFactory> create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF);
		// 指定信任密钥存储对象和连接套接字工厂
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			SSLContext sslContext = SSLContexts.custom().useTLS()
					.loadTrustMaterial(trustStore, new AnyTrustStrategy())
					.build();
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(
					sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registryBuilder.register("https", sslSF);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		// 设置连接参数
		ConnectionConfig connConfig = ConnectionConfig.custom()
				.setCharset(Charset.forName("utf-8")).build();
		// 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				registry);
		connManager.setDefaultConnectionConfig(connConfig);
		connManager.setDefaultSocketConfig(socketConfig);
		// 指定cookie存储对象
		BasicCookieStore cookieStore = new BasicCookieStore();
		return HttpClientBuilder.create().setDefaultCookieStore(cookieStore)
				.setConnectionManager(connManager).build();
	}

	private static class AnyTrustStrategy implements TrustStrategy {

		@Override
		public boolean isTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			return true;
		}

	}

	/**
	 * 发送json格式请求到指定地址
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String sendRequest(String url, String json,String contentType) {
		HttpClient httpClient = null;
		HttpResponse resp = null;
		String resultContent = null;
		try {
			HttpPost httpPost = new HttpPost(
					"url");
			httpPost.setHeader("Content-Type", contentType);
			String requestContent = "{}";// "{\"mchId\":\"000100000000003\",\"outTradeNo\":\"04201609200000000001\",\"queryType\":\"1\",\"tradeType\":\"cs.trade.single.query\",\"version\":\"1.2\",\"sign\":\"7A5F5E258406476790855C29C0979626\"}";
			HttpEntity postEntity = new StringEntity(requestContent, "utf-8");
			httpPost.setEntity(postEntity);
			httpClient = HttpClientUtil.createAuthNonHttpClient();
			resp = httpClient.execute(httpPost);
			HttpEntity entity = resp.getEntity();
			resultContent = EntityUtils.toString(entity);
			System.out.println("返回结果:" + resultContent);
		} catch (Exception e) {
			throw new RuntimeException("请求异常", e);
		} finally {
			HttpClientUtils.closeQuietly(resp);
			HttpClientUtils.closeQuietly(httpClient);
		}
		return resultContent;
	}

	/**
	 * https 不验证证书
	 * 
	 * @param httpClient
	 */
	public static void wrapClient(HttpClient httpClient) {
		try {
			X509TrustManager xtm = new X509TrustManager() { // 创建TrustManager
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			// TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			// 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			// 创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			// 通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry()
					.register(new Scheme("https", 443, socketFactory));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
	}

}

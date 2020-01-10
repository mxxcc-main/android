//package com.qy.ccm.utils;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.net.ssl.SSLContext;
//
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.NoHttpResponseException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.config.SocketConfig;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContexts;
//import org.apache.http.conn.ssl.TrustStrategy;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//
///**
// * Description:http工具类
// *
// * @author: ydd
// * @date: 2018年8月16日 上午11:51:13
// */
//@SuppressWarnings("deprecation")
//public class HttpClient {
//	private static String charset = "utf-8";
//	public static String cookies = "";
//	private static final String CHARSET = "UTF-8";
//
//    @SuppressWarnings("unused")
//	public static String doPostJson(final Map<String, Object> map, final Map<String, Object> headerMap, final String url) {
//        CloseableHttpClient httpClient = null;
//        HttpPost httpPost = null;
//        String result = null;
//        CloseableHttpResponse response = null;
//        boolean hasError = false;
//        do {
//            hasError = false;
//            Label_0357: {
//                try {
//                    httpClient = HttpClients.custom().build();
//                    httpPost = new HttpPost(url);
//                    httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000).setConnectionRequestTimeout(600000).setSocketTimeout(600000).build());
//                    final List<NameValuePair> list = new ArrayList<NameValuePair>();
//                    final StringEntity entity = new StringEntity(JSON.toJSONString(map), HttpClient.charset);
//                    entity.setContentEncoding("UTF-8");
//                    entity.setContentType("application/json");
//                    httpPost.setEntity(entity);
//                    if (headerMap != null && !headerMap.isEmpty()) {
//                        for (final Entry<String, Object> m : headerMap.entrySet()) {
//                            httpPost.setHeader(m.getKey(), m.getValue().toString());
//                        }
//                    }
//                    response = httpClient.execute((HttpUriRequest)httpPost);
//                    final int statusCode = response.getStatusLine().getStatusCode();
//                    if (response == null) {
//                        break Label_0357;
//                    }
//                    final HttpEntity resEntity = response.getEntity();
//                    if (resEntity != null) {
//                        result = EntityUtils.toString(resEntity, HttpClient.charset);
//                    }
//                }
//                catch (NoHttpResponseException ex) {
//                    ex.printStackTrace();
//                    hasError = true;
//                }
//                catch (Exception ex2) {
//                    ex2.printStackTrace();
//                    hasError = true;
//                }
//                finally {
//                    if (response != null) {
//                        try {
//                            response.close();
//                        }
//                        catch (IOException ex3) {}
//                    }
//                    if (httpClient != null) {
//                        try {
//                            httpClient.close();
//                        }
//                        catch (IOException ex4) {}
//                    }
//                }
//            }
//            if (response != null) {
//                try {
//                    response.close();
//                }
//                catch (IOException ex5) {}
//            }
//            if (httpClient != null) {
//                try {
//                    httpClient.close();
//                }
//                catch (IOException ex6) {}
//            }
//        } while (hasError);
//        return result;
//    }
//
//	public static String doPost(Map<String, Object> map, Map<String, Object> headerMap, String url) {
//
//		BufferedReader buffer = null;
//		String result = "";
//		try {
//			CloseableHttpClient httpclient = HttpClients.createDefault();
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();// 设置请求和传输超时时间
//			HttpPost httpPost = new HttpPost(url);
//			httpPost.setConfig(requestConfig);
//			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
//			httpPost.addHeader(HTTP.CONTENT_TYPE, "text/json");
//			System.out.println(JSONObject.toJSONString(map));
//			StringEntity se = new StringEntity(JSONObject.toJSONString(map), "utf-8");
//			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			httpPost.setEntity(se);
//			CloseableHttpResponse response = httpclient.execute(httpPost);
//			buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
//			result = buffer.readLine();
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				buffer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//
//	@SuppressWarnings("unused")
//	public static String post(final Map<String, Object> map, final Map<String, Object> headerMap, final String url) {
//		CloseableHttpClient httpClient = null;
//		HttpPost httpPost = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//		boolean hasError = false;
//		do {
//			hasError = false;
//			Label_0424: {
//				try {
//					httpClient = HttpClients.custom().build();
//					httpPost = new HttpPost(url);
//					httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000)
//							.setConnectionRequestTimeout(600000).setSocketTimeout(600000).build());
//					final List<NameValuePair> list = new ArrayList<NameValuePair>();
//					for (final Entry<String, Object> elem : map.entrySet()) {
//						list.add(new BasicNameValuePair(elem.getKey(), elem.getValue().toString()));
//					}
//					if (list.size() > 0) {
//						final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, CHARSET);
//						httpPost.setEntity(entity);
//					}
//					if (headerMap != null && !headerMap.isEmpty()) {
//						for (final Entry<String, Object> m : headerMap.entrySet()) {
//							httpPost.setHeader(m.getKey(), m.getValue().toString());
//						}
//					}
//					response = httpClient.execute((HttpUriRequest) httpPost);
//					final int statusCode = response.getStatusLine().getStatusCode();
//					if (response == null) {
//						break Label_0424;
//					}
//					final HttpEntity resEntity = response.getEntity();
//					if (resEntity != null) {
//						result = EntityUtils.toString(resEntity, CHARSET);
//					}
//				} catch (NoHttpResponseException ex) {
//					ex.printStackTrace();
//					hasError = true;
//				} catch (Exception ex2) {
//					ex2.printStackTrace();
//					hasError = true;
//				} finally {
//					if (response != null) {
//						try {
//							response.close();
//						} catch (IOException ex3) {
//						}
//					}
//					if (httpClient != null) {
//						try {
//							httpClient.close();
//						} catch (IOException ex4) {
//						}
//					}
//				}
//			}
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException ex5) {
//				}
//			}
//			if (httpClient != null) {
//				try {
//					httpClient.close();
//				} catch (IOException ex6) {
//				}
//			}
//		} while (hasError);
//		return result;
//	}
//
//	@SuppressWarnings("unused")
//	public static String doGet(Map<String, Object> headersMap, String url) throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpGet httpGet = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//		String vd = "";
//		hasError = false;
//		try {
//			// 不使用代理
//			httpClient = HttpClients.custom().build();
//
//			httpGet = new HttpGet(url);
//
//			httpGet.setConfig(RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(60000)
//					.setSocketTimeout(60000).build());
//
//			// 请求头参数
//			for (Entry<String, Object> m : headersMap.entrySet()) {
//				httpGet.setHeader(m.getKey(), m.getValue().toString());
//			}
//
//			response = httpClient.execute(httpGet);
//			if (response != null) {
//				HttpEntity resEntity = response.getEntity();
//				if (resEntity != null) {
//					result = EntityUtils.toString(resEntity, charset);
//				}
//			}
//		} catch (org.apache.http.NoHttpResponseException ex) {
//			log.error("http is error:" + url, ex);
//			hasError = true;
//		} catch (Exception ex) {
//			log.error("http is error:" + url, ex);
//			hasError = true;
//		} finally {
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//				}
//			}
//			if (httpClient != null) {
//				try {
//					httpClient.close();
//				} catch (IOException e) {
//				}
//			}
//		}
//		// result = vd;
//		return result;
//	}
//
//	@SuppressWarnings("unused")
//	public static String doGet(String url) throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpGet httpGet = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//
//		String vd = "";
//		hasError = false;
//		try {
//			httpClient = HttpClients.custom().build();
//
//			httpGet = new HttpGet(url);
//			httpGet.setConfig(RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(50000).setSocketTimeout(50000).build());
//
//			response = httpClient.execute(httpGet);
//			Header[] allHeaders = response.getAllHeaders();
//
//			if (response != null) {
//				HttpEntity resEntity = response.getEntity();
//				if (resEntity != null) {
//					result = EntityUtils.toString(resEntity, charset);
//				}
//			}
//		} catch (org.apache.http.NoHttpResponseException ex) {
//			log.error("http is error:" + url, ex);
//			hasError = true;
//		} catch (Exception ex) {
//			log.error("http is error:" + url, ex);
//			hasError = true;
//		} finally {
//			if (response != null) {
//				try {
//					response.close();
//				} catch (IOException e) {
//				}
//			}
//			if (httpClient != null) {
//				try {
//					httpClient.close();
//				} catch (IOException e) {
//				}
//			}
//		}
//		// result = vd;
//		return result;
//	}
//
//	public static String doGetGbk(String url) throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpGet httpGet = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//
//		do {
//			hasError = false;
//			try {
//
//				httpClient = HttpClients.custom().build();
//
//				httpGet = new HttpGet(url);
//				httpGet.setConfig(RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(10000)
//						.setSocketTimeout(50000).build());
//
//				response = httpClient.execute(httpGet);
//
//				if (response != null) {
//					HttpEntity resEntity = response.getEntity();
//
//					if (resEntity != null) {
//						result = EntityUtils.toString(resEntity, "gbk");
//					}
//				}
//			} catch (org.apache.http.NoHttpResponseException ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} finally {
//				if (response != null) {
//					try {
//						response.close();
//					} catch (IOException e) {
//					}
//				}
//				if (httpClient != null) {
//					try {
//						httpClient.close();
//					} catch (IOException e) {
//					}
//				}
//			}
//		} while (hasError);
//		return result;
//	}
//
//	public static String doGetAsSSL(String url) throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpGet httpGet = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//
//		do {
//			hasError = false;
//			try {
//
//				httpGet = new HttpGet(url);
//				httpGet.setConfig(RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(6000).setSocketTimeout(6000).build());
//
//				response = createHttpsClient().execute(httpGet);
//
//				if (response != null) {
//					HttpEntity resEntity = response.getEntity();
//
//					if (resEntity != null) {
//						result = EntityUtils.toString(resEntity, charset);
//					}
//				}
//			} catch (org.apache.http.NoHttpResponseException ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} finally {
//				if (response != null) {
//					try {
//						response.close();
//					} catch (IOException e) {
//					}
//				}
//				if (httpClient != null) {
//					try {
//						httpClient.close();
//					} catch (IOException e) {
//					}
//				}
//			}
//		} while (hasError);
//		return result;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unused" })
//	public static String doGetAsSSL(Map<String, Object> headerMap, String url) throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpGet httpGet = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//
//		do {
//			hasError = false;
//			try {
//
//				Iterator iterator = headerMap.entrySet().iterator();
//				httpGet = new HttpGet(url);
//				httpGet.setConfig(RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(6000).setSocketTimeout(6000).build());
//				// 请求头参数
//				if (headerMap != null && !headerMap.isEmpty()) {
//					for (Entry<String, Object> m : headerMap.entrySet()) {
//						httpGet.setHeader(m.getKey(), m.getValue().toString());
//					}
//				}
//				response = createHttpsClient().execute(httpGet);
//
//				if (response != null) {
//					HttpEntity resEntity = response.getEntity();
//
//					if (resEntity != null) {
//						result = EntityUtils.toString(resEntity, charset);
//					}
//				}
//			} catch (org.apache.http.NoHttpResponseException ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} finally {
//				if (response != null) {
//					try {
//						response.close();
//					} catch (IOException e) {
//					}
//				}
//				if (httpClient != null) {
//					try {
//						httpClient.close();
//					} catch (IOException e) {
//					}
//				}
//			}
//		} while (hasError);
//		return result;
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static String doPostAsSSL(Map<String, Object> map, Map<String, Object> headerMap, String url)
//			throws Exception {
//		CloseableHttpClient httpClient = null;
//		HttpPost httpPost = null;
//		String result = null;
//		CloseableHttpResponse response = null;
//
//		boolean hasError = false;
//
//		do {
//			hasError = false;
//			try {
//
//				httpPost = new HttpPost(url);
//
//				httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000).setConnectionRequestTimeout(600000).setSocketTimeout(600000).build());
//
//				// 设置参数
//				List<NameValuePair> list = new ArrayList<NameValuePair>();
//				Iterator iterator = map.entrySet().iterator();
//				while (iterator.hasNext()) {
//					Entry<String, String> elem = (Entry<String, String>) iterator.next();
//					list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
//				}
//				if (list.size() > 0) {
//					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
//					httpPost.setEntity(entity);
//				}
//
//				// 请求头参数
//				if (headerMap != null && !headerMap.isEmpty()) {
//					for (Entry<String, Object> m : headerMap.entrySet()) {
//						httpPost.setHeader(m.getKey(), m.getValue().toString());
//					}
//				}
//
//				response = createHttpsClient().execute(httpPost);
//				if (response != null) {
//					HttpEntity resEntity = response.getEntity();
//					if (resEntity != null) {
//						result = EntityUtils.toString(resEntity, charset);
//					}
//				}
//			} catch (org.apache.http.NoHttpResponseException ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} catch (Exception ex) {
//				ex.printStackTrace();
//				hasError = true;
//			} finally {
//				if (response != null) {
//					try {
//						response.close();
//					} catch (IOException e) {
//					}
//				}
//				if (httpClient != null) {
//					try {
//						httpClient.close();
//					} catch (IOException e) {
//					}
//				}
//			}
//		} while (hasError);
//		return result;
//	}
//
//	private static CloseableHttpClient createHttpsClient() {
//		// 创建httpClient
//		CloseableHttpClient client = null;
//		try {
//			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null, new TrustStrategy() {
//				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					// 信任所有
//					return true;
//				}
//			}).build();
//
//			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
//
//			Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create().register("https", sslConnectionSocketFactory).build();
//
//			// 设置连接池，配置过期时间为20s。
//			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
//			cm.setMaxTotal(500);
//			cm.setDefaultMaxPerRoute(350);
//
//			SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).setSoTimeout(20000).build();
//			cm.setDefaultSocketConfig(socketConfig);
//
//			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(20000).setConnectTimeout(20000).setSocketTimeout(20000).build();
//
//			client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//		}
//
//		return client;
//	}
//
//}

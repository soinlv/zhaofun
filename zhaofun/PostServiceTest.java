package com.qcloud.weapp.test.zhaofun;

import static org.junit.Assert.*;

import java.io.IOException;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.qcloud.weapp.Configuration;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.ConfigurationManager;
import com.qcloud.weapp.post.PostService;
import com.qcloud.weapp.test.HttpMock;
import com.qcloud.weapp.test.tunnel.TunnelServiceTestHelper;

public class PostServiceTest {
	private TunnelServiceTestHelper helper = new TunnelServiceTestHelper();
	
	@Before
	public void setup() {
		Configuration config = new Configuration();
		config.setServerHost("87372966.qcloud.la");
		config.setAuthServerUrl("http://10.105.31.36/mina_auth/");
		config.setTunnelServerUrl("https://87372966.ws.qcloud.la");
		config.setTunnelSignatureKey("test key");
		config.setNetworkTimeout(1000);
		try {
			ConfigurationManager.setup(config);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddAPostByOpenId() throws JSONException, ConfigurationException, IOException {
		HttpMock httpMock = helper.createTunnelHttpMock("POST");
		httpMock.setRequestBody(helper.buildPacket(new JSONObject()
				.put("title", "Junit test title")
				.put("content", "Junit test content")
				.put("type", "1B1B")
				.put("city", "seattle")
				.toString()
			));
		PostService postService = new PostService(httpMock.request, httpMock.response);
		//postService.handlePost("abcdef8");
	}
	
	@Test
	public void testGetPostsByOpenId() throws IOException {
		HttpMock httpMock = helper.createTunnelHttpMock("GET");
		PostService postService = new PostService(httpMock.request, httpMock.response);
		//postService.handleGet("abcdef8");
	}

}

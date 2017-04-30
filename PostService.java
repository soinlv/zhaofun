package com.qcloud.weapp.post;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.phpdao.dao.impl.PostDAOImpl;
import com.phpdao.dao.impl.UserDAOImpl;
import com.phpdao.domain.Post;
import com.qcloud.weapp.ConfigurationException;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.UserInfo;
import com.qcloud.weapp.tunnel.TunnelHandleOptions;

public class PostService {
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private static final Logger LOGGER = Logger.getLogger(PostService.class.getName());
	
	public PostService(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public void handle(TunnelHandleOptions options) throws ConfigurationException
	, JSONException, IOException {
		UserInfo user = null;
		if (options != null && options.isCheckLogin()) {
			try {
				LoginService loginService = new LoginService(request, response);
				user = loginService.check();
			} catch (Exception e) {
				PostServiceHelper.writeJson(response, PostServiceHelper.getJsonForError(e));
				return;
			}
		}
		if (user == null) return;
		if (request.getMethod().toUpperCase() == "GET") {
			handleGet(user.getOpenId());
		}
		else if (request.getMethod().toUpperCase() == "POST") {
			handlePost(user.getOpenId());
		}
		else if (request.getMethod().toUpperCase() == "DELETE") {
			handleDelete(user.getOpenId());
		}
		else if (request.getMethod().toUpperCase() == "PUT") {
			handleUpdate(user.getOpenId());
		}
	}
	
	private void handleUpdate(String openId) {
		String data = PostServiceHelper.getDataFromRequest(request, response);
		// parse data
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(data);
			Post post = getPostFromJsonData(dataJson, openId);
			PostDAOImpl postDao = new PostDAOImpl();
			postDao.update(post);
			JSONObject response = new JSONObject();
			response.put("code", 0);
			response.put("message", "OK");
			PostServiceHelper.writeJson(this.response, response);
		} catch (JSONException e) {
			JSONObject response = new JSONObject();
			try {
				response.put("code", 9004);
				response.put("message", "Bad Request - 无法解析的数据包");
			} catch (JSONException e1) {
				LOGGER.log(Level.SEVERE, "fail to put attribut in response", e);
			}
			PostServiceHelper.writeJson(this.response, response);
			LOGGER.log(Level.SEVERE, "fail to parse Json with data " + data, e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "fail to save post with openId "
					+ openId, e);
		}
		
	}

	private void handleDelete(String openId) {
		String data = PostServiceHelper.getDataFromRequest(request, response);
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(data);
			Long postId = dataJson.getLong("postId");
			PostServiceHelper.deletePostByPostId(openId, postId);
			JSONObject response = new JSONObject();
			response.put("code", 0);
			response.put("message", "OK");
			PostServiceHelper.writeJson(this.response, response);
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "fail to parse data"
					+ openId, e);
		}
	}

	private void handlePost(String openId) throws JSONException, IOException {
		String data = PostServiceHelper.getDataFromRequest(request, response);
		// parse data
		JSONObject dataJson;
		try {
			dataJson = new JSONObject(data);
			Post post = getPostFromJsonData(dataJson, openId);
			PostDAOImpl postDao = new PostDAOImpl();
			postDao.insert(post);
			JSONObject response = new JSONObject();
			response.put("code", 0);
			response.put("message", "OK");
			PostServiceHelper.writeJson(this.response, response);
		} catch (JSONException e) {
			JSONObject response = new JSONObject();
			try {
				response.put("code", 9004);
				response.put("message", "Bad Request - 无法解析的数据包");
			} catch (JSONException e1) {
				LOGGER.log(Level.SEVERE, "fail to put attribut in response", e);
			}
			PostServiceHelper.writeJson(this.response, response);
			LOGGER.log(Level.SEVERE, "fail to parse Json with data " + data, e);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "fail to save post with openId "
					+ openId, e);
		}
	}



	private Post getPostFromJsonData(JSONObject dataJson, String openId) 
			throws Exception {
		String title = dataJson.getString("title");
		String content = dataJson.getString("content");
		String type = dataJson.getString("type");
		String college = dataJson.getString("college");
		Long userId = new UserDAOImpl().getUserIdByOpenId(openId);
		Date createdDate = new Date(System.currentTimeMillis());
		return userId == null ? null : new Post(title, content, type, college, userId, createdDate);
	}

	private void handleGet(String openId) {
		String data = PostServiceHelper.getDataFromRequest(request, response);
		JSONObject dataJson = new JSONObject();
		
		try {
			dataJson = new JSONObject(data);
			if (dataJson.has("postId")) {
				Long postId = dataJson.getLong("postId");
				queryPostById(openId, postId);
			}
			else {
				queryPostOpenId(openId);
			}
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "fail to parse Json", e);
		}
	}

	private void queryPostById(String openId, Long postId) {
		JSONObject response = new JSONObject();
		try {
			response.put("code", 0);
			response.put("message", "OK");
			Post post = PostServiceHelper.getPostByPostId(openId, postId);
			response.put("data", post);
			PostServiceHelper.writeJson(this.response, response);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "fail to handle get with openId "
					+ openId, e);
			PostServiceHelper.writeJson(this.response, PostServiceHelper.getJsonForError(e));
		}
	}

	private void queryPostOpenId(String openId) {
		JSONObject response = new JSONObject();
		try {
			Long userId = new UserDAOImpl().getUserIdByOpenId(openId);
			response.put("code", 0);
			response.put("message", "OK");
			if (userId == null) PostServiceHelper.writeJson(this.response, response);
			List<Post> posts = new PostDAOImpl().queryByUserId(userId);
			response.put("data", posts);
			PostServiceHelper.writeJson(this.response, response);
		} catch(Exception e) {
			LOGGER.log(Level.SEVERE, "fail to handle get with openId "
					+ openId, e);
			PostServiceHelper.writeJson(this.response, PostServiceHelper.getJsonForError(e));
		}
	}
}

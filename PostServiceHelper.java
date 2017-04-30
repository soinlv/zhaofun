package com.qcloud.weapp.post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.phpdao.dao.impl.PostDAOImpl;
import com.phpdao.domain.Post;
import com.qcloud.weapp.authorization.LoginServiceException;

public class PostServiceHelper {

	private static final Logger LOGGER = Logger.getLogger(PostServiceHelper.class.getName());

	public static String getDataFromRequest(HttpServletRequest request
			, HttpServletResponse response) {
		String requestContent = null;
		// 1. read buffer content
		try {
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
			requestContent = "";
			for (String line; (line = requestReader.readLine()) != null;) {
				requestContent += line;
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "fail to read buffer", e);
			writeJson(response, getJsonForError(e));
			return null;
		}
		
		// 2. save the content in body variable
		JSONObject body = null;
		String data = null;
		try {
			body = new JSONObject(requestContent);
			data = body.getString("data");
			// String signature = body.getString("signature");
		} catch (JSONException e) {
			JSONObject errJson = new JSONObject();
			try {
				errJson.put("code", 9001);
				errJson.put("message", "Cant not parse the request body: invalid json");
			} catch (JSONException e1) {
				LOGGER.log(Level.SEVERE, "fail to put attribut in response", e);
			}
			writeJson(response, errJson);
		}
		return data;
	}
	
	private static JSONObject getJsonForError(Exception error, int errorCode) {
		JSONObject json = new JSONObject();
		try {
			json.put("code", errorCode);
			if (error instanceof LoginServiceException) {
				json.put("error", ((LoginServiceException) error).getType());
			}
			json.put("message", error.getMessage());
		} catch (JSONException e) {
			LOGGER.log(Level.SEVERE, "fail to getJsonForError", e);
		}
		return json;
	}
	
	public static JSONObject getJsonForError(Exception error) {
		return getJsonForError(error, -1);
	}
	
	public static void writeJson(HttpServletResponse response, JSONObject json) {
		try {
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				response.getWriter().print(json.toString());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "fail to writeJson", e);
		}
	}

	public static void deletePostByPostId(String openId, Long postId) {
		
		Post post = getPostByPostId(openId, postId);
		if (post != null) {
			PostDAOImpl postDao = new PostDAOImpl();
			try {
				postDao.delete(post.getId());
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, "fail to delete post with postId" 
						+ postId + " and openId " + openId, e);
			}
		}
	}

	public static Post getPostByPostId(String openId, Long postId) {
		PostDAOImpl postDAO = new PostDAOImpl();
		try {
			Post post = postDAO.queryByPostIdAndOpenId(openId, postId);
			return post;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "fail to get post with postId"
					+ postId + " and openId " + openId, e);
		}
		return null;
	}
}

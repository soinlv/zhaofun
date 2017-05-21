package com.qcloud.cos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import com.qcloud.cos.request.CreateFolderRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.weapp.authorization.LoginService;
import com.qcloud.weapp.authorization.UserInfo;
import com.qcloud.weapp.post.PostServiceHelper;
import com.qcloud.weapp.tunnel.TunnelHandleOptions;

public class UploadService {

	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private COSClient cosClient;
	private static String bucketName = "zhaofun";
	private static final Logger LOGGER = Logger.getLogger(UploadService.class.getName());
	
	public UploadService(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		cosClient = new ClientConfigFactory().getCosClient();
	}

	public void handle(TunnelHandleOptions options) throws IOException {
		UserInfo user = null;
		/*if (options != null && options.isCheckLogin()) {
			try {
				LoginService loginService = new LoginService(request, response);
				user = loginService.check();
			} catch (Exception e) {
				PostServiceHelper.writeJson(response, PostServiceHelper.getJsonForError(e));
				return;
			}
		}
		if (user == null) return;*/
		if (request.getMethod().toUpperCase() == "GET") {
			//handleGet(user.getOpenId());
			handleGet("testOpenId");
		}
		else if (request.getMethod().toUpperCase() == "POST") {
			//handlePost(user.getOpenId());
			handlePost("testOpenId");
		}
		
	}

	private void handlePost(String openId) throws IOException {
		LOGGER.log(Level.INFO, "handlePost with openId: " + openId);
		String cosFolderPath = createFolderByOpenid(openId);
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			DiskFileItemFactory factory = new DiskFileItemFactory(); 
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8"); 
			try {
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {  
	                String name = item.getFieldName();  
	                FileItem file = null;
	                String sessionToken = null;
	                // 若是一个一般的表单域, 打印信息  
	                if (item.isFormField()) {  
	                    String value = "";
						try {
							value = item.getString("utf-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
	                    if("session_token".equals(name)){
	                        sessionToken = value;
	                    }
	                } 
	                else {
		                    if("file".equals(name)){
		                        file = item;
		                    }
	                }  
	                //获取路径名
                    String value = item.getName();
                    //索引到最后一个反斜杠
                    int start = value.lastIndexOf("");
                    //截取 上传文件的 字符串名字，加1是 去掉反斜杠，
                    //String fileName = value.substring(start+1);
                    String fileName = "testfile.jpg";
                    InputStream input = file.getInputStream();
                    //本地用于存放文件的路径
                    String localPath = request.getSession().getServletContext().getRealPath("/")
                    		  + fileName;
                    LOGGER.log(Level.INFO, "localPath: " + localPath);
                    FileOutputStream os = new FileOutputStream(localPath);
                    byte[] buffer = new byte[1024];
                    int n = 0;
                    while (-1 != (n = input.read(buffer))) {
                        os.write(buffer, 0, n);
                    }
                    os.flush();
                    os.close();
                    input.close();
                    String cosFilePath = cosFolderPath + fileName;
                    uploadFileToCOS(cosFilePath, localPath);
                    //删除上传之后的文件
                    File fileLocal = new File(localPath);
                    fileLocal.delete();
                    JSONObject response = new JSONObject();
    				response.put("code", 0);
    				response.put("message", "OK");
    				PostServiceHelper.writeJson(this.response, response);
	            }
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				LOGGER.log(Level.SEVERE, "fail to upload file", e);
			}
		}
	}

	private void uploadFileToCOS(String cosFilePath, String localPath) {
		 
	        UploadFileRequest uploadFileRequest =
	                new UploadFileRequest(bucketName, cosFilePath, localPath);
	        uploadFileRequest.setEnableShaDigest(false);
	        cosClient.uploadFile(uploadFileRequest);
	}

	private String createFolderByOpenid(String openId) {
		// TODO check if the folder exists or not.
		 String cosFolderPath = "/" + openId + "/";
	     CreateFolderRequest createFolderRequest =
	    		 new CreateFolderRequest(bucketName, cosFolderPath);
	     cosClient.createFolder(createFolderRequest);
	     return cosFolderPath;
	}

	private void handleGet(String openId) {
		// TODO Auto-generated method stub
		
	}
	
}

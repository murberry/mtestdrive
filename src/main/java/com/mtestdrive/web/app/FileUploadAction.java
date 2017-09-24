package com.mtestdrive.web.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.UUIDGenerator;
import org.jeecgframework.web.demo.entity.test.FileMeta;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/fileUpload")
public class FileUploadAction {
	private File file; //file控件名
    private String fileContentType;//图片类型
    private String fileFileName; //文件名
	

	@RequestMapping(params = "upImg", method = RequestMethod.POST)
	@ResponseBody
	public LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		String savePath = null;
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		FileMeta fileMeta = null;
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		while (itr.hasNext()) {
			mpf = request.getFile(itr.next());
			if (files.size() >= 10)
				files.pop();
			fileMeta = new FileMeta();
			fileMeta.setOriginalFileName(mpf.getOriginalFilename());
			String originalFilename = mpf.getOriginalFilename();
			String substring = originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length());
			String fileName = UUIDGenerator.generate() + "."+substring;
			fileMeta.setFileName(fileName); 
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			try {
				String realPath = ResourceUtil.getConfigByName("uploadpath");// 获取配置文件中配置的文件路径
				File baseDir = new File(realPath);
				if(!baseDir.exists())
					baseDir.mkdirs();
				savePath = realPath+fileName;// 文件保存全路径
				FileCopyUtils.copy(mpf.getBytes(),new File(savePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			files.add(fileMeta);
		}
		return files;
	}
	
	@RequestMapping(params = "view", method = {RequestMethod.GET,RequestMethod.POST})
	 public void get(HttpServletResponse response,String fileName) throws Exception{

       File file = new File(ResourceUtil.getConfigByName("uploadpath")+fileName);

       //判断文件是否存在如果不存在就返回默认图标
       if(!(file.exists() && file.canRead())) {
           throw new FileNotFoundException(ResourceUtil.getConfigByName("uploadpath")+fileName);
       }
       FileInputStream fis = null;  
       OutputStream os = null;  
       response.setContentType(new MimetypesFileTypeMap().getContentType(file));
       try {  
           fis = new FileInputStream(file);  
           os = response.getOutputStream();  
           int count = 0;  
           byte[] buffer = new byte[1024 * 8];  
           while ((count = fis.read(buffer)) != -1) {  
               os.write(buffer, 0, count);  
               os.flush();  
           }  
       } catch (Exception e) {  
           e.printStackTrace();  
       } finally{
       	 fis.close();  
            os.close();  
       }
	 }
	
}

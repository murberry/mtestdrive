package org.jeecgframework.web.demo.controller.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 类 名 称： FileUploadController 类 描 述： jQuery File Upload 例子 创 建 人： yiming.zhang
 * 联系方式： 1374250553@qq.com 创建时间： 2014-2-19 下午11:25:22
 * 
 * 修 改 人： Administrator 操作时间： 2014-2-19 下午11:25:22 操作原因：
 * 
 */
//@Scope("prototype")
@Controller
@RequestMapping("/fileUploadController")
public class FileUploadController extends BaseController {
 
	private static final Logger logger = Logger.getLogger(FileUploadController.class);
	private int maxBufferSize =  16*1024*1024;//最大文件大小16M


	/**
	 * 方法描述:  (这里用一句话描述这个方法的作用)
	 * 作    者： yiming.zhang
	 * 日    期： 2014-2-20-下午10:27:45
	 * @param request
	 * @return 
	 * 返回类型： ModelAndView
	 */
	@RequestMapping(params = "fileUploadSample")
	public ModelAndView webOfficeSample(HttpServletRequest request) {
		return new ModelAndView("jeecg/demo/test/fileUploadSample");
	}

	@RequestMapping(params = "upload", method = RequestMethod.POST)
	@ResponseBody
	public LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		FileMeta fileMeta = null;
		logger.info("upload-》1. build an iterator");
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		logger.info("upload-》2. get each file");
		while (itr.hasNext()) {
			logger.info("upload-》2.1 get next MultipartFile");
			mpf = request.getFile(itr.next());
			logger.info(mpf.getOriginalFilename() + " uploaded! " + files.size());
			//System.out.println(mpf.getOriginalFilename() + " uploaded! " + files.size());
			logger.info("2.2 if files > 10 remove the first from the list");
			if (files.size() >= 10)
				files.pop();
			logger.info("2.3 create new fileMeta");
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
				logger.info("upload-》文件的硬盘真实路径"+realPath);
				String savePath = realPath+fileName;// 文件保存全路径
				logger.info("upload-》文件保存全路径"+savePath);
				FileCopyUtils.copy(mpf.getBytes(),new File(savePath));
				logger.info("copy file to local disk (make sure the path e.g. "+realPath+" exists)");
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("2.4 add to files");
			files.add(fileMeta);
			logger.info("success uploaded="+files.size());
		}
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
	}
	
	@RequestMapping(params = "view", method = {RequestMethod.GET,RequestMethod.POST})
	 public void get(HttpServletResponse response,String fileName) throws Exception{

         File file = new File(ResourceUtil.getConfigByName("uploadpath")+fileName);

         //判断文件是否存在如果不存在就返回默认图标
         if(!(file.exists() && file.canRead())) {
			 logger.error("文件不存在: File = "+ResourceUtil.getConfigByName("uploadpath")+fileName);
//            throw new FileNotFoundException(ResourceUtil.getConfigByName("uploadpath")+fileName);
         } else {
             FileInputStream fis = null;
             OutputStream os = null;
             response.setContentType(new MimetypesFileTypeMap().getContentType(file));
             try {
                 fis = new FileInputStream(file);
                 os = response.getOutputStream();
                 int count = 0;
                 byte[] buffer = new byte[maxBufferSize];
                 while ((count = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, count);
                    os.flush();
                }
             } catch (Exception e) {
				 logger.error("文件输出失败: File = "+file.getAbsolutePath());
				 e.printStackTrace();
             } finally{
                 if(fis != null)
                     fis.close();
                 if(os != null)
                     os.close();
             }
         } //end if
	 }
}

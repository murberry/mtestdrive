package com.mtestdrive.web.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.entity.LibEntity;
import com.mtestdrive.service.LibServiceI;

/**
 * @Title: Controller
 * @Description: 字典
 * @author zhangdaihao
 * @date 2017-03-10 17:19:31
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/libController")
public class LibController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LibController.class);

	@Autowired
	private LibServiceI libService;
	@Autowired
	private SystemService systemService;

	/**
	 * 字典列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/lib/libList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(LibEntity lib, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(LibEntity.class, dataGrid);
		// 查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, lib, request.getParameterMap());
		this.libService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除字典
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(LibEntity lib, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		lib = systemService.getEntity(LibEntity.class, lib.getId());
		message = "字典删除成功";
		libService.delete(lib);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);

		j.setMsg(message);
		return j;
	}

	/**
	 * 添加字典
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(LibEntity lib, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(lib.getId())) {
			message = "字典更新成功";
			LibEntity t = libService.get(LibEntity.class, lib.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(lib, t);
				libService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "字典更新失败";
			}
		} else {
			message = "字典添加成功";
			libService.save(lib);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 字典列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(LibEntity lib, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(lib.getId())) {
			lib = libService.getEntity(LibEntity.class, lib.getId());
			req.setAttribute("libPage", lib);
		}
		return new ModelAndView("mpage/manage/lib/lib");
	}
}

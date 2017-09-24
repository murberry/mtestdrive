package com.mtestdrive.web.manage;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mtestdrive.MaseratiConstants.CarStatus;
import com.mtestdrive.MaseratiConstants.ReportStatus;
import com.mtestdrive.dto.ReportRecordsDto;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.ReportRecordsEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.ReportRecordsServiceI;

/**   
 * @Title: Controller
 * @Description: 报备信息
 * @author zhangdaihao
 * @date 2017-03-10 17:29:49
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/reportRecordsController")
public class ReportRecordsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ReportRecordsController.class);

	@Autowired
	private ReportRecordsServiceI reportRecordsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private AgencyInfoServiceI agencyInfoService;

	
	
	/**
	 * @Title: cancleReport   
	 * @Description: 关闭报备   
	 * @param: @param reportId
	 * @param: @return      
	 * @return: AjaxJson      
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(params = "cancleReport", method = RequestMethod.POST)
	public AjaxJson cancleReport(String reportId){
		AjaxJson aj = new AjaxJson();
		
		if(StringUtil.isEmpty(reportId)){
			aj.setMsg("reportId 不能为空");
			aj.setSuccess(false);
		}else{
			ReportRecordsEntity report = reportRecordsService.get(ReportRecordsEntity.class, reportId);
			if(report == null){
				aj.setMsg("报备不存在");
				aj.setSuccess(false);
			}else{
				report.setStatus(ReportStatus.FINISHED);
				reportRecordsService.updateEntitie(report);
				aj.setMsg("操作成功");
				aj.setSuccess(true);
			}
		}
		return aj;
	}

	/**
	 * 报备信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/reportRecords/reportRecordsList");
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
	public void datagrid(ReportRecordsDto reportRecords,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ReportRecordsEntity.class, dataGrid);
		if(!ResourceUtil.getSessionUserName().getUserKey().equals("超级管理员") &&!ResourceUtil.getSessionUserName().getUserKey().equals("超级系统管理员")){
			cq.add(Restrictions.eq("agency.id", ResourceUtil.getSessionUserName().getDepartid()));
		}
		if (StringUtil.isNotEmpty(reportRecords.getRegionId()) || StringUtil.isNotEmpty(reportRecords.getDealerName())) {
			cq.createAlias("agency", "agency");
			String regionId = reportRecords.getRegionId();
			String name = reportRecords.getDealerName();
			if (StringUtil.isNotEmpty(regionId)) {
				cq.add(Restrictions.eq("agency.regionId", regionId));
			}
			if (StringUtil.isNotEmpty(name)) {
				cq.add(Restrictions.like("agency.name", "%"+name+"%"));
			}
		}
		if (StringUtil.isNotEmpty(reportRecords.getCarType()) || StringUtil.isNotEmpty(reportRecords.getPlateNo())) {
			DetachedCriteria salesDc = DetachedCriteria.forClass(CarInfoEntity.class);
			if (StringUtil.isNotEmpty(reportRecords.getCarType())) {
				salesDc.add(Restrictions.eq("type", reportRecords.getCarType()));
			}
			if (StringUtil.isNotEmpty(reportRecords.getPlateNo())) {
				salesDc.add(Restrictions.like("plateNo", "%"+reportRecords.getPlateNo()+"%"));
			}
			List<CarInfoEntity> listCarInfo = carInfoService .findByDetached(salesDc);
			if (listCarInfo.size()!=0) {
				String CarIds[] = new String[listCarInfo.size()];
				for (int i = 0; i < CarIds.length; i++) {
					CarIds[i] = listCarInfo.get(i).getId();
				}
				cq.add(Restrictions.in("carId", CarIds));
			}else{
				//使他查不到
				cq.add(Restrictions.eq("carId", "0"));
			}
		}
		cq.addOrder("createTime", SortDirection.desc);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, new ReportRecordsEntity(), request.getParameterMap());
		this.reportRecordsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除报备信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(ReportRecordsEntity reportRecords, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		reportRecords = systemService.getEntity(ReportRecordsEntity.class, reportRecords.getId());
		message = "报备信息删除成功";
		reportRecordsService.delete(reportRecords);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加报备信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ReportRecordsEntity reportRecords, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		if (StringUtil.isNotEmpty(reportRecords.getId())) {
			message = "报备信息更新成功";
			ReportRecordsEntity t = reportRecordsService.get(ReportRecordsEntity.class, reportRecords.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(reportRecords, t);
				reportRecordsService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "报备信息更新失败";
			}
		} else {
			message = "报备信息添加成功";
			String carId = request.getParameter("carId");
			
			CarInfoEntity carInfo = carInfoService.get(CarInfoEntity.class, carId);
			reportRecords.setAgency(carInfo.getAgency());
			reportRecords.setCarId(carId);
			reportRecords.setStatus(ReportStatus.AWAIT);
			reportRecords.setCreateTime(new Date());
			reportRecords.setCreateBy(ResourceUtil.getSessionUserName().getId());
			reportRecordsService.save(reportRecords);
			/*carInfo.setStatus(CarStatus.ACTIVITY_REPORT);
			carInfo.setUpdateTime(new Date());
			carInfoService.saveOrUpdate(carInfo);*/
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 报备信息列表页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(ReportRecordsEntity reportRecords, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(reportRecords.getId())) {
			reportRecords = reportRecordsService.getEntity(ReportRecordsEntity.class, reportRecords.getId());
			req.setAttribute("reportRecordsPage", reportRecords);
		}
		return new ModelAndView("mpage/manage/reportRecords/reportRecords");
	}
}

package com.mtestdrive.web.app;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.dto.CustomerInfoDto;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.vo.CustomerInfoVo;
import com.mtestdrive.vo.DriveRecodsVo;
import com.mtestdrive.vo.SalesmanInfoVo;

/**
 * @Title: Action
 * @Description: 客户信息
 * @author zhangdaihao
 * @date 2017-03-10 17:26:29
 * @version V1.0
 * 
 */
@Controller
@RequestMapping("/customerInfoAction")
public class CustomerInfoAction extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CustomerInfoAction.class);

	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private Validator validator;

	@RequestMapping(params = "jsonList", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson jsonList(HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String parameter = request.getParameter("parameter");
		String curPage = request.getParameter("curPage");
		
		if(StringUtil.isNotEmpty(curPage)){
			SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
			CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class);
			cq.eq("createBy", salesmanInfo.getId());
			if(parameter!=null){
				try {
					parameter=new String(parameter.getBytes("ISO-8859-1"),"utf-8");
					cq.or(Restrictions.like("name", "%"+parameter+"%"), Restrictions.like("mobile", "%"+parameter+"%"));
				} catch (UnsupportedEncodingException e1) {
					logger.error(e1.getMessage());
				} 
			}
			cq.add();
			cq.add(Restrictions.eq("status", ConstantStatus.VALID));
			cq.setPageSize(20);
			cq.setCurPage(Integer.parseInt(curPage));
			cq.addOrder("createTime", SortDirection.desc);
			List<CustomerInfoEntity> customerInfoList = customerInfoService.getListByCriteriaQuery(cq, true);
			
			List<CustomerInfoVo> customerInfoVoList = null;
			List<DriveRecodsVo> driveRecodsVoList = null;
			try {
				customerInfoVoList = ListUtils.copyTo(customerInfoList, CustomerInfoVo.class);
				for (CustomerInfoVo customerInfoVo : customerInfoVoList) {
					List<DriveRecodsEntity> driveRecodsList = driveRecodsService.findByProperty(DriveRecodsEntity.class, "customer.id", customerInfoVo.getId());
					if(driveRecodsList.size()!=0){
						driveRecodsVoList = ListUtils.copyTo(driveRecodsList, DriveRecodsVo.class);
						customerInfoVo.setDriveRecodses(driveRecodsVoList);
					}
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			j.setObj(customerInfoVoList);
		}
		return j;
	}
	
	
	
	@RequestMapping(params = "index", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request){
		String parameter = request.getParameter("parameter");
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class);
		cq.eq("createBy", salesmanInfo.getId());
		if(parameter!=null){
			try {
				parameter=new String(parameter.getBytes("ISO-8859-1"),"utf-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			cq.or(Restrictions.like("name", "%"+parameter+"%"), Restrictions.like("mobile", "%"+parameter+"%"));
		}
		cq.add();
		cq.add(Restrictions.eq("status", ConstantStatus.VALID));

		List<CustomerInfoEntity> customerInfoList = customerInfoService.getListByCriteriaQuery(cq, false);
		
		List<CustomerInfoVo> customerInfoVoList = null;
		List<DriveRecodsVo> driveRecodsVoList = null;
		try {
			customerInfoVoList = ListUtils.copyTo(customerInfoList, CustomerInfoVo.class);
			for (CustomerInfoVo customerInfoVo : customerInfoVoList) {
				List<DriveRecodsEntity> driveRecodsList = driveRecodsService.findByProperty(DriveRecodsEntity.class, "customer.id", customerInfoVo.getId());
				if(driveRecodsList.size()!=0){
					driveRecodsVoList = ListUtils.copyTo(driveRecodsList, DriveRecodsVo.class);
					customerInfoVo.setDriveRecodses(driveRecodsVoList);
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("customerInfoVoList", customerInfoVoList);
		return new ModelAndView("customerInfo/index");
	}
	
	
	
	
	@RequestMapping(params = "add", method = RequestMethod.GET)
	public ModelAndView add(HttpServletRequest request) {
		List<TSType> quarry = customerInfoService.getAllQuarries();
		request.setAttribute("quarry", quarry);
		String id = request.getParameter("id");
		if(id!=null){
			CustomerInfoEntity customerInfoEntity = customerInfoService.get(CustomerInfoEntity.class, id);
			CustomerInfoVo customerInfoVo = new CustomerInfoVo();
			try {
				BeanUtils.copyProperties(customerInfoVo, customerInfoEntity);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			request.setAttribute("customerInfo", customerInfoVo);
		}
		
		return new ModelAndView("customerInfo/add");
	}
	
	@RequestMapping(params = "save", method = RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		if(gender == null)
			gender = "0";
		String birthday = request.getParameter("birthday");
		String drivingLicensePicPath = request.getParameter("drivingLicensePicPath");
		String mobile = request.getParameter("mobile");
		String quarry = request.getParameter("quarry");
		String remark = request.getParameter("remark");
		CustomerInfoEntity customerInfoEntity = customerInfoService.get(CustomerInfoEntity.class, id);
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		if(customerInfoEntity!=null){
			customerInfoEntity.setName(name);
			customerInfoEntity.setGender(Integer.parseInt(gender));
			Date str2Date = DateUtils.str2Date(birthday, DateUtils.date_sdf);
			customerInfoEntity.setBirthday(str2Date);
			customerInfoEntity.setDrivingLicensePicPath(drivingLicensePicPath);
			customerInfoEntity.setMobile(mobile);
			customerInfoEntity.setQuarry(Integer.parseInt(quarry));
			customerInfoEntity.setRemark(remark);
			customerInfoEntity.setUpdateTime(DateUtils.gettimestamp());
			customerInfoEntity.setUpdateBy(salesmanInfo.getId());
			customerInfoService.saveOrUpdate(customerInfoEntity);
		}else{
			CustomerInfoEntity customerInfoEntity2 = new CustomerInfoEntity();
			customerInfoEntity2.setName(name);
			customerInfoEntity2.setGender(Integer.parseInt(gender));
			Date str2Date = DateUtils.str2Date(birthday, DateUtils.date_sdf);
			customerInfoEntity2.setBirthday(str2Date);
			customerInfoEntity2.setDrivingLicensePicPath(drivingLicensePicPath);
			customerInfoEntity2.setMobile(mobile);
			customerInfoEntity2.setQuarry(Integer.parseInt(quarry));
			
			customerInfoEntity2.setCreateBy(salesmanInfo.getId());
			customerInfoEntity2.setAgencyId(salesmanInfo.getAgencyId());
			customerInfoEntity2.setSource(2);
			customerInfoEntity2.setRemark(remark);
			customerInfoEntity2.setCreateTime(DateUtils.gettimestamp());
			customerInfoEntity2.setStatus(ConstantStatus.VALID);
			customerInfoEntity2.setType("潜在客户");
			customerInfoService.save(customerInfoEntity2);
		}
		
		return new ModelAndView(new RedirectView("customerInfoAction.action?index"));
	}
	
	
	@RequestMapping(params = "check", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson check(@RequestBody CustomerInfoDto customer,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		String mobile = customer.getMobile();
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class);
		cq.eq("mobile", mobile);
		cq.eq("createBy", salesmanInfo.getId());
		cq.add();
		List<CustomerInfoEntity> customerList = carInfoService.getListByCriteriaQuery(cq, false);
		if(customerList.size()!=0){
			message="2";
			j.setObj(message);
			return j;
		}
		message="1";
		j.setObj(message);
		return j;
	}
	
	@RequestMapping(params = "checkById", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson checkById(@RequestBody CustomerInfoDto customer,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		String mobile = customer.getMobile();
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		CriteriaQuery cq = new CriteriaQuery(CustomerInfoEntity.class);
		cq.eq("mobile", mobile);
		cq.eq("createBy", salesmanInfo.getId());
		cq.notEq("id", customer.getId());
		cq.add();
		List<CustomerInfoEntity> customerList = carInfoService.getListByCriteriaQuery(cq, false);
		if(customerList.size()!=0){
			message="2";
			j.setObj(message);
			return j;
		}
		message="1";
		j.setObj(message);
		return j;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(params = "list", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public List<CustomerInfoVo> list(@RequestBody CustomerInfoDto customer) {
		List<CustomerInfoVo> customerInfos = null;
		boolean isAll = customer.getDriveStartTime() == null || customer.getDriveEndTime() == null;
		try {
			CustomerInfoEntity example = new CustomerInfoEntity();
			MyBeanUtils.copyBean2Bean(example, customer);
			List listCustomerInfo = customerInfoService.findByExample("com.mtestdrive.entity.CustomerInfoEntity", example);
			if(!ListUtils.isNullOrEmpty(listCustomerInfo)){
				customerInfos = new ArrayList<CustomerInfoVo>();
				CustomerInfoVo customerInfo = null;
				for (int i = 0; i < listCustomerInfo.size(); i++) {
					customerInfo = new CustomerInfoVo();
					MyBeanUtils.copyBean2Bean(customerInfo, listCustomerInfo.get(i));
					if (StringUtils.isNotEmpty(customerInfo.getId())) {
						List listDriveRecods = null;
						if (isAll) {//全部试驾预约
							listDriveRecods = driveRecodsService.findByProperty(DriveRecodsEntity.class, "customer.id", customerInfo.getId());
						} else {//时间约束
							CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
							cq.eq("customer.id", customerInfo.getId());
							cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(customer.getDriveStartTime()), DateUtils.date_sdf));
							Date orderEndTime = customer.getDriveEndTime() == null?
												DateUtils.addDate(DateUtils.formatDate(customer.getDriveStartTime()), 1):
												DateUtils.addDate(DateUtils.formatDate(customer.getDriveEndTime()), 1);
							cq.lt("orderEndTime", orderEndTime);
							cq.add();
							listDriveRecods = driveRecodsService.getListByCriteriaQuery(cq, false);
						}
						if (!ListUtils.isNullOrEmpty(listDriveRecods)) {
							List<DriveRecodsVo> driveRecodses = ListUtils.copyTo(listDriveRecods, DriveRecodsVo.class);
							for (DriveRecodsVo driveRecodsVo : driveRecodses) {
								if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())){
									CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
									if (car != null) {
										MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
									}
								}
							}
							customerInfo.setDriveRecodses(driveRecodses);
						}
					}
					customerInfos.add(customerInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerInfos;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		CustomerInfoEntity task = customerInfoService.get(CustomerInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(params = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody CustomerInfoDto customer, UriComponentsBuilder uriBuilder) {
		CustomerInfoEntity customerInfo = new CustomerInfoEntity();
		try {
			MyBeanUtils.copyBean2Bean(customerInfo, customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CustomerInfoEntity>> failures = validator.validate(customerInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		customerInfoService.save(customerInfo);

		// 按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		//String id = customerInfo.getId();
		//URI uri = uriBuilder.path("/rest/customerInfoAction/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//headers.setLocation(uri);

		return new ResponseEntity(customerInfo, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody CustomerInfoDto customer) {
		CustomerInfoEntity customerInfo = customerInfoService.get(CustomerInfoEntity.class, customer.getId());
		// 此处添加用户更新信息字段
		// 调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<CustomerInfoEntity>> failures = validator.validate(customerInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		// 保存
		customerInfoService.saveOrUpdate(customerInfo);

		// 按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		customerInfoService.deleteEntityById(CustomerInfoEntity.class, id);
	}
}

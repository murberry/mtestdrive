package com.mtestdrive.web.manage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.MutiLangUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.PasswordUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.pojo.base.TSUserOrg;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.ChinaEntity;
import com.mtestdrive.service.AgencyInfoServiceI;

/**   
 * @Title: Controller
 * @Description: 经销商信息
 * @author zhangdaihao
 * @date 2017-03-10 17:21:49
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/agencyInfoController")
public class AgencyInfoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(AgencyInfoController.class);

	@Autowired
	private AgencyInfoServiceI agencyInfoService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Validator validator;

	/**
	 * 经销商信息列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("mpage/manage/agencyInfo/agencyInfoList");
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
	public void datagrid(AgencyInfoEntity agencyInfo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AgencyInfoEntity.class, dataGrid);
		String name = agencyInfo.getName();
		String regionId = agencyInfo.getRegionId();
		String provinceId = agencyInfo.getProvinceId();
		String cityId = agencyInfo.getCityId();
		
		cq.add(Restrictions.eq("status", ConstantStatus.VALID));
		cq.addOrder("createTime", SortDirection.desc);
		//查询条件组装器
		/*org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, agencyInfo, request.getParameterMap());*/
		if(StringUtil.isNotEmpty(name)){
			cq.add(Restrictions.like("name", '%'+name+'%'));
		}
		if(StringUtil.isNotEmpty(regionId)){
			cq.add(Restrictions.eq("regionId", regionId));
		}
		if(StringUtil.isNotEmpty(provinceId)){
			cq.add(Restrictions.eq("provinceId", provinceId));
		}
		if(StringUtil.isNotEmpty(cityId)){
			cq.add(Restrictions.eq("cityId", cityId));
		}
		this.agencyInfoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "getAgency")
	@ResponseBody
	public List<AgencyInfoEntity> getAgency() {
		List<AgencyInfoEntity> AgencyInfoList = agencyInfoService.loadAll(AgencyInfoEntity.class);
		return AgencyInfoList;
	}
	
	/**
	 * easyui AJAX请求数据省
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param 张丹超
	 */

	@RequestMapping(params = "getProvince")
	@ResponseBody
	public List<ChinaEntity> getProvince() {
		List<ChinaEntity> ProvinceList = systemService.findByProperty(ChinaEntity.class, "pid", 0);
		return ProvinceList;
	}
	
	/**
	 * easyui AJAX请求数据省
	 * 
	 * @param request
	 */

	@RequestMapping(params = "getCity")
	@ResponseBody
	public List<ChinaEntity> getCity(HttpServletRequest request) {
		String pId = request.getParameter("pId");
		List<ChinaEntity> ProvinceList = systemService.findByProperty(ChinaEntity.class, "pid", Integer.parseInt(pId));
		return ProvinceList;
	}
	
	/**
	 * 方法描述:  查看成员列表
	 * 作    者： 张丹超
	 * @param request
	 * @param departid
	 * @return 
	 * 返回类型： ModelAndView
	 */
	@RequestMapping(params = "userList")
	public ModelAndView userList(HttpServletRequest request, String agenctid) {
		request.setAttribute("agenctid", agenctid);
		return new ModelAndView("mpage/manage/agencyInfo/agencyUserList");
	}
	
	/**
	 * 方法描述:  成员列表dataGrid
	 * 作    者：张丹超
	 * @param user
	 * @param request
	 * @param response
	 * @param dataGrid 
	 * 返回类型： void
	 */
	@RequestMapping(params = "userDatagrid")
	public void userDatagrid(TSBaseUser user,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		if(user!=null&&user.getDepartid()!=null){
			user.setDepartid(null);//设置用户的所属部门的查询条件为空；
		}
		CriteriaQuery cq = new CriteriaQuery(TSBaseUser.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);
		String departid = oConvertUtils.getString(request.getParameter("agenctid"));
		if (!StringUtil.isEmpty(departid)) {

			/*DetachedCriteria dc = cq.getDetachedCriteria();
			DetachedCriteria dcDepart = dc.createCriteria("userOrgList");*/
			cq.add(Restrictions.eq("departid", departid));
			cq.add(Restrictions.eq("deleteFlag", Globals.User_Forbidden));
            // 这种方式也是可以的
            //DetachedCriteria dcDepart = dc.createAlias("userOrgList", "userOrg");
            //dcDepart.add(Restrictions.eq("userOrg.tsDepart.id", departid));
			

		}
		Short[] userstate = new Short[] { Globals.User_Normal, Globals.User_ADMIN };
		cq.in("status", userstate);
		
		this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}	
	
	/**
     * 添加 用户到组织机构 的页面  跳转
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "goAddUserToOrg")
    public ModelAndView goAddUserToOrg(HttpServletRequest req) {
        req.setAttribute("orgId", req.getParameter("orgId"));
        return new ModelAndView("mpage/manage/agencyInfo/noCurDepartUserList");
    }
    
    /**
     * 获取 除当前 组织之外的用户信息列表
     * @param request request
     * @return 处理结果信息
     */
    @RequestMapping(params = "addUserToOrgList")
    public void addUserToOrgList(TSUser user, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        String orgId = request.getParameter("orgId");

        CriteriaQuery cq = new CriteriaQuery(TSUser.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, user);

        // 获取 当前组织机构的用户信息
        CriteriaQuery subCq = new CriteriaQuery(TSUserOrg.class);
        subCq.setProjection(Property.forName("tsUser.id"));
        subCq.eq("tsDepart.id", orgId);
        subCq.add();

        cq.add(Property.forName("id").notIn(subCq.getDetachedCriteria()));
        cq.add();

        this.systemService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }
    
    /**
     * 用户表 添加 经销商ID
     * @param req request
     * @return 处理结果信息
     */
    @RequestMapping(params = "doAddUserToOrg")
    @ResponseBody
    public AjaxJson doAddUserToOrg(HttpServletRequest req) {
    	String message = null;
        AjaxJson j = new AjaxJson();
       // AgencyInfoEntity agencyInfoEntity = agencyInfoService.get(AgencyInfoEntity.class, req.getParameter("orgId"));
        String[] userIds = req.getParameterValues("userIds");
        for (String id : userIds) {
        	TSBaseUser baseUser = systemService.getEntity(TSBaseUser.class, id);
        	baseUser.setDepartid(req.getParameter("orgId"));
        	 agencyInfoService.saveOrUpdate(baseUser);
		}
        message =  MutiLangUtil.paramAddSuccess("common.user");
//      systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        j.setMsg(message);

        return j;
    }
    
    /**
     * 保存 组织机构-用户 关系信息
     * @param request request
     * @param depart depart
     */
    private void saveOrgUserList(HttpServletRequest request,  AgencyInfoEntity agencyInfoEntity) {
        String orgIds = oConvertUtils.getString(request.getParameter("userIds"));

        List<TSUserOrg> userOrgList = new ArrayList<TSUserOrg>();
        List<String> userIdList = extractIdListByComma(orgIds);
        for (String userId : userIdList) {
            TSUser user = new TSUser();
            user.setId(userId);

           
        }
        if (!userOrgList.isEmpty()) {
            systemService.batchSave(userOrgList);
        }
    }
    
    /**
	 * easyuiAJAX请求数据： 用户添加绑定页面
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "addorup")
	public ModelAndView addorup( HttpServletRequest req,TSUser user) {
		String agenctid = req.getParameter("agenctid");
		
		if (StringUtil.isNotEmpty(user.getId())) {
			user = agencyInfoService.getEntity(TSUser.class, user.getId());
			req.setAttribute("user", user);
		}
		
		
		req.setAttribute("agenctid", req.getParameter("agenctid"));
		
		
		
        return new ModelAndView("mpage/manage/agencyInfo/user");
	}
    
	/**
	 * 用户录入
	 * 
	 * @param user
	 * @param req
	 * @return
	 */

	@RequestMapping(params = "saveUser")
	@ResponseBody
	public AjaxJson saveUser(HttpServletRequest req, TSUser user) {
		String agenctid = req.getParameter("agenctid");
		String message = null;
		AjaxJson j = new AjaxJson();
		String password = oConvertUtils.getString(req.getParameter("password"));
		
		if (StringUtil.isNotEmpty(user.getId())) {
			TSUser users = systemService.getEntity(TSUser.class, user.getId());
			users.setEmail(user.getEmail());
			users.setOfficePhone(user.getOfficePhone());
			users.setMobilePhone(user.getMobilePhone());
			users.setRealName(user.getRealName());
			users.setStatus(Globals.User_Normal);
			users.setActivitiSync(user.getActivitiSync());
			systemService.updateEntitie(users);
			message = "用户: " + users.getUserName() + "更新成功";
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}else{
			TSUser dbUser = userService.checkUserNameExits(user);
			//TSUser users = systemService.findUniqueByProperty(TSUser.class, "userName",user.getUserName());
			if (dbUser != null) {
				message = "用户: " + dbUser.getUserName() + "已经存在";
			} else {
				user.setPassword(PasswordUtil.encrypt(user.getUserName(), password, PasswordUtil.getStaticSalt()));
				user.setStatus(Globals.User_Normal);
				user.setDepartid(agenctid);
				user.setDeleteFlag(Globals.Delete_Normal);
				user.setUserKey("经销商管理员");
				systemService.save(user);
				
				//297e25b75b27a70b015b28039a0d000f是数据库中  组织玛莎拉蒂组织机构ID
				saveUserOrgList("297e25b75b27a70b015b28039a0d000f", user);
				
				//297e25b75aefdd5f015aefeaf8a7000a是数据库中  经销商管理员角色的ID
				saveRoleUser(user, "297e25b75aef86b1015aef8bd6ac0013");
				message = "用户: " + user.getUserName() + "添加成功";
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}
		j.setMsg(message);

		return j;
	}
    
	
	
	
	/**
	 * 删除经销商信息
	 * 
	 * @return
	 */
	@RequestMapping(params = "del")
	@ResponseBody
	public AjaxJson del(AgencyInfoEntity agencyInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		agencyInfo = systemService.getEntity(AgencyInfoEntity.class, agencyInfo.getId());
		message = "经销商信息删除成功";
		agencyInfo.setStatus(ConstantStatus.INVALID);
		agencyInfoService.updateEntitie(agencyInfo);
		systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加经销商信息
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(AgencyInfoEntity agencyInfo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		String userId = ResourceUtil.getSessionUserName().getId();
		if (StringUtil.isNotEmpty(agencyInfo.getId())) {
			message = "经销商信息更新成功";
			AgencyInfoEntity t = agencyInfoService.get(AgencyInfoEntity.class, agencyInfo.getId());
			try {
				MyBeanUtils.copyBeanNotNull2Bean(agencyInfo, t);
				agencyInfo.setUpdateTime(new Date());
				t.setUpdateBy(userId);
				t.setUpdateTime(DateUtils.gettimestamp());
				agencyInfoService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "经销商信息更新失败";
			}
		} else {
			message = "经销商信息添加成功";
			agencyInfo.setCreateTime(DateUtils.gettimestamp());
			agencyInfo.setCreateBy(userId);
			agencyInfo.setStatus(ConstantStatus.VALID);
			agencyInfoService.save(agencyInfo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 经销商信息列表页面跳转
	 * 
	 * @return
	 */
	
	@RequestMapping(params = "addorupdate")
	public ModelAndView addorupdate(AgencyInfoEntity agencyInfo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(agencyInfo.getId())) {
			agencyInfo = agencyInfoService.getEntity(AgencyInfoEntity.class, agencyInfo.getId());
			req.setAttribute("agencyInfoPage", agencyInfo);
		}
		return new ModelAndView("mpage/manage/agencyInfo/agencyInfo");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<AgencyInfoEntity> list() {
		List<AgencyInfoEntity> listAgencyInfos=agencyInfoService.getList(AgencyInfoEntity.class);
		return listAgencyInfos;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AgencyInfoEntity task = agencyInfoService.get(AgencyInfoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody AgencyInfoEntity agencyInfo, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<AgencyInfoEntity>> failures = validator.validate(agencyInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		agencyInfoService.save(agencyInfo);

		//按照Restful风格约定，创建指向新任务的url, 也可以直接返回id或对象.
		String id = agencyInfo.getId();
		URI uri = uriBuilder.path("/rest/agencyInfoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody AgencyInfoEntity agencyInfo) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<AgencyInfoEntity>> failures = validator.validate(agencyInfo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//保存
		agencyInfoService.saveOrUpdate(agencyInfo);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		agencyInfoService.deleteEntityById(AgencyInfoEntity.class, id);
	}
	
	
	/**
	 * @Title: saveUserOrgList   
	 * @Description: 保存销售顾问组织机构信息
	 * @param: @param request
	 * @param: @param user      
	 * @return: void      
	 * @throws
	 */
    private void saveUserOrgList(String orgId, TSUser user) {
            TSDepart depart = new TSDepart();
            depart.setId(orgId);
            TSUserOrg userOrg = new TSUserOrg();
            userOrg.setTsUser(user);
            userOrg.setTsDepart(depart);
            
            systemService.save(userOrg);
    }
	
	
	/**
	 * @Title: saveRoleUser   
	 * @Description: 保存销售顾问角色信息
	 * @param: @param user
	 * @param: @param roleidstr      
	 * @return: void      
	 * @throws
	 */
	protected void saveRoleUser(TSUser user, String roleidstr) {
		String[] roleids = roleidstr.split(",");
		for (int i = 0; i < roleids.length; i++) {
			TSRoleUser rUser = new TSRoleUser();
			TSRole role = systemService.getEntity(TSRole.class, roleids[i]);
			rUser.setTSRole(role);
			rUser.setTSUser(user);
			systemService.save(rUser);

		}
	}
}

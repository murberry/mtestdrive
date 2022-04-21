package com.mtestdrive.web.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.vo.datatable.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.druid.util.Utils;
import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.MaseratiConstants.DriveRecodsStatus;
import com.mtestdrive.MaseratiConstants.SalesmanStatus;
import com.mtestdrive.dto.SalesmanInfoDto;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.entity.TokenEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.SalesmanInfoServiceI;
import com.mtestdrive.service.TokenServiceI;
import com.mtestdrive.vo.DriveRecodsVo;
import com.mtestdrive.vo.SalesmanInfoVo;

@Controller
@RequestMapping("/login")
public class LoginAction extends BaseController {
	private static final Logger logger = Logger.getLogger(ObdInfoAction.class);

	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;
	@Autowired
	private TokenServiceI tokenService;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private AgencyInfoServiceI agencyInfoService;

	@RequestMapping(params = "login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(HttpServletRequest request) {
		// 查询是否已登录
		String uuid = null;
		String md5 = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals("uuid")) {
					uuid = cookie.getValue();
				}
				if (cookie.getName().equals("md5")) {
					md5 = cookie.getValue();
				}
			}
			if (StringUtil.isNotEmpty(uuid) && StringUtil.isNotEmpty(md5)) {
				CriteriaQuery cq = new CriteriaQuery(TokenEntity.class);
				cq.eq("userId", uuid);
				cq.eq("token", md5);
				cq.add();
				List<TokenEntity> tokenList = tokenService.getListByCriteriaQuery(cq, false);
				if (tokenList == null || tokenList.isEmpty()) {
					return new ModelAndView("login");
				}
				Date createTime = tokenList.get(0).getCreateTime();
				Calendar cal = Calendar.getInstance();
				cal.setTime(createTime);
				cal.add(Calendar.DATE, 30);//30天内免登录
				Date time = cal.getTime();
				boolean after = time.after(new Date());
				if (after) {
					if (tokenList.size() != 0) {
						
						DetachedCriteria dc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
						dc.add(Restrictions.eq("id", uuid));
						dc.add(Restrictions.eq("status", ConstantStatus.VALID));
						SalesmanInfoEntity salesmanInfo = salesmanInfoService.get(SalesmanInfoEntity.class, uuid);
						if (salesmanInfo != null && StringUtil.isNotEmpty(salesmanInfo.getId())
								&& StringUtil.isNotEmpty(salesmanInfo.getAgencyId())
								&& StringUtil.isNotEmpty(salesmanInfo.getMobile())) {
							SalesmanInfoVo salesmanInfoVo = new SalesmanInfoVo();
							try {
								BeanUtils.copyProperties(salesmanInfoVo, salesmanInfo);
								if (StringUtils.isNotEmpty(salesmanInfoVo.getAgencyId())) {
									AgencyInfoEntity agencyInfo = agencyInfoService.get(AgencyInfoEntity.class,
											salesmanInfoVo.getAgencyId());
									if (agencyInfo != null) {
										salesmanInfoVo.setAddress(agencyInfo.getAddress());
										salesmanInfoVo.setAgencyName(agencyInfo.getName());
									}else
										return new ModelAndView("login");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							request.getSession().setAttribute("SalesmanInfo", salesmanInfoVo);
							return new ModelAndView(new RedirectView("login.action?page"));
						}
					}
				}
			}
		}

		return new ModelAndView("login");
	}

	@RequestMapping(params = "out", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView out(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		Cookie cookie = new Cookie("uuid", null);
		// 设置失效为0 ，即马上失效
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		cookie = new Cookie("md5", null);
		// 设置失效为0 ，即马上失效
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return new ModelAndView("login");
	}

	@RequestMapping(params = "check", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String check(@RequestBody SalesmanInfoDto salesman, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO 登录校验
		String mobile = salesman.getMobile();
		String password = salesman.getPassword();
		if (StringUtil.isEmpty(mobile)) {
			return "2";
		}

		List<SalesmanInfoEntity> listSalesmanInfo = null;
		try {
			CriteriaQuery cq = new CriteriaQuery(SalesmanInfoEntity.class);
			cq.eq("mobile", mobile);
			cq.eq("password", password);
			cq.eq("status", ConstantStatus.VALID);
			cq.add();
			listSalesmanInfo = salesmanInfoService.getListByCriteriaQuery(cq, false);
			if (ListUtils.isNullOrEmpty(listSalesmanInfo)) {
				return "2";
			}else{
				AgencyInfoEntity agencyInfo = agencyInfoService.get(AgencyInfoEntity.class,
						listSalesmanInfo.get(0).getAgencyId());
				if (agencyInfo == null) {
					return "2";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 顾问
		List<SalesmanInfoVo> salesmanInfoVoList = null;
		try {
			salesmanInfoVoList = ListUtils.copyTo(listSalesmanInfo, SalesmanInfoVo.class);
			for (SalesmanInfoVo salesmanInfoVo : salesmanInfoVoList) {
				if (StringUtils.isNotEmpty(salesmanInfoVo.getAgencyId())) {
					AgencyInfoEntity agencyInfo = agencyInfoService.get(AgencyInfoEntity.class,
							salesmanInfoVo.getAgencyId());
					if (agencyInfo != null) {
						salesmanInfoVo.setAddress(agencyInfo.getAddress());
						salesmanInfoVo.setAgencyName(agencyInfo.getName());
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.getSession().setAttribute("SalesmanInfo", salesmanInfoVoList.get(0));
		request.setAttribute("SalesmanInfo", salesmanInfoVoList.get(0));
		// 登录成功存储cooke
		// uuid
		String uuid = listSalesmanInfo.get(0).getId();
		String md5 = Utils.md5(System.currentTimeMillis() + "");
		Cookie cookie = new Cookie("uuid", uuid);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		Cookie cookie2 = new Cookie("md5", md5);
		cookie2.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);
		response.addCookie(cookie2);
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setUserId(uuid);
		tokenEntity.setToken(md5);
		tokenEntity.setCreateTime(new Date());
		tokenService.save(tokenEntity);
		// 获取
		return "1";
	}

	@RequestMapping(params = "page", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView page(HttpServletRequest request) {
		List<DriveRecodsVo> driveRecodses = null;
		SalesmanInfoVo salesmanInfo = (SalesmanInfoVo) request.getSession().getAttribute("SalesmanInfo");
		List<DriveRecodsEntity> listDriveRecods = null;
		try {
			CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
			cq.eq("salesman.id", salesmanInfo.getId());
			cq.ge("orderStartTime", DateUtils.str2Date(DateUtils.formatDate(new Date()), DateUtils.date_sdf));
			cq.lt("orderEndTime", DateUtils.addDate(DateUtils.formatDate(new Date()), 1));
			cq.add();
			cq.addOrder("orderStartTime", SortDirection.asc);
			listDriveRecods = driveRecodsService.getListByCriteriaQuery(cq, false);
			if (!ListUtils.isNullOrEmpty(listDriveRecods)) {
				driveRecodses = ListUtils.copyTo(listDriveRecods, DriveRecodsVo.class);
				for (DriveRecodsVo driveRecodsVo : driveRecodses) {
					if (StringUtils.isNotEmpty(driveRecodsVo.getCarId())) {
						CarInfoEntity car = carInfoService.get(CarInfoEntity.class, driveRecodsVo.getCarId());
						if (car != null) {
							MyBeanUtils.copyBean2Bean(driveRecodsVo.getCar(), car);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 潜在试驾
		List<DriveRecodsVo> potentialDrive = new ArrayList<DriveRecodsVo>();
		// 已确认
		List<DriveRecodsVo> confirmedDrive = new ArrayList<DriveRecodsVo>();
		// 手续办理
		List<DriveRecodsVo> formalitiesDrive = new ArrayList<DriveRecodsVo>();
		// 开始试驾
		List<DriveRecodsVo> startDrive = new ArrayList<DriveRecodsVo>();
		// 正在试驾
		List<DriveRecodsVo> testDrive = new ArrayList<DriveRecodsVo>();
		// 试驾也完成
		List<DriveRecodsVo> finishDrive = new ArrayList<DriveRecodsVo>();
		if (!ListUtils.isNullOrEmpty(driveRecodses)) {
			for (DriveRecodsVo driveRecodsEntity : driveRecodses) {
				Integer status = driveRecodsEntity.getStatus();
				if (status == DriveRecodsStatus.ASK) {
					potentialDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.ANSWER) {
					confirmedDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.CONFIRMED || status == DriveRecodsStatus.FORMALITIES) {
					formalitiesDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.FORMALITIES) {
					startDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.UNDERWAY) {
					testDrive.add(driveRecodsEntity);
				}
				if (status == DriveRecodsStatus.COMPLETE || status == DriveRecodsStatus.GENERATEDREPORT) {
					finishDrive.add(driveRecodsEntity);
				}
			}
		}
		request.setAttribute("potentialDrive", potentialDrive);
		request.setAttribute("confirmedDrive", confirmedDrive);
		request.setAttribute("formalitiesDrive", formalitiesDrive);
		request.setAttribute("startDrive", startDrive);
		request.setAttribute("testDrive", testDrive);
		request.setAttribute("finishDrive", finishDrive);
		// 计算预约成功率
		List<DriveRecodsEntity> allList = null;
		CriteriaQuery cq = new CriteriaQuery(DriveRecodsEntity.class);
		cq.eq("salesman.id", salesmanInfo.getId());
		cq.add();
		try {
			allList = driveRecodsService.getListByCriteriaQuery(cq, false);
		} catch (org.hibernate.ObjectNotFoundException e) {
			logger.error(e.getMessage());
		}

		List<DriveRecodsEntity> status8List = null;
		CriteriaQuery cq2 = new CriteriaQuery(DriveRecodsEntity.class);
		cq2.eq("salesman.id", salesmanInfo.getId());
		cq2.eq("status", DriveRecodsStatus.GROUP);
		cq2.add();
		status8List = driveRecodsService.getListByCriteriaQuery(cq2, false);
		int all = 0;
		if (!ListUtils.isNullOrEmpty(allList))
			all = allList.size();
		int s = status8List.size() * 100;

		int d = 0;
		if (all != 0) {
			d = s / all;
		}
		double ceil = Math.ceil(d);
		salesmanInfo.setCeil(ceil);
		request.getSession().setAttribute("SalesmanInfo", salesmanInfo);
		request.setAttribute("SalesmanInfo", salesmanInfo);
		return new ModelAndView("index");
	}

	/**
	 * @Title: jumpToRetrievePasswordPage
	 * @Description: 跳转到找回密码页面
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: ModelAndView
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "jumpToRetrievePasswordPage", method = { RequestMethod.GET })
	public ModelAndView jumpToRetrievePasswordPage(HttpServletRequest request) {
		return new ModelAndView("verificationCode");
	}

	/**
	 * @Title: jumpToSetNewPasswordPage
	 * @Description: 跳转到找设置新密码页面
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: ModelAndView
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "jumpToSetNewPasswordPage", method = { RequestMethod.GET })
	public ModelAndView jumpToSetNewPasswordPage(HttpServletRequest request) {
		return new ModelAndView("setNewPassword");
	}

	/**
	 * @Title: setNewPassword
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param: @param
	 *             phone
	 * @param: @param
	 *             password
	 * @param: @param
	 *             repPassword
	 * @param: @param
	 *             request
	 * @param: @return
	 * @return: AjaxJson
	 * @throws @author:
	 *             mengtaocui
	 */
	@RequestMapping(params = "setNewPassword", method = { RequestMethod.POST })
	@ResponseBody
	public AjaxJson setNewPassword(@RequestParam(required = true) String phone,
			@RequestParam(required = true) String password, @RequestParam(required = true) String repPassword,
			HttpServletRequest request) {
		AjaxJson aj = new AjaxJson();
		if (password.equals(repPassword)) {
			aj.setSuccess(true);
			DetachedCriteria dc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
			dc.add(Restrictions.eq("mobile", phone));
			dc.add(Restrictions.eq("status", ConstantStatus.VALID));
			List<SalesmanInfoEntity> sieList = salesmanInfoService.findByDetached(dc);
			if (!ListUtils.isNullOrEmpty(sieList)) {
				SalesmanInfoEntity sie = sieList.get(0);
				if (sie != null && StringUtil.isNotEmpty(sie.getId()) ) {
					// 修改密码
					sie.setPassword(repPassword);
					salesmanInfoService.updateEntitie(sie);
					aj.setSuccess(true);
				} else {
					aj.setSuccess(false);
					aj.setMsg("用户不存在，请联系管理员");
				}
			} else {
				aj.setSuccess(false);
				aj.setMsg("用户不存在，请联系管理员");
			}
		} else {
			aj.setSuccess(false);
			aj.setMsg("密码不一致");
		}
		return aj;
	}
}

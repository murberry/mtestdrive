package org.jeecgframework.web.demo.service.impl.test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtestdrive.MaseratiConstants.CarStatus;
import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.MaseratiConstants.CustomerSourceType;
import com.mtestdrive.MaseratiConstants.DriveRecodsStatus;
import com.mtestdrive.MaseratiConstants.SFInfo;
import com.mtestdrive.entity.AgencyInfoEntity;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.CustomerInfoEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.SalesmanInfoEntity;
import com.mtestdrive.service.AgencyInfoServiceI;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.CustomerInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.SalesmanInfoServiceI;
import com.mtestdrive.utils.HttpClientUtil;

@Service("salesforceService")
public class SalesforceServiceImpl {
	private static final Logger logger = Logger.getLogger(SalesforceServiceImpl.class);
	
    @Autowired
	private AgencyInfoServiceI agencyInfoService;
	@Autowired
	private SalesmanInfoServiceI salesmanInfoService;
	@Autowired
	private CarInfoServiceI carInfoService;
	@Autowired
	private CustomerInfoServiceI customerInfoService;
	@Autowired
	private DriveRecodsServiceI driveRecodsService;

	@Autowired
	private SystemService sysService;

	/**
	 * @Title: work   
	 * @Description:从SF获取数据  
	 * @param: @throws UnsupportedEncodingException
	 * @param: @throws ParseException      
	 * @return: void      
	 * @throws
	 */
	public void work() throws UnsupportedEncodingException, ParseException {
//		fetchAgencyInfo(); // 经销商
//		fetchSalesmanInfo();// 销售顾问
//		fetchCarInfo(); // 车辆信息
//		fetchTestDriveAppointment();// 试驾预约信息
//		fetchCustomerInfo();// 客户信息
		fetchTestDrivePurchase();// 试驾购买信息
	}
	
	/**
	 * @Title: fetchTestDriveAppointment
	 * @Description: 同步试驾预约数据 	说明：双向同步 试驾车平台<-->SF
	 * @param: @throws ParseException      
	 * @return: void      
	 * @throws
	 */
	public void fetchTestDriveAppointment() throws ParseException {
		logger.info("获取试驾预约数据   begin");
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("获取试驾预约数据---->");
		JSONArray array = getQueryList("select+id,OwnerId,Name__c,name,Gender__c,Cancelled__c,Followupstate__c,ApplicationSource__c,TestDriveCarOrder__c,Mobile__c,Dealer_Code__C,AppointmentTimeSlot__c,AppointmentDate__c,AppointmentType__c,Purchase_Plan__c,Vin__c,Province__c,City__c+from+TestDriveAppointment__c+where+SystemModStamp=TODAY");
		//JSONArray array = getAllDriveAppointment("select+id,OwnerId,Name__c,name,Gender__c,Cancelled__c,Followupstate__c,ApplicationSource__c,TestDriveCarOrder__c,Mobile__c,Dealer_Code__C,AppointmentTimeSlot__c,AppointmentDate__c,AppointmentType__c,Purchase_Plan__c,Vin__c,Province__c,City__c+from+TestDriveAppointment__c+limit+200");
		if (array != null) {
			List<DriveRecodsEntity> saveEn = new ArrayList<DriveRecodsEntity>();
			List<DriveRecodsEntity> upEn = new ArrayList<DriveRecodsEntity>();
			for (int i = 0; i < array.length(); i++) {
				DriveRecodsEntity driveRecodsEntity = new DriveRecodsEntity();
				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject obj = new JSONObject(jsonObject.toString());
				
				// 取出参数进行保存 //id
				driveRecodsEntity.setSfId(StringUtil.getStrByObj(obj.get("Id")));

				int recodsStatus = 0;
				if ((Boolean) obj.get("Cancelled__c"))
					recodsStatus = DriveRecodsStatus.CANCEL;
				
				if("已跟进".equals(StringUtil.getStrByObj(obj.get("Followupstate__c"))))
					recodsStatus = DriveRecodsStatus.ANSWER;
				
				String AppointmentTimeSlot = StringUtil.getStrByObj(obj.get("AppointmentTimeSlot__c"));
				String[] time = AppointmentTimeSlot.split("-"); 
				String AppointmentDate = StringUtil.getStrByObj(obj.get("AppointmentDate__c"));
				String startTime = AppointmentDate + " " + time[0];
				String endTime = AppointmentDate + " " + time[1];
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date orderStartTime = sdf.parse(startTime);
				Date orderEndTime = sdf.parse(endTime);
				
				String mobile = StringUtil.getStrByObj(obj.get("Mobile__c"));//客户手机号
				String cusName = StringUtil.getStrByObj(obj.get("Name__c"));//客户手机号
				cusName.replaceAll(",", " ");
				
				driveRecodsEntity.setStatus(recodsStatus); 
				driveRecodsEntity.setOrderStartTime(orderStartTime);
				driveRecodsEntity.setOrderEndTime(orderEndTime); 
				
				//车辆信息
				String carId = StringUtil.getStrByObj(obj.get("TestDriveCarOrder__c"));
				String vin = StringUtil.getStrByObj(obj.get("Vin__c"));
				DetachedCriteria carDc = DetachedCriteria.forClass(CarInfoEntity.class);
				carDc.add(Restrictions.ne("status", ConstantStatus.INVALID));
				carDc.add(Restrictions.eq("vin", vin));
				List<CarInfoEntity> cars = carInfoService.findByDetached(carDc);
				
				if(ListUtils.isNullOrEmpty(cars)){
					logMsg.append("预约车辆不存在，SFID："+carId);
					logger.error(logMsg);
					sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
				}
				else{
					CarInfoEntity car = cars.get(0);
					driveRecodsEntity.setCarId(car.getId());
					driveRecodsEntity.setCarType(car.getType());
				}
				
				//经销商信息
				String code = StringUtil.getStrByObj(obj.get("Dealer_Code__c"));
				AgencyInfoEntity agencyInfoEntity = null;
				if(!"".equals(code)){
					agencyInfoEntity = agencyInfoService.getAgencyByCode(code);
					if(agencyInfoEntity != null){
						driveRecodsEntity.setAgency(agencyInfoEntity);
						
						//销售顾问信息
						String salesManIdSf = StringUtil.getStrByObj(obj.get("OwnerId"));
						SalesmanInfoEntity salesMan = getSfSalesman(agencyInfoEntity.getId(), salesManIdSf);
						if(salesMan != null){
							driveRecodsEntity.setSalesman(salesMan);
							//客户信息
							CustomerInfoEntity customer = customerInfoService.getCustomerByMobileAndSalesManId(mobile, salesMan.getId());
						    if(customer != null)
						    	driveRecodsEntity.setCustomer(customer);
						    else{
						    	CustomerInfoEntity memCus = new CustomerInfoEntity();
						    	memCus.setAgencyId(agencyInfoEntity.getId());
						    	memCus.setCreateBy(salesMan.getId());
						    	memCus.setCreateTime(DateUtils.gettimestamp());
						    	memCus.setMobile(mobile);
						    	memCus.setName(cusName);
						    	memCus.setStatus(ConstantStatus.VALID);
						    	customerInfoService.save(memCus);
						    	driveRecodsEntity.setCustomer(memCus);
						    }
							
						}else{
							CustomerInfoEntity memCus = new CustomerInfoEntity();
					    	memCus.setAgencyId(agencyInfoEntity.getId());
					    	memCus.setCreateTime(DateUtils.gettimestamp());
					    	memCus.setMobile(mobile);
					    	memCus.setName(cusName);
					    	memCus.setStatus(ConstantStatus.VALID);
					    	customerInfoService.save(memCus);
					    	driveRecodsEntity.setCustomer(memCus);
							
							logMsg.append("销售顾问不存在，SFID："+salesManIdSf);
							logger.error(logMsg);
							sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
						}
						
					}else{
						logMsg.append("经销商不存在，代码："+code);
						logger.error(logMsg);
						sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
					}
				}

				DriveRecodsEntity driveRecodes = driveRecodsService.findUniqueByProperty(DriveRecodsEntity.class,
						"sfId", driveRecodsEntity.getSfId());
				if (driveRecodes == null) {
					driveRecodsEntity.setCreateBy(SFInfo.USERNAME);
					driveRecodsEntity.setCreateTime(DateUtils.getTimestamp());
					saveEn.add(driveRecodsEntity);
				} else {
					driveRecodes.setStatus(recodsStatus);
					driveRecodes.setCarId(carId);
					driveRecodes.setAgency(agencyInfoEntity);
					driveRecodes.setOrderStartTime(orderStartTime);
					driveRecodes.setOrderEndTime(orderEndTime);
					driveRecodes.setUpdateBy(SFInfo.USERNAME);
					driveRecodes.setUpdateTime(DateUtils.gettimestamp());
					upEn.add(driveRecodes);
				}
			}
			// 批量保存或修改
			if (!ListUtils.isNullOrEmpty(saveEn))
				sysService.batchSave(saveEn);
			if (!ListUtils.isNullOrEmpty(upEn))
				sysService.batchSave(upEn);
		}
		logger.info("获取试驾预约数据  end");
	}


	/**
	 * @Title: fetchCustomerInfo
	 * @Description: 同步SF客户数据
	 * @param: @param isAll      
	 * @return: void      
	 * @throws
	 */
	public void fetchCustomerInfo() {
		logger.info("获取SF客户数据   begin");
		sysService.addSimpleLog("获取SF客户数据---->", Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		JSONArray array = getQueryList("select+id,name,owner.Dealercode__c,OwnerId,PersonBirthdate,PersonMobilePhone,AccountSource__c,AccountSourceDetail__c,Genger__c,Customer_types__c+from+Account+where+SystemModStamp=TODAY");

		if (array != null) {
			List<CustomerInfoEntity> saveEn = new ArrayList<CustomerInfoEntity>();
			List<CustomerInfoEntity> upEn = new ArrayList<CustomerInfoEntity>();
			for (int i = 0; i < array.length(); i++) {
				CustomerInfoEntity customerInfo = new CustomerInfoEntity();
				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject obj = new JSONObject(jsonObject.toString());
				net.sf.json.JSONObject owner = JSONHelper.toJSONObject(StringUtil.getStrByObj(obj.get("Owner")));
				
				String gender = StringUtil.getStrByObj(obj.get("Genger__c"));
				
				customerInfo.setSfId(StringUtil.getStrByObj(obj.get("Id")));
				customerInfo.setName(StringUtil.getStrByObj(obj.get("Name")));
				customerInfo.setMobile(StringUtil.getStrByObj(obj.get("PersonMobilePhone")));
				String type = StringUtil.getStrByObj(obj.get("Customer_types__c"));
				customerInfo.setType(StringUtil.isNotEmpty(type)?type:"-");
				
				AgencyInfoEntity agen = agencyInfoService.getAgencyByCode(StringUtil.getStrByObj(owner.get("Dealercode__c")));
				if(agen != null){
					customerInfo.setAgencyId(agen.getId());
					
					//SF中销售顾问的ID
					String salesManSfId = StringUtil.getStrByObj(obj.get("OwnerId"));
					SalesmanInfoEntity salesMan = getSfSalesman(agen.getId(), salesManSfId);
					if(salesMan != null)
						customerInfo.setCreateBy(salesMan.getId());
					else{
						String msg = "经销商"+StringUtil.getStrByObj(owner.get("Dealercode__c"))+"销售顾问不存在，SFID："+salesManSfId;
						logger.error(msg);
						sysService.addSimpleLog(msg, Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
					}
				}else{
					String msg = "经销商不存在，code："+StringUtil.getStrByObj(owner.get("Dealercode__c"));
					logger.error(msg);
					sysService.addSimpleLog(msg, Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
				}
				
				customerInfo.setStatus(ConstantStatus.VALID);
				
				if(StringUtil.isEmpty(gender))
					customerInfo.setGender(0);//性别未知
				else if("男".equals(gender))
					customerInfo.setGender(1);//男
				else 
					customerInfo.setGender(2);//女
				if(obj.get("PersonBirthdate") != null){
					try {
						customerInfo.setBirthday(DateUtils.str2Date(
								StringUtil.getStrByObj(obj.get("PersonBirthdate")), DateUtils.date_sdf));
					} catch (JSONException e) {
					}
				}
				customerInfo.setSource(CustomerSourceType.FROM_SALEFORCE);//设置客户来源为SF
				
				CustomerInfoEntity dbCustomerInfo = null;
			
				List<CustomerInfoEntity> bsCus = customerInfoService.findByProperty(CustomerInfoEntity.class, "sfId", customerInfo.getSfId());
				if(ListUtils.isNullOrEmpty(bsCus)){
					customerInfo.setCreateTime(DateUtils.getTimestamp());
					//customerInfo.setCreateBy(SFInfo.USERNAME);
					customerInfo.setStatus(ConstantStatus.VALID);
					saveEn.add(customerInfo);
				}else{
					dbCustomerInfo = bsCus.get(0);
					dbCustomerInfo.setName(customerInfo.getName());
					dbCustomerInfo.setMobile(customerInfo.getMobile());
					dbCustomerInfo.setUpdateBy(SFInfo.USERNAME);
					dbCustomerInfo.setUpdateTime(DateUtils.gettimestamp());
					upEn.add(dbCustomerInfo);
				}
			}// end for
			// 批量保存或修改
			if (!ListUtils.isNullOrEmpty(saveEn))
				sysService.batchSave(saveEn);
			if (!ListUtils.isNullOrEmpty(upEn))
				sysService.batchUpdate(upEn);
		}
		logger.info("获取SF客户数据   end");
	}

	/**
	 * @Title: fetchCarInfo
	 * @Description: 获取车辆数据
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void fetchCarInfo() {
		logger.info("获取车辆数据      begin");
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("获取车辆数据---->");
		JSONObject carObject = getDriveAppointment("select+id,name,FactoryOrders__c,order_model_type__c,Test_Driving__c,Frame_chassis_ID__c,DealerCode__c,DealerLookup__c,license_plate_number__c+from+order__c+where+Test_Driving__c=true+and+SystemModStamp=TODAY");
        //JSONObject carObject = getDriveAppointment("select+id,name,FactoryOrders__c,order_model_type__c,Test_Driving__c,Frame_chassis_ID__c,DealerCode__c,DealerLookup__c,license_plate_number__c+from+order__c+where+Test_Driving__c=true+limit+200");
		JSONArray array = (JSONArray) carObject.get("records");
		if (array != null) {
			List<CarInfoEntity> saveEn = new ArrayList<CarInfoEntity>();
			List<CarInfoEntity> upEn = new ArrayList<CarInfoEntity>();
			CarInfoEntity car = null;
			JSONObject jsonObject = null;
			JSONObject obj = null;
			
			for (int i = 0; i < array.length(); i++) {
				car = new CarInfoEntity();
				jsonObject = array.getJSONObject(i);
				obj = new JSONObject(jsonObject.toString());
				
				// 取出参数进行保存
				car.setSfId(StringUtil.getStrByObj(obj.get("Id")));
				car.setName(StringUtil.getStrByObj(obj.get("Name")));
				car.setType(StringUtil.getStrByObj(obj.get("order_model_type__c"))); 
				car.setVin(StringUtil.getStrByObj(obj.get("Frame_chassis_ID__c")));
				car.setPlateNo(StringUtil.getStrByObj(obj.get("license_plate_number__c")));
				
				String code = StringUtil.getStrByObj(obj.get("DealerCode__c"));
				AgencyInfoEntity agencyInfoEntity = agencyInfoService.getAgencyByCode(code);
				if(agencyInfoEntity != null)
					car.setAgency(agencyInfoEntity);
				else{
					logMsg.append("经销商不存在，经销商代码："+code);
					logger.error(logMsg);
					sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
				}
				
				
				DetachedCriteria carDc = DetachedCriteria.forClass(CarInfoEntity.class);
				carDc.add(Restrictions.ne("status", 0));
				carDc.add(Restrictions.eq("vin", car.getVin()));
				List<CarInfoEntity> carList = carInfoService.findByDetached(carDc);
				CarInfoEntity findUniqueByProperty = null;
				if(ListUtils.isNullOrEmpty(carList)){
					car.setStatus(CarStatus.NO_USED);
					car.setCreateBy(SFInfo.USERNAME);
					car.setCreateTime(DateUtils.getTimestamp());
					saveEn.add(car);
				}else{
					findUniqueByProperty = carList.get(0);
					findUniqueByProperty.setName(car.getName());
					findUniqueByProperty.setType(car.getType());
					findUniqueByProperty.setPlateNo(car.getPlateNo());
					findUniqueByProperty.setCode(code);
					findUniqueByProperty.setSfId(car.getSfId());
					findUniqueByProperty.setUpdateBy(SFInfo.USERNAME);
					findUniqueByProperty.setUpdateTime(DateUtils.getTimestamp());
					upEn.add(findUniqueByProperty);
				}
			}
			// 批量保存或修改
			if (!ListUtils.isNullOrEmpty(saveEn))
				sysService.batchSave(saveEn);
			if (!ListUtils.isNullOrEmpty(upEn))
				sysService.batchUpdate(upEn);
		}
		logger.info("获取车辆数据      end");
	}
	/**
	 * @Title: fetchSalesmanInfo
	 * @Description: 获取销售顾问数据  说明：单向获取 SF-->试驾车平台
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void fetchSalesmanInfo() {
		logger.info("获取销售顾问数据      begin");
		StringBuilder logMsg = new StringBuilder();
		logMsg.append("获取销售顾问数据---->");
	    JSONArray array = getQueryList("select+id,name,Username,DealerCode__c,MobilePhone+from+User+where+SystemModStamp=TODAY");
		//JSONArray array = getAllDriveAppointment("select+id,name,Username,DealerCode__c,MobilePhone+from+User+limit+200");
		if (array != null) {
			List<SalesmanInfoEntity> saveEn = new ArrayList<SalesmanInfoEntity>();
			List<SalesmanInfoEntity> upEn = new ArrayList<SalesmanInfoEntity>();

			for (int i = 0; i < array.length(); i++) {
				SalesmanInfoEntity salesman = new SalesmanInfoEntity();
				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject obj = new JSONObject(jsonObject.toString());
				// 取出参数进行保存
				salesman.setSfId(StringUtil.getStrByObj(obj.get("Id"))); 
				salesman.setName(StringUtil.getStrByObj(obj.get("Name")));
				salesman.setMobile(StringUtil.getStrByObj(obj.get("MobilePhone")));

				String code = StringUtil.getStrByObj(obj.get("Dealercode__c"));
				
				List<AgencyInfoEntity> agenList = agencyInfoService.findByProperty(AgencyInfoEntity.class, "code", code);
				if(ListUtils.isNullOrEmpty(agenList)){
					logMsg.append("经销商不存在，经销商代码："+code);
					logger.error(logMsg);
					sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
				}else{
					if(agenList.get(0) != null)
						salesman.setAgencyId(agenList.get(0).getId());
					else{
						logMsg.append("经销商不存在，经销商代码："+code);
						logger.error(logMsg);
						sysService.addSimpleLog(logMsg.toString(), Globals.Log_Type_OTHER, Globals.Log_Leavel_ERROR);
					}
				}
				
				// 查一下有了没有 有了就修改 没有就添加
				SalesmanInfoEntity salesmanInfoEntity = salesmanInfoService
						.findUniqueByProperty(SalesmanInfoEntity.class, "sfId", salesman.getSfId());
				if (salesmanInfoEntity == null) {
					salesman.setCreateBy(SFInfo.USERNAME);
					salesman.setCreateTime(DateUtils.gettimestamp());
					salesman.setStatus(ConstantStatus.VALID);
					salesman.setPassword("123456");
					saveEn.add(salesman);
				} else {
					salesmanInfoEntity.setName(salesman.getName());
					salesmanInfoEntity.setMobile(salesman.getMobile());
					salesmanInfoEntity.setAgencyId(salesman.getAgencyId());
					salesmanInfoEntity.setUpdateBy(SFInfo.USERNAME);
					salesmanInfoEntity.setUpdateTime(DateUtils.gettimestamp());
					upEn.add(salesmanInfoEntity);
				}
			}
			// 批量保存或修改
			if (!ListUtils.isNullOrEmpty(saveEn))
				sysService.batchSave(saveEn);
			if (!ListUtils.isNullOrEmpty(upEn))
				sysService.batchUpdate(upEn);
		}
		logger.info("获取销售顾问数据      end");
	}

	/**
	 * @Title: fetchAgencyInfo
	 * @Description: 获取经销商的数据 说明：单向获取 SF-->试驾车平台
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void fetchAgencyInfo() {
		logger.info("获取经销商的数据      begin");
		JSONArray array = getQueryList("select+id,name,Address__c,Province__c,City__c,Marketing_Team__c,SalesTeam__c,DealerCode__c,Disable_Dealer__c+from+Dealer__c+where+SystemModStamp=TODAY");
		if (array != null) {
			List<AgencyInfoEntity> saveEn = new ArrayList<AgencyInfoEntity>();
			List<AgencyInfoEntity> upEn = new ArrayList<AgencyInfoEntity>();
			for (int i = 0; i < array.length(); i++) {
				AgencyInfoEntity agency = new AgencyInfoEntity();
				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject obj = new JSONObject(jsonObject.toString());
				// 取出参数进行保存
				agency.setSfId(StringUtil.getStrByObj(obj.get("Id")));
				agency.setName(StringUtil.getStrByObj(obj.get("Name")));
				agency.setAddress(StringUtil.getStrByObj(obj.get("Address__c")));
				agency.setProvinceId(StringUtil.getStrByObj(obj.get("Province__c")));
				agency.setCityId(StringUtil.getStrByObj(obj.get("City__c")));
				agency.setCode(StringUtil.getStrByObj(obj.get("DealerCode__c")));
				
				// 查一下有了没有 有了就修改 没有就添加
				AgencyInfoEntity agencyInfoEntity = agencyInfoService.findUniqueByProperty(AgencyInfoEntity.class,
						"sfId", agency.getSfId());
				
				if (agencyInfoEntity == null) {
					agency.setStatus(ConstantStatus.VALID);
					agency.setCreateBy(SFInfo.USERNAME);
					agency.setCreateTime(DateUtils.getTimestamp());
					saveEn.add(agency);
				} else {
					agencyInfoEntity.setName(agency.getName());
					agencyInfoEntity.setAddress(agency.getAddress());
					agencyInfoEntity.setProvinceId(agency.getProvinceId());
					agencyInfoEntity.setCityId(agency.getCityId());
					agencyInfoEntity.setCode(agency.getCityId());
					agencyInfoEntity.setUpdateBy(SFInfo.USERNAME);
					agencyInfoEntity.setUpdateTime(DateUtils.getTimestamp());
					upEn.add(agencyInfoEntity);
				}
			}
			// 批量保存或修改
			if (!ListUtils.isNullOrEmpty(saveEn))
				sysService.batchSave(saveEn);
			if (!ListUtils.isNullOrEmpty(upEn))
				sysService.batchUpdate(upEn);
		}
		logger.info("获取经销商的数据      end");
	}

	/**
	 * @Title: 获取试驾后的购买信息
	 * @Description: 获取试驾后的购买信息 SF-->试驾车平台
	 * @param:
	 * @return: void
	 * @throws
	 */
	public void fetchTestDrivePurchase() {
		logger.info("获取试驾后的购买信息      begin");

		//取增量时间
		Date sfModified = (Date)driveRecodsService.findOneForJdbc("select max(sf_modified) as max_sf_modified from t_drive_recods").get("max_sf_modified");
		if(null==sfModified) {
			Calendar cal=Calendar.getInstance();
			cal.clear();
			cal.set(Calendar.YEAR,2017);
			cal.set(Calendar.MONTH,1);//2017-01
			sfModified = cal.getTime();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//UTC国际标准时间格式
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));//sf的时区时间为国际标准时间

		//远程取更新值
		String sql="select+Id,GPSExternalID__c,PurchaseDealer__c,CarPurchase__c+from+GPSTestDrive__c+where+CarPurchase__c=true+and+LastModifiedDate>"+sdf.format(sfModified);
		JSONArray array = getQueryList(sql);
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				JSONObject obj = new JSONObject(jsonObject.toString());
				// 取出参数进行保存
				String sfId = StringUtil.getStrByObj(obj.get("GPSExternalID"));
				DriveRecodsEntity driveRec = driveRecodsService.get(DriveRecodsEntity.class, sfId);
				driveRec.setSfId(obj.getString("Id"));
				driveRec.setPurchaseDealer(obj.getString("PurchaseDealer__c"));
				driveRec.setHasCarPurchase(obj.getBoolean("CarPurchase__c"));
				driveRec.setSfModified(new Date(obj.getLong("LastModifiedDate")));
				sysService.updateEntitie(driveRec);
			}
			logger.info("保存试驾后的购买信息条数="+array.length());
		} else {
			logger.info("SF无新的试驾后的购买信息 sql=:"+sql);
		}
		logger.info("获取试驾后的购买信息       end");
	}

	private static JSONArray getQueryList(String sql) {
		String jsonStr = HttpClientUtil.doGetQueryList(sql);
		if (jsonStr == null) {
			return null;
		} else {
			JSONObject a = new JSONObject(jsonStr);
			JSONArray array = (JSONArray) a.get("records");
			return array;
		}
	}

	private static JSONObject getDriveAppointment(String sql) {
		String doGetGetDriveAppointment = HttpClientUtil.doGetQueryList(sql);
		if (doGetGetDriveAppointment == null) {
			return null;
		} else {
			JSONObject a = new JSONObject(doGetGetDriveAppointment);
			return a;
		}
	}
	
	public  SalesmanInfoEntity getSfSalesman(String agenId,String sfid){
		JSONArray array = getQueryList("select+id,name,Username,DealerCode__c,MobilePhone+from+User+where+id='"+sfid+"'");
		if(array != null && array.length() > 0){
			JSONObject obj = (JSONObject) JSONObject.wrap(array.get(0));
			String mobile = StringUtil.getStrByObj(obj.get("MobilePhone"));
			DetachedCriteria dc = DetachedCriteria.forClass(SalesmanInfoEntity.class);
			dc.add(Restrictions.eq("agencyId", agenId));
			dc.add(Restrictions.eq("mobile", mobile));
			dc.add(Restrictions.eq("status", ConstantStatus.VALID));
			
			List<SalesmanInfoEntity> saList = agencyInfoService.findByDetached(dc);
			if(!ListUtils.isNullOrEmpty(saList))
				return saList.get(0);
			else
				return null;
		}
		return null;
	}
	
	
	public static void main(String[] args){
		//JSONArray array = getAllDriveAppointment("select+id,name,Address__c,Province__c,City__c,Marketing_Team__c,SalesTeam__c,DealerCode__c,Disable_Dealer__c+from+Dealer__c+limit+200");
		//JSONArray array = getAllDriveAppointment("select+id,name,Username,DealerCode__c,MobilePhone+from+User+where+id='00590000003jK81AAE'");
		//JSONObject array = getDriveAppointment("select+id,name,FactoryOrders__c,order_model_type__c,Test_Driving__c,Frame_chassis_ID__c,DealerCode__c,DealerLookup__c,license_plate_number__c+from+order__c+where+Test_Driving__c=true+limit+200");
		JSONArray array = getQueryList("select+id,OwnerId,Name__c,name,Gender__c,Cancelled__c,Followupstate__c,ApplicationSource__c,TestDriveCarOrder__c,Mobile__c,Dealer_Code__C,AppointmentTimeSlot__c,AppointmentDate__c,AppointmentType__c,Purchase_Plan__c,Vin__c,Province__c,City__c+from+TestDriveAppointment__c+limit+200");
	    //JSONArray array = getAllDriveAppointment("select+id,name,owner.Dealercode__c,OwnerId,PersonBirthdate,PersonMobilePhone,AccountSource__c,AccountSourceDetail__c,Genger__c,Customer_types__c+from+Account+where+SystemModStamp=TODAY");
		System.out.println(array);
	}
}


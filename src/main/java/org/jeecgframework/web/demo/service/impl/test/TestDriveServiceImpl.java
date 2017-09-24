package org.jeecgframework.web.demo.service.impl.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.ChinaEntity;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.entity.ObdDriveRecodsEntity;
import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.entity.RouteInfoEntity;
import com.mtestdrive.service.CarInfoServiceI;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.service.ObdGatherInfoServiceI;
import com.mtestdrive.service.ReportRecordsServiceI;
import com.mtestdrive.service.RouteInfoServiceI;
import com.mtestdrive.service.impl.ObdGatherInfoServiceImpl;

@Service("testDriveService")
public class TestDriveServiceImpl {
	@Autowired
	private ObdGatherInfoServiceI  obdGatherInfoService;
	
	@Autowired
	private CarInfoServiceI  carInfoService;
	
	@Autowired
	private DriveRecodsServiceI  driveRecodsService;
	
	@Autowired
	private RouteInfoServiceI routeInfoService;
	
	public void work() throws ParseException{
		System.out.println("定时任务执行了");
		//获取昨天
		Date date=new Date();//取时间
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		 calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		 date=calendar.getTime(); //这个时间就是日期往后推一天的结果 
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 String dateString = formatter.format(date);
		//获取当天行驶的车辆OBDID
		List<ObdGatherInfoEntity> obdIdList = obdGatherInfoService.getObdIdByToday(dateString);
		for (ObdGatherInfoEntity obdGatherInfoEntity : obdIdList) {
			List<ObdGatherInfoEntity> list = new ArrayList<ObdGatherInfoEntity>();
			String termId = obdGatherInfoEntity.getTermid();
			//List<CarInfoEntity> carList = carInfoService.findByProperty(CarInfoEntity.class, "obdId", termId);
			List<CarInfoEntity> carList = carInfoService.getByCarObdId(termId);
			if(carList.size()!=0){
			System.out.println(termId);
			List<ObdGatherInfoEntity> obdList = obdGatherInfoService.getObdByTermidAndGnsstime(termId,dateString);
			System.out.println(obdList.size());
			List<String> timeList = new ArrayList<String>();
			List<ObdGatherInfoEntity> obdList2 = new  ArrayList<ObdGatherInfoEntity>();
			for (ObdGatherInfoEntity obdGatherInfo2 : obdList) {
				Boolean accOn = obdGatherInfo2.getAccOn();
				
					String gnsstime = obdGatherInfo2.getGnsstime();
					int i = 0;
					for (String string : timeList) {
						if(gnsstime.equals(string)){
							//有一样的（不重复）
							i=1;
							
						}else{
							//没有一样的（重复）
						}
					}
					if(i==1){
						//不在循环中删除 先放list里面
						obdList2.add(obdGatherInfo2);
					}
					if(!accOn){
						if(i==0){
							timeList.add(gnsstime);
						}
					}
				
			}
			obdList.removeAll(obdList2);
			for (ObdGatherInfoEntity obdGatherInfo : obdList) {
				Boolean accOn = obdGatherInfo.getAccOn();
				if(accOn!=null){
				
				if(accOn){
					//1 启动 将数据保存进List
					list.add(obdGatherInfo);
					String ListId = obdList.get(obdList.size()-1).getId();
					String newId = obdGatherInfo.getId();
					if(ListId == newId){
						if(list.size()>1){
						ObdDriveRecodsEntity obdDriveRecods = new ObdDriveRecodsEntity();
						String describe = "原因：";
						String carId = carList.get(0).getId();
						
						ObdGatherInfoEntity fristObd = list.get(0);
						ObdGatherInfoEntity lastObd = list.get(list.size()-1);
						//获取行驶距离
						float m = lastObd.getMileage()-fristObd.getMileage();
						Float km = m/1000;
						BigDecimal x1 = new BigDecimal(Float.toString(km));
						//根据车辆ID 获取当日预约
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date startDate = sdf.parse(fristObd.getGnsstime());
						Date endDate = sdf.parse(lastObd.getGnsstime());
						List<DriveRecodsEntity> driveRecodsList = new ArrayList<DriveRecodsEntity>();
						long x = endDate.getTime()-startDate.getTime();
						if(x>60000l){
							if(x>600000l){
								driveRecodsList = driveRecodsService.getByObdIdAndTimeOnHalf(carId, startDate, endDate);
								if(driveRecodsList.size()!=0){
									driveRecodsList = driveRecodsService.getByObdIdAndTime(carId, startDate, endDate);
								}else{
									describe = describe +" "+ "无试驾流程";
								}
								
							}else{
								describe = describe +" "+ "试驾少于10分钟";
							}
						if(driveRecodsList.size()==0){
							//没有预约的驾驶 或者是时间不对
							describe = describe +" "+ "试驾流程不规范";
							obdDriveRecods.setAgencyId(carList.get(0).getAgency().getId());
							obdDriveRecods.setSalesmanId("/");
							obdDriveRecods.setCustomerId("/");
							obdDriveRecods.setCarId(carId);
							obdDriveRecods.setCreateTime(new Date());
							obdDriveRecods.setDriveEndTime(endDate);
							obdDriveRecods.setDriveStartTime(startDate);
							obdDriveRecods.setMileage(x1);
							obdDriveRecods.setStatus(2);
							obdDriveRecods.setUpdateTime(new Date());
							obdDriveRecods.setDescription(describe);
							//试驾公里数达成率
							Float achievementKm= null;
							String agencyId =carList.get(0).getAgency().getId();
							List<RouteInfoEntity> routeInfoList = routeInfoService.getByAgencyId(agencyId);
							for (RouteInfoEntity routeInfoEntity : routeInfoList) {
								List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(routeInfoEntity.getTermId(), routeInfoEntity.getStartTime(), routeInfoEntity.getEndTime());
								if (obdGathers != null && !obdGathers.isEmpty()) {
									Float mileageFrist = obdGathers.get(0).getMileage();
									Float mileageLast = obdGathers.get(obdGathers.size()-1).getMileage();
									Float xMileage =mileageLast-mileageFrist;
									Float kmMileage = xMileage/1000;
									if(achievementKm==null || achievementKm>kmMileage){
										achievementKm = kmMileage;
									}
								}
							}
							if(achievementKm==null){
								obdDriveRecods.setAchievement(0+"%");
							}else{
								BigDecimal achievementBig = new BigDecimal(Float.toString(achievementKm));
								BigDecimal achievementint = new BigDecimal(100);
								int achievement = 0;
								System.out.println(achievementBig);
								int i = achievementBig.compareTo(BigDecimal.ZERO);
								if(i!=0){
									achievement = (x1.divide(achievementBig, 2, BigDecimal.ROUND_HALF_UP)).multiply(achievementint).intValue();
								}
								String achievementStr = achievement+"%";
								obdDriveRecods.setAchievement(achievementStr);
							}
							//满足一个时间就把客户和经销商带上
							driveRecodsList = driveRecodsService.getByObdIdAndStartTime(carId, startDate);
							if(driveRecodsList.size()!=0){
								DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
								obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
								obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
								obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
								obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
								obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
							}else{
								driveRecodsList = driveRecodsService.getByObdIdAndEndTime(carId, endDate);
								if(driveRecodsList.size()!=0){
									DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
									obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
									obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
									obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
									obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
									obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
								}
							}
							
							
							obdGatherInfoService.save(obdDriveRecods);
						}else{
							
							DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
							obdDriveRecods.setAgencyId(carList.get(0).getAgency().getId());
							obdDriveRecods.setCarId(carId);
							obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
							obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
							
							obdDriveRecods.setCreateTime(new Date());
							obdDriveRecods.setDriveEndTime(endDate);
							obdDriveRecods.setDriveStartTime(startDate);
							obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
							obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
							obdDriveRecods.setMileage(x1);
							obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
							obdDriveRecods.setStatus(1);
							obdDriveRecods.setUpdateTime(new Date());
							BigDecimal mile = new BigDecimal(50);
							int compareTo = x1.compareTo(mile);
							if(compareTo==1){
								describe = describe +" "+ "试驾公里数大于50";
								obdDriveRecods.setStatus(2);
							}
							obdDriveRecods.setDescription(describe);
							//试驾公里数达成率
							Float achievementKm= null;
							String agencyId =carList.get(0).getAgency().getId();
							List<RouteInfoEntity> routeInfoList = routeInfoService.getByAgencyId(agencyId);
							for (RouteInfoEntity routeInfoEntity : routeInfoList) {
								List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(routeInfoEntity.getTermId(), routeInfoEntity.getStartTime(), routeInfoEntity.getEndTime());
								if (obdGathers != null && !obdGathers.isEmpty()) {
									Float mileageFrist = obdGathers.get(0).getMileage();
									Float mileageLast = obdGathers.get(obdGathers.size()-1).getMileage();
									Float xMileage =mileageLast-mileageFrist;
									Float kmMileage = xMileage/1000;
									if(achievementKm==null || achievementKm>kmMileage){
										achievementKm = kmMileage;
									}
								}
							}
							if(achievementKm==null){
								obdDriveRecods.setAchievement(0+"%");
							}else{
								BigDecimal achievementBig = new BigDecimal(Float.toString(achievementKm));
								BigDecimal achievementint = new BigDecimal(100);
								int achievement = 0;
								System.out.println(achievementBig);
								int i = achievementBig.compareTo(BigDecimal.ZERO);
								if(i!=0){
									achievement = (x1.divide(achievementBig, 2, BigDecimal.ROUND_HALF_UP)).multiply(achievementint).intValue();
								}
								String achievementStr = achievement+"%";
								obdDriveRecods.setAchievement(achievementStr);
							}
							obdGatherInfoService.save(obdDriveRecods);
						}
					  }
					}	
						//list 清零
						list.clear();
					}
				}else{
					//0 熄火 处理List中的数据并清理List中的数据
					list.add(obdGatherInfo);
					if(list.size()>1){
						ObdDriveRecodsEntity obdDriveRecods = new ObdDriveRecodsEntity();
						String describe = "原因：";
						String carId = carList.get(0).getId();
						
						ObdGatherInfoEntity fristObd = list.get(0);
						ObdGatherInfoEntity lastObd = list.get(list.size()-1);
						//获取行驶距离
						float m = lastObd.getMileage()-fristObd.getMileage();
						Float km = m/1000;
						BigDecimal x1 = new BigDecimal(Float.toString(km));
						//根据车辆ID 获取当日预约
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date startDate = sdf.parse(fristObd.getGnsstime());
						Date endDate = sdf.parse(lastObd.getGnsstime());
						List<DriveRecodsEntity> driveRecodsList = new ArrayList<DriveRecodsEntity>();
						long x = endDate.getTime()-startDate.getTime();
						if(x>60000l){
						if(x>600000l){
							driveRecodsList = driveRecodsService.getByObdIdAndTimeOnHalf(carId, startDate, endDate);
							if(driveRecodsList.size()!=0){
								driveRecodsList = driveRecodsService.getByObdIdAndTime(carId, startDate, endDate);
							}else{
								describe = describe +" "+ "无试驾流程";
							}
							
						}else{
							describe = describe +" "+ "试驾少于10分钟";
						}
						if(driveRecodsList.size()==0){
							//没有预约的驾驶 或者是时间不对
							describe = describe +" "+ "试驾流程不规范";
							obdDriveRecods.setAgencyId(carList.get(0).getAgency().getId());
							obdDriveRecods.setSalesmanId("/");
							obdDriveRecods.setCustomerId("/");
							obdDriveRecods.setCarId(carId);
							obdDriveRecods.setCreateTime(new Date());
							obdDriveRecods.setDriveEndTime(endDate);
							obdDriveRecods.setDriveStartTime(startDate);
							obdDriveRecods.setMileage(x1);
							obdDriveRecods.setStatus(2);
							obdDriveRecods.setUpdateTime(new Date());
							obdDriveRecods.setDescription(describe);
							//试驾公里数达成率
							Float achievementKm= null;
							String agencyId =carList.get(0).getAgency().getId();
							List<RouteInfoEntity> routeInfoList = routeInfoService.getByAgencyId(agencyId);
							for (RouteInfoEntity routeInfoEntity : routeInfoList) {
								List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(routeInfoEntity.getTermId(), routeInfoEntity.getStartTime(), routeInfoEntity.getEndTime());
								if (obdGathers != null && !obdGathers.isEmpty()) {
									Float mileageFrist = obdGathers.get(0).getMileage();
									Float mileageLast = obdGathers.get(obdGathers.size()-1).getMileage();
									Float xMileage =mileageLast-mileageFrist;
									Float kmMileage = xMileage/1000;
									if(achievementKm==null || achievementKm>kmMileage){
										achievementKm = kmMileage;
									}
								}
							}
							if(achievementKm==null){
								obdDriveRecods.setAchievement(0+"%");
							}else{
								BigDecimal achievementBig = new BigDecimal(Float.toString(achievementKm));
								BigDecimal achievementint = new BigDecimal(100);
								int achievement = 0;
								System.out.println(achievementBig);
								int i = achievementBig.compareTo(BigDecimal.ZERO);
								if(i!=0){
									achievement = (x1.divide(achievementBig, 2, BigDecimal.ROUND_HALF_UP)).multiply(achievementint).intValue();
								}
								String achievementStr = achievement+"%";
								obdDriveRecods.setAchievement(achievementStr);
							}
							//满足一个时间就把客户和经销商带上
							driveRecodsList = driveRecodsService.getByObdIdAndStartTime(carId, startDate);
							if(driveRecodsList.size()!=0){
								DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
								obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
								obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
								obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
								obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
								obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
							}else{
								driveRecodsList = driveRecodsService.getByObdIdAndEndTime(carId, endDate);
								if(driveRecodsList.size()!=0){
									DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
									obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
									obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
									obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
									obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
									obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
								}
							}
							
							obdGatherInfoService.save(obdDriveRecods);
						}else{
							DriveRecodsEntity driveRecodsEntity = driveRecodsList.get(0);
							
							obdDriveRecods.setAgencyId(carList.get(0).getAgency().getId());
							obdDriveRecods.setCarId(carId);
							obdDriveRecods.setContractPicPath(driveRecodsEntity.getTestDriveContractPicPath());
							obdDriveRecods.setCustomerId(driveRecodsEntity.getCustomer().getId());
							
							obdDriveRecods.setCreateTime(new Date());
							obdDriveRecods.setDriveEndTime(endDate);
							obdDriveRecods.setDriveStartTime(startDate);
							obdDriveRecods.setEndPicPath(driveRecodsEntity.getEndPicPath());
							obdDriveRecods.setFeedback(driveRecodsEntity.getFeedback());
							obdDriveRecods.setMileage(x1);
							obdDriveRecods.setSalesmanId(driveRecodsEntity.getSalesman().getId());
							obdDriveRecods.setStatus(1);
							obdDriveRecods.setUpdateTime(new Date());
							BigDecimal mile = new BigDecimal(50);
							int compareTo = x1.compareTo(mile);
							if(compareTo==1){
								describe = describe +" "+ "试驾公里数大于50";
								obdDriveRecods.setStatus(2);
							}
							obdDriveRecods.setDescription(describe);
							//试驾公里数达成率
							Float achievementKm= null;
							String agencyId =carList.get(0).getAgency().getId();
							List<RouteInfoEntity> routeInfoList = routeInfoService.getByAgencyId(agencyId);
							for (RouteInfoEntity routeInfoEntity : routeInfoList) {
								List<ObdGatherInfoEntity> obdGathers = obdGatherInfoService.getDatasByTimeQuantum(routeInfoEntity.getTermId(), routeInfoEntity.getStartTime(), routeInfoEntity.getEndTime());
								if (obdGathers != null && !obdGathers.isEmpty()) {
									Float mileageFrist = obdGathers.get(0).getMileage();
									Float mileageLast = obdGathers.get(obdGathers.size()-1).getMileage();
									Float xMileage =mileageLast-mileageFrist;
									Float kmMileage = xMileage/1000;
									if(achievementKm==null || achievementKm>kmMileage){
										achievementKm = kmMileage;
									}
								}
							}
							if(achievementKm==null){
								obdDriveRecods.setAchievement(0+"%");
							}else{
								BigDecimal achievementBig = new BigDecimal(Float.toString(achievementKm));
								BigDecimal achievementint = new BigDecimal(100);
								int achievement = 0;
								System.out.println(achievementBig);
								int i = achievementBig.compareTo(BigDecimal.ZERO);
								if(i!=0){
									achievement = (x1.divide(achievementBig, 2, BigDecimal.ROUND_HALF_UP)).multiply(achievementint).intValue();
								}
								String achievementStr = achievement+"%";
								obdDriveRecods.setAchievement(achievementStr);
							}
							obdGatherInfoService.save(obdDriveRecods);
						}
					  }
					}
					//list 清零
					list.clear();
					
				}
				
				}
			  }	
			}
		}
		
	}
}

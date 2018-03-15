package com.mtestdrive;

import java.util.HashMap;
import java.util.Map;

public class MaseratiConstants {
	
	private static Map<String, String> obd_drive_join_on;
	private static Map<String, String> drive_join_on;

	/**
	 * 根据身份编号获取性别
	 * 
	 * @param idCard 身份编号
	 * @return 性别(1-男，2-女，0-未知)
	 */
	public static int getGenderByIdCard(String idCard) {
		int gender = 0;
		String sCardNum = idCard.substring(16, 17);
		if (Integer.parseInt(sCardNum) % 2 != 0) {
			gender = 1;
		} else {
			gender = 2;
		}
		return gender;
	}

	/**
	 * 客户来源类型
	 *  1:saleforce推送而来;
	 *  2:4S店添加
	 */
	public static class CustomerSourceType {
		/** saleforce推送而来 */
		public static final int FROM_SALEFORCE = 1;
		
		/** 4S店添加手动添加 */
		public static final int ADD_BY_AGENCY = 2;
		/** The Constant descrMap. */
		static final Map<Integer, String> descrMap = new HashMap<Integer, String>();

		static {
			descrMap.put(1, "saleforce");
			descrMap.put(2, "手动添加");
		}

		public static String getDescr(int type) {
			if (descrMap.containsKey(type))
				return descrMap.get(type);
			else {
				return "未知来源";
			}
		}
	}
	
	
	
	/**
	 * 
	 * 试驾车状态
	 *  1:试驾中;
	 *  2:空闲中;
	 *  3:活动报备;
	 *  4:维修报备;
	 *  5:临时报备;
	 *  6:巡展报备;
	 */
	public static class CarStatus {
		/** 试驾中  */
		public static final int TEST_DRIVING = 1;
		
		/** 空闲中  */
		public static final int NO_USED = 2;
		
		/** 活动报备  */
		public static final int ACTIVITY_REPORT = 3;
		
		/** 维修报备  */
		public static final int MAINTAIN_REPORT = 4;
		
		/** 临时报备  */
		public static final int TEMPORARY_REPORT = 5;
		
		/** 巡展报备  */
		public static final int EXHIBITION_REPORT = 6;
		
		/** The Constant descrMap. */
		static final Map<Integer, String> serverMap = new HashMap<Integer, String>();
		
		/** The Constant descrMap. */
		static final Map<Integer, String> clientMap = new HashMap<Integer, String>();

		static {
			serverMap.put(1, "正常");
			serverMap.put(2, "正常");
			serverMap.put(3, "活动报备");
			serverMap.put(4, "维修报备");
			serverMap.put(5, "临时报备");
			serverMap.put(6, "巡展报备");
			
			clientMap.put(1, "试驾中");
			clientMap.put(2, "空闲");
			clientMap.put(3, "外出");
			clientMap.put(4, "外出");
			clientMap.put(5, "外出");
			clientMap.put(6, "外出");
		}

		public static String getServerDescr(int type) {
			if (serverMap.containsKey(type))
				return serverMap.get(type);
			else {
				return "未知";
			}
		}
		
		public static String getClientDescr(int type) {
			if (clientMap.containsKey(type))
				return clientMap.get(type);
			else {
				return "未知";
			}
		}
	}
	
	
	
	/**
	 * 
	 * 预约状态 0-预约，1-已确认，2-试驾中，3-完成，4-无效的，5-放弃
	 *  0:预约;
	 *  1:已确认;
	 *  2:已准备;
	 *  3:手续已办理;
	 *  4:试驾中;
	 *  5:完成;
	 *  6:无效的;
	 *  7:放弃;
	 *  8:已提交问卷
	 */
	public static class DriveRecodsStatus {
		/** 预约  */
		public static final int ASK = 0;
		
		/** 已确认  */
		public static final int ANSWER = 1;
		
		/** 已准备  */
		public static final int CONFIRMED = 2;
		
		/** 手续已办理  */
		public static final int FORMALITIES = 3;
		
		/** 试驾中  */
		public static final int UNDERWAY = 4;
		
		/** 完成  */
		public static final int COMPLETE = 5;
		
		/** 无效的  */
		public static final int DESERTED = 6;
		
		/** 放弃  */
		public static final int CANCEL = 7;
		
		/** 已提交问卷  */
		public static final int GROUP = 8;
		/** 已生成报告  */
		public static final int GENERATEDREPORT = 9;
		/** 预约  */
		private static final Integer NONEVENT[] = {ASK,ANSWER,CANCEL};
		
		/** 试驾  */
		private static final Integer DRIVERECODS[] = {FORMALITIES,UNDERWAY,COMPLETE,DESERTED,GROUP,GENERATEDREPORT};
		
		/** The Constant descrMap. */
		static final Map<String, Integer[]> statusesMap = new HashMap<String, Integer[]>();

		static {
			statusesMap.put("0", NONEVENT);
			statusesMap.put("", DRIVERECODS);
		}

		public static Integer[] statusDescr(String type) {
			if (statusesMap.containsKey(type))
				return statusesMap.get(type);
			else {
				return DRIVERECODS;
			}
		}
	}
	
	
	/**
	 * @ClassName:  AuthCodeStatus   
	 * @Description: 验证码状态
	 * @author: mengtaocui
	 * @date:   2017年3月27日 下午5:09:59   
	 *     
	 * @Copyright:
	 */
	public static class AuthCodeStatus{
		public static final int UNUSED = 0;
		public static final int USED = 1;
		public static final int UNLAWFUL = 2;
	}
	
	/**
	 */
	public static class SalesmanStatus{
		public static final int INVALID = 0;
		public static final int VALID = 1;
	}
	
	/**
	 */
	public static class RouteStatus{
		public static final int INVALID = 0;
		public static final int VALID = 1;
	}
	
	public static class ConstantStatus{
		public static final int INVALID = 0;//无效
		public static final int VALID = 1;//有效
	}

	/**
	 * 维度
	 */
	public static class WD{
		/** 销售顾问  */
		public static final String CONSULTANT ="consultant";
		/** 车型  */
		public static final String MOTORCYCLE = "motorcycle";
		/** 门店  */
		public static final String STORE ="store";
		/** 区域  */
		public static final String QUYU = "quyu";
	}
	
	/**
	 * 报备状态 0：未开始报备，1：已开始报备，2：报备已结束
	 */
	public static class ReportStatus{
		public static final int AWAIT = 0;//未开始报备
		public static final int UNDERWAY = 1;//已开始报备
		public static final int FINISHED = 2;//报备已结束
	}
	
	public static class SFInfo{
		public static final String USERNAME = "salesforce";
	}

	public static Map<String, String> getObdDriveJoinOn(){
		if (null == obd_drive_join_on) {
			obd_drive_join_on = new HashMap<String, String>();
			obd_drive_join_on.put("agency", " LEFT JOIN t_agency_info agency ON obdDrive.agency_id=agency.id");
			obd_drive_join_on.put("car", " LEFT JOIN t_car_info car ON obdDrive.car_id=car.id");
			obd_drive_join_on.put("salesman", " LEFT JOIN t_salesman_info salesman ON obdDrive.salesman_id=salesman.id");
			obd_drive_join_on.put("region", " LEFT JOIN t_agency_info agency ON obdDrive.agency_id=agency.id"
					+ " LEFT JOIN t_s_type region ON agency.dearler_group=region.typecode");
			obd_drive_join_on.put("carType", " LEFT JOIN t_car_info car ON obdDrive.car_id=car.id"
					+ " LEFT JOIN t_s_type carType ON car.type=carType.typecode");
		}
		return obd_drive_join_on;
	}

	public static Map<String, String> getDriveJoinOn(){
		if (null == drive_join_on) {
			drive_join_on = new HashMap<String, String>();
			drive_join_on.put("agency", " LEFT JOIN t_agency_info agency ON drive.agency_id=agency.id");
			drive_join_on.put("salesman", " LEFT JOIN t_salesman_info salesman ON drive.salesman_id=salesman.id");
			drive_join_on.put("region", " LEFT JOIN t_agency_info agency ON drive.agency_id=agency.id"
					+ " LEFT JOIN t_s_type region ON agency.dearler_group=region.typecode");
			drive_join_on.put("carType", " LEFT JOIN t_s_type carType ON drive.car_type=carType.typecode");
		}
		return drive_join_on;
	}

}

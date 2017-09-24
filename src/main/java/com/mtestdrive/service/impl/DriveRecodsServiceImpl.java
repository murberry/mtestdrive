package com.mtestdrive.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants;
import com.mtestdrive.entity.DriveRecodsEntity;
import com.mtestdrive.service.DriveRecodsServiceI;
import com.mtestdrive.vo.DriveRecodsVo;

@Service("driveRecodsService")
@Transactional
public class DriveRecodsServiceImpl extends CommonServiceImpl implements DriveRecodsServiceI {

	/**
	 * 
	 * Title: getAwitSendRecodsId
	 * Description: 得到等待发送调查问卷的试驾记录的ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getAwitSendRecodsId() {
		String sql = " select * from sent_questionnaire_customer_info_view ";
		
		SQLQuery query = getSession().createSQLQuery(sql.toString())
				.addScalar("recodesId",StandardBasicTypes.STRING)
				.addScalar("dealerName", StandardBasicTypes.STRING)
				.addScalar("customerMobile", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	@Override
	public DriveRecodsVo attachPic(String id, String picPath) {
		DriveRecodsVo driveRecodsVo = null;
		if (StringUtil.isNotEmpty(id)) {
			DriveRecodsEntity driveRecods = get(DriveRecodsEntity.class, id);
			if (driveRecods != null) {
				driveRecods.setStartPicPath(picPath);
				saveOrUpdate(driveRecods);
				driveRecodsVo = new DriveRecodsVo();
				try {
					MyBeanUtils.copyBean2Bean(driveRecodsVo, driveRecods);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return driveRecodsVo;
	}

	@Override
	public void complete(String id, int i) {
		DriveRecodsEntity driveRecods = get(DriveRecodsEntity.class, id);
		if (driveRecods != null && driveRecods.getStatus() == i) {
			driveRecods.setStatus(driveRecods.getStatus() + 1);
			saveOrUpdate(driveRecods);
		}
	}

	@Override
	public Map<String, Integer> effective(String groupBy, String month, String... param) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		StringBuilder sqlB = new StringBuilder("SELECT ").append(groupBy).append(",COUNT(drive.id) FROM t_drive_recods drive");
		Map<String, String> whereParam = new HashMap<String, String>();
		if (param != null) {
			if (0 < param.length && StringUtil.isNotEmpty(param[0])) {
				param[0] = new StringBuilder(" AND agency.id='").append(param[0]).append("'").toString();//经销商
				whereParam.put("agency", param[0]);
			}
			if (1 < param.length && StringUtil.isNotEmpty(param[1])) {
				param[1] = new StringBuilder(" AND agency.dearler_group='").append(param[1]).append("'").toString();//集团
				whereParam.put("agency", param[1]);
			}
		}
		//join开始
		String alias = groupBy.substring(0, groupBy.indexOf("."));
		if (!(whereParam.keySet().contains(alias) || "drive".equals(alias))) {
			sqlB.append(MaseratiConstants.getDriveJoinOn().get(alias));
		}
		for (String key : whereParam.keySet()) {
			sqlB.append(MaseratiConstants.getDriveJoinOn().get(key));
		}
		//join结束
		//where开始
		sqlB.append(" WHERE date_format(drive.order_start_time,'%Y-%c')='").append(month).append("' AND NOT drive.status>7");
		for (String value : whereParam.values()) {
			sqlB.append(value);
		}
		//where结束
		sqlB.append(" GROUP BY ").append(groupBy);
		List<Object[]> list = findListbySql(sqlB.toString());//带条件分子
		for (Object[] objects : list) {
			map.put(objects[0].toString(), Integer.parseInt(objects[1].toString()));
		}
		list = findListbySql(sqlB.toString().replace(" AND NOT drive.status>7", ""));//不带条件分母
		for (int i = 0; i < list.size(); i++) {
			if (null == list.get(i) || null == list.get(i)[0] || "0".equals(list.get(i)[1])) {
				continue;
			}
			if (null == map.get(list.get(i)[0].toString())) {
				map.put(list.get(i)[0].toString(), 0);
			} else {
				int value = 100*(map.get(list.get(i)[0].toString()))/Integer.parseInt(list.get(i)[1].toString());
				map.put(list.get(i)[0].toString(), value);
			}
		}
		return map;
	}

	
	public List<DriveRecodsEntity> getByObdIdAndTime(String carId, Date startDate, Date endDate) {
		long startTimeLong1 = startDate.getTime()-180000L;
		Date startTime1 = new Date(startTimeLong1);
		long startTimeLong2 = startDate.getTime()+180000L;
		Date startTime2 = new Date(startTimeLong2);
		long endTimeLong1 = endDate.getTime()-180000L;
		Date endTime1 = new Date(endTimeLong1);
		long endTimeLong2 = endDate.getTime()+180000L;
		Date endTime2= new Date(endTimeLong2);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from  DriveRecodsEntity this_");
		sql.append(" where this_.carId=? ");
		sql.append(" and this_.driveStartTime between ? and ? ");
		sql.append(" and this_.driveEndTime between ? and ? ");
		sql.append(" order by this_.driveStartTime asc ");
		/*SQLQuery query = getSession().createSQLQuery(sql.toString());*/
		
/*		query.setParameter("carId", carId);
		query.setParameter("startTime1", startTime1);
		query.setParameter("startTime2", startTime2);
		query.setParameter("endTime1", endTime1);
		query.setParameter("endTime2", endTime2);*/
		//query.setResultTransformer(Transformers.aliasToBean(DriveRecodsEntity.class));
		System.out.println("开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始");
		return commonDao.findHql(sql.toString(), carId, startTime1, startTime2, endTime1, endTime2);
		//List<DriveRecodsEntity> list = query.list();
		//System.out.println("结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束结束");
		//return list;
	}

	@Override
	public List<DriveRecodsEntity> getByObdIdAndTimeOnHalf(String carId, Date startDate, Date endDate) {
		long startTimeLong1 = startDate.getTime()-1800000L;
		Date startTime1 = new Date(startTimeLong1);
		long startTimeLong2 = startDate.getTime()+1800000L;
		Date startTime2 = new Date(startTimeLong2);
		long endTimeLong1 = endDate.getTime()-1800000L;
		Date endTime1 = new Date(endTimeLong1);
		long endTimeLong2 = endDate.getTime()+1800000L;
		Date endTime2= new Date(endTimeLong2);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from  DriveRecodsEntity this_");
		sql.append(" where this_.carId=? ");
		sql.append(" and this_.driveStartTime between ? and ? ");
		sql.append(" and this_.driveEndTime between ? and ? ");
		sql.append(" order by this_.driveStartTime asc ");
		System.out.println("开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始开始");
		return commonDao.findHql(sql.toString(), carId, startTime1, startTime2, endTime1, endTime2);
	}

	@Override
	public List<DriveRecodsEntity> getByObdIdAndStartTime(String carId, Date startDate) {
		long startTimeLong1 = startDate.getTime()-1800000L;
		Date startTime1 = new Date(startTimeLong1);
		long startTimeLong2 = startDate.getTime()+1800000L;
		Date startTime2 = new Date(startTimeLong2);
		
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from  DriveRecodsEntity this_");
		sql.append(" where this_.carId=? ");
		sql.append(" and this_.driveStartTime between ? and ? ");
		sql.append(" order by this_.driveStartTime asc ");
		return commonDao.findHql(sql.toString(), carId, startTime1, startTime2);
	}

	@Override
	public List<DriveRecodsEntity> getByObdIdAndEndTime(String carId, Date endDate) {
		long endTimeLong1 = endDate.getTime()-1800000L;
		Date endTime1 = new Date(endTimeLong1);
		long endTimeLong2 = endDate.getTime()+1800000L;
		Date endTime2= new Date(endTimeLong2);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" from  DriveRecodsEntity this_");
		sql.append(" where this_.carId=? ");
		sql.append(" and this_.driveEndTime between ? and ? ");
		sql.append(" order by this_.driveStartTime asc ");
		return commonDao.findHql(sql.toString(), carId, endTime1, endTime2);
	}
	
}
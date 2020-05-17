package com.mtestdrive.service.impl;


import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.p3.core.common.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.entity.ObdGatherInfoEntity;
import com.mtestdrive.service.ObdGatherInfoServiceI;

@Service("obdGatherInfoService")
@Transactional
public class ObdGatherInfoServiceImpl extends CommonServiceImpl implements ObdGatherInfoServiceI {

	/**
	 * @Title: getLastData   
	 * @Description: 得到指定OBD设备最后一次推送的数据
	 * @param: @param id
	 * @return      
	 * @return: ObdGatherInfoEntity      
	 * @throws
	 */
	@Override
	public ObdGatherInfoEntity getLastData(String termid) {
		String hql = "from ObdGatherInfoEntity where termid=:termid order by gnssTime desc";
		Query query = getSession().createQuery(hql);
		query.setParameter("termid", termid);
		query.setMaxResults(1);
		List<ObdGatherInfoEntity> list = query.list();
		if(list != null && !list.isEmpty())
			return list.get(0);
		return new ObdGatherInfoEntity();
	}

	/**
	 * @Title: getDatasByTimeQuantum   
	 * @Description: 查询指定设备  指定时间段内的数据
	 * @param: @param id
	 * @return      
	 * @return: ObdGatherInfoEntity      
	 * @throws
	 */
	@Override
	public List<ObdGatherInfoEntity> getDatasByTimeQuantum(String termid, Date startTime, Date endTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select this_.id , this_.alt ,this_.CREATE_TIME as createTime ,this_.gnsstime, ");
		sql.append(" this_.head , this_.lat , this_.lon ,this_.mileage ,this_.obdSpd , ");
		sql.append(" this_.spd , this_.termid  from  t_obd_gather_info this_  ");
		sql.append(" where this_.TERMID=:termId ");
		sql.append(" and this_.gnssTime between :startTime and :endTime ");
//		sql.append(" group by this_.lat , this_.lon"); 	去除无意义的聚合 20200420
		sql.append(" order by this_.gnssTime asc ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("termId", termid);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		query.setResultTransformer(Transformers.aliasToBean(ObdGatherInfoEntity.class));
		
		return query.list();
	}

	/**
	 * 根据线路ID获取OBD数据
	 * @param routeId
	 * @return
	 */
	@Override
	public List<ObdGatherInfoEntity> getDatasByRoute(String routeId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" Select this_.id , this_.alt ,this_.CREATE_TIME as createTime ,this_.gnsstime, ")
		   .append(" this_.head , this_.lat , this_.lon ,this_.mileage ,this_.obdSpd , ")
		   .append(" this_.spd , this_.termid  from  t_obd_route_info this_  ")
		   .append(" where this_.route_id=:routeId ")
		   .append(" order by this_.gnssTime asc ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.setParameter("routeId", routeId);
		query.setResultTransformer(Transformers.aliasToBean(ObdGatherInfoEntity.class));

		return query.list();
	}
	
	/**
	 * @Title: getDatasByTimeQuantum   
	 * @Description: 查询当天（一辆车）所有数据
	 * @param: @param id
	 * @return      
	 * @return: ObdGatherInfoEntity      
	 * @throws
	 */
	public List<ObdGatherInfoEntity> getObdByTermidAndGnsstime(String termid, String gnssTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select this_.id , this_.alt ,this_.CREATE_TIME as createTime ,this_.gnsstime, ");
		sql.append(" this_.head , this_.lat , this_.lon ,this_.mileage ,this_.obdSpd , ");
		sql.append(" this_.spd , this_.termid ,this_.accOn  from  t_obd_gather_info this_  ");
		sql.append(" where this_.TERMID=:termId ");
		sql.append(" and this_.gnssTime like concat('%',:gnssTime,'%')");
		sql.append(" order by this_.gnssTime asc ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("termId", termid);
		query.setParameter("gnssTime", gnssTime);
		query.setResultTransformer(Transformers.aliasToBean(ObdGatherInfoEntity.class));
		
		return query.list();
	}

	public List<ObdGatherInfoEntity> getObdIdByToday(String gnssTime) {
		StringBuilder sql = new StringBuilder();	
		sql.append("  select distinct this_.termid from  t_obd_gather_info this_  ");
		sql.append(" where this_.gnssTime like concat('%',:gnssTime,'%')");
		sql.append(" order by this_.gnssTime asc ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter("gnssTime", gnssTime);
		query.setResultTransformer(Transformers.aliasToBean(ObdGatherInfoEntity.class));
		
		return query.list();
	}

	
}
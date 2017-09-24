package com.mtestdrive.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.TimestampType;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.pojo.base.TSType;
import org.jeecgframework.web.system.pojo.base.TSTypegroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants.ConstantStatus;
import com.mtestdrive.dto.CarInfoDto;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.service.CarInfoServiceI;

@Service("carInfoService")
@Transactional
public class CarInfoServiceImpl extends CommonServiceImpl implements CarInfoServiceI {

	@Override
	public List<CarInfoEntity> getCarByType(String agencyId, String type) {
		String hql = "select"
				+ " new CarInfoEntity(id,code,vin,name,plateNo,picPath,saleYear,driveTotal,status,obdId)"
				+ " from CarInfoEntity car"
				+ " where car.agency.id=? and car.type=? and car.status<>0";
		return findHql(hql, agencyId, type);
	}

	@Override
	public List<Object[]> getCarTypes() {
		String query = "SELECT TYPECODE typecode,TYPENAME typename"
				+ " FROM t_s_type"
				+ " WHERE TYPEGROUPID ="
				+ " (SELECT ID FROM t_s_typegroup WHERE TYPEGROUPCODE = 'carType')";
		return findListbySql(query);
	}

	private void fallow(CarInfoDto carInfo, CriteriaQuery cq) {
		List<CarInfoEntity> carInfos = null;
		DetachedCriteria carDc = null;
		if (carInfo.getAgency() == null || StringUtil.isEmpty(carInfo.getAgency().getId())) {
			carDc = DetachedCriteria.forClass(CarInfoEntity.class);
			carDc.add(Restrictions.ne("status", ConstantStatus.INVALID));
			if(StringUtil.isNotEmpty(carInfo.getType()))
				carDc.add(Restrictions.eq("type", carInfo.getType()));
			carInfos = findByDetached(carDc);

		} else {
			if(StringUtil.isNotEmpty(carInfo.getType()))
				carInfos = getCarByType(carInfo.getAgency().getId(), carInfo.getType());
			else{
				carDc = DetachedCriteria.forClass(CarInfoEntity.class);
				carDc.add(Restrictions.ne("status", ConstantStatus.INVALID));
				carDc.add(Restrictions.eq("agency.id", carInfo.getAgency().getId()));
				carInfos = findByDetached(carDc);
			}
		}
		Map<String, Date[]> carIds = new HashMap<String, Date[]>();
		if (!ListUtils.isNullOrEmpty(carInfos)) {
			for (int i = 0; i < carInfos.size(); i++) {
				carInfo.setId(carInfos.get(i).getId());
				long[] orderDates = animate(carInfo);
				if (null == orderDates) {
					try {
						Date month[] = {DateUtils.short_date_sdf.parse(carInfo.getMonth())};
						carIds.put(carInfos.get(i).getId(), month);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					for (int j = 1; j < orderDates.length; j++) {
						if (orderDates[j] - orderDates[j-1] > 14 * 24 * 3600 * 1000) {
							Date twoWeeks[] = {new Date(orderDates[j-1]), new Date(orderDates[j])};
							carIds.put(carInfos.get(i).getId(), twoWeeks);
							break;
						}
					}
				}
			}
		}
		if (!carIds.isEmpty()) {
			cq.add(Restrictions.in("id", carIds.keySet()));
			getDataGridReturn(cq, true);
			for (Iterator iterator = cq.getDataGrid().getResults().iterator(); iterator.hasNext();) {
				CarInfoEntity car = (CarInfoEntity) iterator.next();
				car.setFallowTime(carIds.get(car.getId()));
			}
		}
	}

	private long[] animate(CarInfoDto carInfo) {
		String sql = "SELECT * FROM t_drive_recods"
				+ " WHERE CAR_ID=:carId"
				+ " AND STATUS=8"
				+ " AND DATE_FORMAT(DRIVE_START_TIME,'%Y-%c')=:month"
				+ " ORDER BY DRIVE_START_TIME";
		
		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("DRIVE_START_TIME", TimestampType.INSTANCE);
		query.setParameter("carId", carInfo.getId());
		query.setParameter("month", carInfo.getMonth());
		
		List<Timestamp> results = query.list();
		if (ListUtils.isNullOrEmpty(results)) {
			return null;
		} else {
			long[] orderDates = new long[results.size()];
			for (int i = 0; i < results.size(); i++) {
				orderDates[i] = results.get(i).getTime();
			}
			return orderDates;
		}
	}

	@Override
	public void datagrid(CarInfoDto carInfo, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(CarInfoEntity.class, dataGrid);
		if (StringUtil.isNotEmpty(carInfo.getMonth())) {
			fallow(carInfo, cq);
		} else {
			cq.createAlias("agency", "agency");
			if (StringUtil.isNotEmpty(carInfo.getAgencyId())) {
				cq.add(Restrictions.eq("agency.id", carInfo.getAgencyId()));
			}
			if (StringUtil.isNotEmpty(carInfo.getRegionId())) {
				cq.add(Restrictions.eq("agency.regionId", carInfo.getRegionId()));
			}
			cq.add(Restrictions.ne("status", ConstantStatus.INVALID));
			cq.setResultTransformer(CarInfoEntity.class);
			getDataGridReturn(cq, true);
		}
	}

	@Override
	public List<TSType> getAllCarTypes() {
		TSTypegroup group = commonDao.findUniqueByProperty(TSTypegroup.class, "typegroupcode", "carType");
		return group.getTSTypes();
	}

	@Override
	public void subTimes(String carId) {
		String hql = "update CarInfoEntity car set car.driveTotal=(car.driveTotal-1) where car.id=:car_id";
		Query query = getSession().createQuery(hql);
		query.setParameter("car_id", carId);
		query.executeUpdate();
	}

	@Override
	public List<CarInfoEntity> getByCarObdId(String termId) {
		String hql = " from CarInfoEntity car"
				+ " where car.obdId=? and car.status<>0";
		return findHql(hql, termId);
	}

	
}
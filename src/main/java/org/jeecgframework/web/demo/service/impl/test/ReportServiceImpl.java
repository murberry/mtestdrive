package org.jeecgframework.web.demo.service.impl.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants.CarStatus;
import com.mtestdrive.MaseratiConstants.ReportStatus;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.ReportRecordsEntity;

@Service("reportService")
@Transactional
public class ReportServiceImpl {
	@Autowired
	private SystemService systemService;

	private static final Logger logger = Logger.getLogger(ReportServiceImpl.class);

	/**
	 * @Title: work   
	 * @Description: 定时任务调度的方法
	 * @param:       
	 * @return: void      
	 * @throws
	 */
	public void work() {
		logger.info("车辆报备定时任务已开始执行");
		
		List<ReportRecordsEntity> recods = null;
		List<CarInfoEntity> carList = new ArrayList<CarInfoEntity>();;
		CarInfoEntity car = null;
		
		//开始报备
		recods = getRecords(ReportStatus.AWAIT);
		ReportRecordsEntity report = null;
		if (!ListUtils.isNullOrEmpty(recods)) {
			
			for (int i = 0; i < recods.size(); i++) {
				report = recods.get(i);
				report.setStatus(ReportStatus.UNDERWAY);
				car = getChangeCarStatus(report.getCarId(), Integer.parseInt(report.getType()));
				if(car != null)
					carList.add(car);
			}
			systemService.batchUpdate(recods);
			systemService.batchUpdate(carList);
			carList.clear();
		}

		//要设为报备结束的记录
		List<ReportRecordsEntity> finishedRecods = new ArrayList<ReportRecordsEntity>();
		
		//报备结束
		recods = getRecords(ReportStatus.UNDERWAY);
		if (!ListUtils.isNullOrEmpty(recods)) {
			for (int i = 0; i < recods.size(); i++) {
				report = recods.get(i);
				long endTime = report.getEndTime().getTime();
				
				long nowTime = DateUtils.gettimestamp().getTime();
				
				//报备结束时间在当前时间前后2分钟的，直接设为报备结束
				if(Math.abs((nowTime - endTime)/1000/60) <= 2){
					report.setStatus(ReportStatus.FINISHED);
					finishedRecods.add(report);
					car = getChangeCarStatus(report.getCarId(), CarStatus.NO_USED);
					if(car != null)
						carList.add(car);
				}
			}
			if(!ListUtils.isNullOrEmpty(finishedRecods)){
				systemService.batchUpdate(finishedRecods);
				systemService.batchUpdate(carList);
			}
		}
		logger.info("车辆报备定时任务执行完毕");
	}

	/**
	 * @Title: getRecords   
	 * @Description: 根据不同状态获取把报备记录   
	 * @param: @param status
	 * @param: @return      
	 * @return: List<ReportRecordsEntity>      
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<ReportRecordsEntity> getRecords(Integer status) {
		String hql = " from ReportRecordsEntity de where status=:status and :nowTime between de.startTime and de.endTime ";
		Query query = systemService.getSession().createQuery(hql);
		query.setParameter("status", status);
		query.setParameter("nowTime", DateUtils.getTimestamp());
		return query.list();
	}
	
	/**
	 * @Title: getChangeCarStatus   
	 * @Description: 返回改变过状态之后的车辆实体  
	 * @param: @param id
	 * @param: @param status
	 * @param: @return      
	 * @return: CarInfoEntity      
	 * @throws
	 */
	public CarInfoEntity getChangeCarStatus(String id, int status){
		if(StringUtil.isNotEmpty(id)){
			CarInfoEntity car = systemService.get(CarInfoEntity.class, id);
			if(car != null){
				car.setStatus(status);
				return car;
			}
		}
		return null;
	}
	
}
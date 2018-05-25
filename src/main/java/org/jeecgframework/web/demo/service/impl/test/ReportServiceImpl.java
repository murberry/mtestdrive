package org.jeecgframework.web.demo.service.impl.test;

import com.mtestdrive.MaseratiConstants.CarStatus;
import com.mtestdrive.MaseratiConstants.ReportStatus;
import com.mtestdrive.entity.CarInfoEntity;
import com.mtestdrive.entity.ReportRecordsEntity;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.util.ListUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("reportService")
@Transactional
public class ReportServiceImpl {

	@Autowired
	private SystemService systemService;

	private static final Logger logger = Logger.getLogger(ReportServiceImpl.class);

	/**
	 * 定时任务调度的方法
	 */
	public void work() {
		logger.debug("车辆报备定时任务已开始执行");

		CriteriaQuery cq = new CriteriaQuery(ReportRecordsEntity.class);
		cq.in("status", new Integer[]{ReportStatus.AWAIT, ReportStatus.UNDERWAY});//取“未开始报备”和“已开始报备”（报备期间）的记录
        cq.add();
        List<ReportRecordsEntity> repRecordList = systemService.getListByCriteriaQuery(cq, false);

		List<CarInfoEntity> carList = new ArrayList<>();
		Date now = new Date();
		
		if (!ListUtils.isNullOrEmpty(repRecordList)) {
			
			for (ReportRecordsEntity repRecord : repRecordList) {

				//如果车辆报备记录处于“未报备”状态，但当前车辆处于报备期间, 则
				// 1. 将报备记录设为“报备中”
				// 2. 若车辆没有在“试驾中”，将车辆状态设为报备状态：3:活动报备;4:维修报备;5:临时报备;6:巡展报备;
				// （车辆完整状态：1:试驾中;2:空闲中;3:活动报备;4:维修报备;5:临时报备;6:巡展报备;）
				if ( repRecord.getStatus().equals(ReportStatus.AWAIT)
						&& now.after(repRecord.getStartTime()) && now.before(repRecord.getEndTime())) {
					repRecord.setStatus(ReportStatus.UNDERWAY);//报备中
					CarInfoEntity car = systemService.get(CarInfoEntity.class, repRecord.getCarId());
					if (car != null && !car.getStatus().equals(CarStatus.TEST_DRIVING)) {
                        logger.info("车辆自动进入报备期：carId="+car.getId()+" carStatus="+car.getStatus()+" to "+repRecord.getType());
						car.setStatus(Integer.parseInt(repRecord.getType())); //报备原因对应车辆状态中的3~6
						carList.add(car);
					}
				}

				//如果车辆报备记录处于“报备中”状态，且当前车辆不处于报备期间, 则
				// 1. 将报备记录设为“报备结束”
				// 2. 若车辆没有在“试驾中”，将车辆状态设为“空闲中”
				// （车辆完整状态：1:试驾中;2:空闲中;3:活动报备;4:维修报备;5:临时报备;6:巡展报备;）
				if ( repRecord.getStatus().equals(ReportStatus.UNDERWAY)
						&& (now.before(repRecord.getStartTime()) || now.after(repRecord.getEndTime()))) {
					repRecord.setStatus(ReportStatus.FINISHED);
					CarInfoEntity car = systemService.get(CarInfoEntity.class, repRecord.getCarId());
					if (car != null && !car.getStatus().equals(CarStatus.TEST_DRIVING)) {
                        logger.info("车辆自动结束报备期：carId="+car.getId()+" carStatus="+car.getStatus()+" to "+repRecord.getType());
                        car.setStatus(CarStatus.NO_USED);
						carList.add(car);
					}
				}


			}
			systemService.batchUpdate(repRecordList);
			systemService.batchUpdate(carList);
			carList.clear();
		}

		logger.debug("车辆报备定时任务执行完毕");
	}

}
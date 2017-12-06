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

import com.mtestdrive.service.impl.DriveRecodsServiceImpl;
import org.apache.log4j.Logger;
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

	private static final Logger logger = Logger.getLogger(TestDriveServiceImpl.class);

	@Autowired
	private ObdGatherInfoServiceI  obdGatherInfoService;


	public void work() throws ParseException{
		logger.info("开始执行全天有效试驾统计任务");
		//获取昨天
		Date date=new Date();//取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
		date=calendar.getTime(); //这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		obdGatherInfoService.executeProcedure("{call proc_match_testdrive(?,?)}", 30, dateString);
		logger.info("结束执行全天有效试驾统计任务");
	}


}

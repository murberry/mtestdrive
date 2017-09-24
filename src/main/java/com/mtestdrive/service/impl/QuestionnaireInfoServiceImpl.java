package com.mtestdrive.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mtestdrive.MaseratiConstants;
import com.mtestdrive.service.QuestionnaireInfoServiceI;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;

@Service("questionnaireInfoService")
@Transactional
public class QuestionnaireInfoServiceImpl extends CommonServiceImpl implements QuestionnaireInfoServiceI {

	@Override
	public Map<String, Integer> satisfied(String groupBy, String month, String... params) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		StringBuilder sqlB = new StringBuilder("SELECT ").append(groupBy).append(",COUNT(questionnaire.id) FROM t_questionnaire_info questionnaire"
				+ " JOIN t_questionnaire_question question ON question.questionnaireId=questionnaire.id"
				+ " JOIN t_drive_recods drive ON questionnaire.drive_id=drive.id");
		Map<String, String> whereParam = new HashMap<String, String>();
		if (params != null) {
			if (0 < params.length && StringUtil.isNotEmpty(params[0])) {
				params[0] = new StringBuilder(" AND agency.id='").append(params[0]).append("'").toString();//经销商
				whereParam.put("agency", params[0]);
			}
			if (1 < params.length && StringUtil.isNotEmpty(params[1])) {
				params[1] = new StringBuilder(" AND agency.dearler_group='").append(params[1]).append("'").toString();//集团
				whereParam.put("agency", params[1]);
			}
		}
		//join开始
		String alias = groupBy.substring(0, groupBy.indexOf("."));
		if (!(whereParam.keySet().contains(alias) || "drive".equals(alias) || "questionnaire".equals(alias) || "question".equals(alias))) {
			sqlB.append(MaseratiConstants.getDriveJoinOn().get(alias));
		}
		for (String key : whereParam.keySet()) {
			sqlB.append(MaseratiConstants.getDriveJoinOn().get(key));
		}
		//join结束
		//where开始
		sqlB.append(" WHERE questionnaire.commit_time like '").append(month).append("%' AND question.question='试驾时您对车辆的整体表现是否满意？' AND question.result='满意'");
		for (String value : whereParam.values()) {
			sqlB.append(value);
		}
		//where结束
		sqlB.append(" GROUP BY ").append(groupBy);
		List<Object[]> list = findListbySql(sqlB.toString());//带条件分子
		for (Object[] objects : list) {
			if (objects[0] != null)
				map.put(objects[0].toString(), Integer.parseInt(objects[1].toString()));
		}
		list = findListbySql(sqlB.toString().replace(" AND question.result='满意'", ""));//不带条件分母
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
	
}
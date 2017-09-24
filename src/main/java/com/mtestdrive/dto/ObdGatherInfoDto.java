package com.mtestdrive.dto;


/**
 * @ClassName: ObdGatherInfoDto
 * @Description:OBD设备推送数据DTO
 * @author: mengtaocui
 * @date: 2017年4月1日 下午2:58:27
 * 
 * @Copyright:
 */
public class ObdGatherInfoDto {

	/** obd编码 */
	private java.lang.String termId;
	private ObdGatherDataDto gnssData;

	public java.lang.String getTermId() {
		return termId;
	}

	public void setTermId(java.lang.String termId) {
		this.termId = termId;
	}

	public ObdGatherDataDto getGnssData() {
		return gnssData;
	}

	public void setGnssData(ObdGatherDataDto gnssData) {
		this.gnssData = gnssData;
	}

}

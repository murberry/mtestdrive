package com.mtestdrive.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerInfoVo {
	private String id;
	private String agencyId;
	private String name;
	private Integer source;
	private Integer gender;
	private Date birthday;
	private String idCard;
	private String mobile;
	private String drivingLicense;
	private String drivingLicensePicPath;
	private String createBy;
	private String remark;
	private Integer quarry;
	private Integer quarryDetail;
	private List<DriveRecodsVo> driveRecodses = new ArrayList<DriveRecodsVo>();

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgencyId() {
		return this.agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdCard() {
		return this.idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public String getDrivingLicensePicPath() {
		return this.drivingLicensePicPath;
	}

	public void setDrivingLicensePicPath(String drivingLicensePicPath) {
		this.drivingLicensePicPath = drivingLicensePicPath;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public List<DriveRecodsVo> getDriveRecodses() {
		return driveRecodses;
	}

	public void setDriveRecodses(List<DriveRecodsVo> driveRecodses) {
		this.driveRecodses = driveRecodses;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getQuarry() {
		return quarry;
	}

	public void setQuarry(Integer quarry) {
		this.quarry = quarry;
	}

	public Integer getQuarryDetail() {
		return quarryDetail;
	}

	public void setQuarryDetail(Integer quarryDetail) {this.quarryDetail = quarryDetail;}
}

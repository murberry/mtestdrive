package com.mtestdrive.dto;

import java.util.HashSet;
import java.util.Set;

import com.mtestdrive.entity.QuestionnaireQuestionEntity;

public class DriveRecodsSfDto {
	private String id;
	private String sfId;
	private String birthday;
	private String vin;
	private String code;
	private String name;
	private String address;
	private String city;
	private String provinces;
	
	private String driveDate;
	private String mobile;
	private String gender;
	private String mileage;

	private String salesmanName;
    private String endPicPath;
    private String drivingLicensePicPath;

	private String quarry;
	private String quarryDetail;

	
	private Set<QuestionnaireQuestionEntity> questionInfos = new HashSet<QuestionnaireQuestionEntity>();
	public Set<QuestionnaireQuestionEntity> getQuestionInfos() {
		return questionInfos;
	}
	public void setQuestionInfos(Set<QuestionnaireQuestionEntity> questionInfos) {
		this.questionInfos = questionInfos;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvinces() {
		return provinces;
	}
	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}
	public String getDriveDate() {
		return driveDate;
	}
	public void setDriveDate(String driveDate) {
		this.driveDate = driveDate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getSfId() {
		return sfId;
	}
	public void setSfId(String sfId) {
		this.sfId = sfId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSalesmanName() { return salesmanName;	}
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	public String getEndPicPath() {
		return endPicPath;
	}
	public void setEndPicPath(String endPicPath) {this.endPicPath = endPicPath;	}
    public String getDrivingLicensePicPath() {
        return drivingLicensePicPath;
    }
    public void setDrivingLicensePicPath(String drivingLicensePicPath) {this.drivingLicensePicPath = drivingLicensePicPath;	}
	public String getQuarry() {	return quarry; }
	public void setQuarry(String quarry) {this.quarry = quarry;}
	public String getQuarryDetail() {	return quarryDetail; }
	public void setQuarryDetail(String quarryDetail) {this.quarryDetail = quarryDetail;}
}

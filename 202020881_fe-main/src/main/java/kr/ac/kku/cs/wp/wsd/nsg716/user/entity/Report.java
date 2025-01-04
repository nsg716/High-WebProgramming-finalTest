/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
/**
 * Report
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Entity
@Table(name = "report")
public class Report {
    @Id
    @Column(name = "report_id", length = 200, nullable = false)
    private String reportId;


    @Column(name = "model_id", length = 200, nullable = false, columnDefinition= "ON UPDATE CASCADE ON DELETE CASCADE")
    private String model;


    @Column(name = "admin_id", length = 200, nullable = false, columnDefinition= "ON UPDATE CASCADE ON DELETE CASCADE")
    private String admin;

    @Column(name = "user_id", length = 200, nullable = false, columnDefinition= "ON UPDATE CASCADE ON DELETE CASCADE")
    private String user;

    @Column(name = "report_name", length = 200, nullable = false)
    private String reportName;

    @Column(name = "usage_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime usageDate;

    @Column(name = "model_name", length = 200, nullable = false)
    private String modelName;

    @Column(name = "accuracy", nullable = false)
    private BigDecimal  accuracy;

    @Column(name = "total_processing_amount", nullable = false)
    private Integer totalProcessingAmount;

    @Column(name = "average_response_time",nullable = false)
    private BigDecimal averageResponseTime;

    @Column(name = "create_date", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Column(name = "update_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    // Getters, Setters, Constructors, equals(), and hashCode()
    public Report() {}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public LocalDateTime getUsageDate() {
		return usageDate;
	}

	public void setUsageDate(LocalDateTime usageDate) {
		this.usageDate = usageDate;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public BigDecimal getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(BigDecimal accuracy) {
		this.accuracy = accuracy;
	}

	public Integer getTotalProcessingAmount() {
		return totalProcessingAmount;
	}

	public void setTotalProcessingAmount(Integer totalProcessingAmount) {
		this.totalProcessingAmount = totalProcessingAmount;
	}

	public BigDecimal getAverageResponseTime() {
		return averageResponseTime;
	}

	public void setAverageResponseTime(BigDecimal averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accuracy, admin, averageResponseTime, createDate, model, modelName, reportId, reportName,
				totalProcessingAmount, updateDate, usageDate, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Report other = (Report) obj;
		return Objects.equals(accuracy, other.accuracy) && Objects.equals(admin, other.admin)
				&& Objects.equals(averageResponseTime, other.averageResponseTime)
				&& Objects.equals(createDate, other.createDate) && Objects.equals(model, other.model)
				&& Objects.equals(modelName, other.modelName) && Objects.equals(reportId, other.reportId)
				&& Objects.equals(reportName, other.reportName)
				&& Objects.equals(totalProcessingAmount, other.totalProcessingAmount)
				&& Objects.equals(updateDate, other.updateDate) && Objects.equals(usageDate, other.usageDate)
				&& Objects.equals(user, other.user);
	}

    // Add other methods as necessary
}

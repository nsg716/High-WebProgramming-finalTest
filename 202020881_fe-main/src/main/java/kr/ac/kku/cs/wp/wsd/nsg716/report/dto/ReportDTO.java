/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.report.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
/**
 * ReportDTO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public class ReportDTO {

    @NotBlank(message = "리포트 ID는 필수 입력 항목입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "리포트 ID는 영어와 숫자만 입력 가능합니다.")
    private String reportId;

    @NotBlank(message = "모델 ID는 필수 입력 항목입니다.")
    private String model;

    @NotBlank(message = "관리자 ID는 필수 입력 항목입니다.")
    private String admin;

    @NotBlank(message = "사용자 ID는 필수 입력 항목입니다.")
    private String user;

    @NotBlank(message = "리포트 이름은 필수 입력 항목입니다.")
    private String reportName;

    @NotNull(message = "사용 날짜는 필수 입력 항목입니다.")
    private LocalDateTime usageDate;

    @NotBlank(message = "모델 이름은 필수 입력 항목입니다.")
    private String modelName;

    @NotNull(message = "정확도는 필수 입력 항목입니다.")
    @Positive(message = "정확도는 0보다 커야 합니다.")
    private BigDecimal accuracy;

    @NotNull(message = "총 처리량은 필수 입력 항목입니다.")
    @Positive(message = "총 처리량은 0보다 커야 합니다.")
    private Integer totalProcessingAmount;

    @NotNull(message = "평균 응답 시간은 필수 입력 항목입니다.")
    @Positive(message = "평균 응답 시간은 0보다 커야 합니다.")
    private BigDecimal averageResponseTime;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // Getters and Setters
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
}

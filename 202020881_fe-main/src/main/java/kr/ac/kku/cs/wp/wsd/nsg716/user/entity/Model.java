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

import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
/**
 * Model
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
@Entity
@Table(name = "model")
public class Model {
    
	@Id
    @Column(name = "model_id", length = 200, nullable = false)
    private String modelId;

    @Column(name = "admin_id", length = 200, nullable = false, columnDefinition=" ON UPDATE CASCADE ON DELETE CASCADE")
    private String adminId;

    @Column(name = "model_name", length = 200, nullable = false)
    private String modelName;

    @Column(name = "version", length = 200, nullable = false)
    private String version;

    @Column(name = "model_type", length = 200, nullable = false)
    private String modelType;

    @Column(name = "framework", length = 200, nullable = false)
    private String framework;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "create_date", updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @Column(name = "update_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;

    // Getters, Setters, Constructors, equals(), and hashCode()
    public Model() {}

    public Model(String modelId,String adminId, String modelName, String version, String modelType, String framework) {
    	this.modelId = modelId;
    	this.adminId = adminId;
    	this.modelName = modelName;
    	this.version = version;
    	this.modelType = modelType;
    	this.framework = framework;
    	
    }
    
    
	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getFramework() {
		return framework;
	}

	public void setFramework(String framework) {
		this.framework = framework;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		return Objects.hash(adminId, createDate, description, framework, modelId, modelType, modelType, updateDate,
				version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		return Objects.equals(adminId, other.adminId) && Objects.equals(createDate, other.createDate)
				&& Objects.equals(description, other.description) && Objects.equals(framework, other.framework)
				&& Objects.equals(modelId, other.modelId) && Objects.equals(modelName, other.modelName)
				&& Objects.equals(modelType, other.modelType) && Objects.equals(updateDate, other.updateDate)
				&& Objects.equals(version, other.version);
	}

    // Add other methods as necessary
}

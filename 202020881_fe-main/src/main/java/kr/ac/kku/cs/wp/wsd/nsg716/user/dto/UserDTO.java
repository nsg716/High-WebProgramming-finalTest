/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * UserDTO
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public class UserDTO {
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "영어와 숫자만 사용 가능합니다.")
    private String id;

	@Pattern(regexp = "^[\\p{L}\\p{M}\\p{N}]*$", message = "특수문자를 사용할 수 없습니다.")
    private String name;

    @NotBlank
    @Email
    private String email;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~`!@#$%^&*()_\\-+=\\[\\]{}|;:'\",.<>/?]).{8,}$",
        message = "비밀번호는 특수문자, 영문자, 숫자를 포함하여 8자리 이상이어야 합니다."
    )
    private String password;


    private String status;

    private String photo;
    
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
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
	public String getHumanReadableStatus() {
        return "active".equals(status) ? "재직중" : "퇴직";
    }
    
    
}

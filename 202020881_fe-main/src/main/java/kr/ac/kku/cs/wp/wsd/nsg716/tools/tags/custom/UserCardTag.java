/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.tools.tags.custom;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import jakarta.servlet.jsp.PageContext;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;

/**
 * UserCardTag
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 03.
 * @version 1.0
 *
 */
public class UserCardTag extends SimpleTagSupport {
	
	private String name = null;
	private String email = null;
	private String id = null;
	private String Status = null;
	
	
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}


	private List<User> users;

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public void doTag() throws JspException, IOException {
        if (users == null || users.isEmpty()) {
            getJspContext().getOut().write("<div>사용자가 없습니다.</div>");
            return;
        }

        StringBuilder output = new StringBuilder();

        // 총 사용자 수 출력
        output.append("<div id=\"user-count\" style=\"margin-bottom:20px;\">");
        output.append("총 사용자 수: <strong>").append(users.size()).append("</strong>명</div>");

        // 사용자 카드 출력
        output.append("<div class=\"user-card-container\" id=\"user-list\">");
        for (User user : users) {
            String photo = (user.getPhoto() == null || user.getPhoto().isEmpty()) 
                            ? "https://via.placeholder.com/150" 
                            : user.getPhoto();
            String badgeColor = "재직중".equals(user.getStatus()) ? "#28a745" : "#dc3545";

            output.append("<div class=\"user-card\" data-name=\"")
                  .append(user.getName()).append("\" data-email=\"").append(user.getEmail())
                  .append("\" data-status=\"").append(user.getStatus())
                  .append("\" data-id=\"").append(user.getUserId()).append("\">");

            output.append("<img src=\"").append(photo).append("\" alt=\"")
                  .append(user.getName()).append(" 사진\">");

            output.append("<div class=\"user-info\">");
            output.append("<h3>").append(user.getName()).append("</h3>");
            output.append("<p><strong>이메일:</strong> ").append(user.getEmail()).append("</p>");
            output.append("<p><strong>사번:</strong> ").append(user.getUserId()).append("</p>");
            output.append("<p><strong>상태:</strong> ").append(user.getStatus()).append("</p>");
            output.append("<span class=\"status-badge\" style=\"background-color: ")
                  .append(badgeColor).append("\"> ").append(user.getStatus()).append(" </span>");
            output.append("<button onclick=\"alert('").append(user.getName())
                  .append("의 상세 정보')\">상세 보기</button>");
            output.append("</div></div>");
        }
        output.append("</div>");

        // JSP에 출력
        getJspContext().getOut().write(output.toString());
    }
}

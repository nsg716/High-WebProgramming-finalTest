/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.AdminDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import jakarta.servlet.http.HttpSession;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
/**
 * LoginControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/app-context.xml")
@WebAppConfiguration
public class LoginControllerTest {

    private MockMvc mockMvc;

    private UserDAO userDAO;
    private AdminDAO adminDAO;

    private User validUser;
    private Admin validAdmin;
    private User invalidUser;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        // MockMvc 설정
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // Mock DAO 생성
        userDAO = mock(UserDAO.class);
        adminDAO = mock(AdminDAO.class);

        // 정상 사용자 및 관리자 데이터 설정
        validUser = new User();
        validUser.setUserId("user001");
        validUser.setName("alice");
        validUser.setPassword("password123!");

        validAdmin = new Admin();
        validAdmin.setId("admin001");
        validAdmin.setName("Admin1");
        validAdmin.setPassword("adminpass");

        // 비정상 사용자 데이터 설정
        invalidUser = new User();
        invalidUser.setUserId("invalidUser");
        invalidUser.setName("Invalid");
        invalidUser.setPassword("wrongpassword");

        // Mock 데이터 반환 설정
        when(userDAO.getUserById("user001")).thenReturn(validUser);
        when(userDAO.getUserById("invalidUser")).thenReturn(null);

        when(adminDAO.getAdminById("admin001")).thenReturn(validAdmin);
        when(adminDAO.getAdminById("invalidAdmin")).thenReturn(null);
    }
    
    // 사용자로 로그인 성공 테스트
    // 예상 결과: 리다이렉션 및 세션에 사용자 정보가 저장됨
    @Test
    void testLoginAsUserSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "user001")
                .param("password", "password123!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(request().sessionAttribute("user", validUser));
    }
    
    // 사용자 로그아웃 테스트
    // 예상 결과: 리다이렉션 및 로그인 페이지로 이동
    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout")
                .sessionAttr("user", validUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    
    // 관리자 로그인 성공 테스트
    // 예상 결과: 리다이렉션 및 세션에 관리자 정보가 저장됨
    @Test
    void testLoginAsAdminSuccess() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "admin001")
                .param("password", "adminpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(result -> {
                    HttpSession session = result.getRequest().getSession();
                    Admin sessionAdmin = (Admin) session.getAttribute("admin"); // 세션 얻기 
                    
                    assertNotNull(sessionAdmin);
                    assertEquals("admin001", sessionAdmin.getId());
                    assertEquals("Admin1", sessionAdmin.getName());
                    assertEquals("adminpass", sessionAdmin.getPassword());
                });
    }
    
    // 관리자 로그아웃 테스트
    // 예상 결과: 리다이렉션 및 로그인 페이지로 이동
    @Test
    void testLogoutAsAdmin() throws Exception {
        mockMvc.perform(post("/logout")
                .sessionAttr("admin", validAdmin))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    
    // 로그인 실패 테스트
    // 예상 결과: 로그인 페이지로 돌아가고 오류 메시지 표시
    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login")
                .param("username", "invalidUser")
                .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/login"))
                .andExpect(model().attribute("error", "login_fail"));
    }

 

    // 로그인 페이지 접근 테스트
    // 예상 결과: 로그인 페이지가 정상적으로 반환됨
    @Test
    void testLoginPageAccess() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/login"));
    }
}

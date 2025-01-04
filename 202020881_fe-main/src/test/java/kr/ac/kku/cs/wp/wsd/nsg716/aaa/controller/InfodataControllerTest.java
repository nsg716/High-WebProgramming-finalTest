/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.aaa.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.AdminDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.dto.UserDTO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.Admin;

/**
 * Merged Controller Test Suite
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/app-context.xml")
@WebAppConfiguration
public class InfodataControllerTest {

    private MockMvc mockMvc;
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private AdminDAO adminDAO;

    @Mock
    private UserDAO mockedUserDAO; // For signup specific testing

    @InjectMocks
    private SignupController signupController;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession userSession;
    private MockHttpSession adminSession;
    private MockHttpSession mockSession;
    private MockHttpSession notmockSession;

    private User testUser;
    private Admin testAdmin;
    private Admin adminUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // Test User 설정
        testUser = userDAO.getUserById("user001");
        if (testUser == null) {
            testUser = new User();
            testUser.setUserId("user011");
            testUser.setName("TestUser");
            testUser.setEmail("test@example.com");
            testUser.setPassword("password123");
            testUser.setStatus("active");
        }

        // Test Admin 설정
        testAdmin = adminDAO.getAdminById("admin001");
        if (testAdmin == null) {
            testAdmin = new Admin();
            testAdmin.setId("admin001");
            testAdmin.setName("Admin1");
            testAdmin.setEmail("admin@example.com");
        }

        // 사용자 세션 설정
        userSession = new MockHttpSession();
        userSession.setAttribute("user", testUser);

        // 관리자 세션 설정
        adminSession = new MockHttpSession();
        adminSession.setAttribute("admin", testAdmin);

        // Mock admin session for signup tests
        adminUser = new Admin();
        adminUser.setId("admin001");
        adminUser.setName("Admin1");
        mockSession = new MockHttpSession();
        mockSession.setAttribute("admin", adminUser);
        
        notmockSession = new MockHttpSession();
        notmockSession.setAttribute("notadmin", adminUser);

        // Mocking UserDAO for specific signup tests
        mockedUserDAO = mock(UserDAO.class);
    }

    // InfoControllerTest 메서드들
    @Test
    void testUpdateInfo_UserSuccess() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(testUser.getUserId());
        userDTO.setName("alice");
        userDTO.setEmail("alice@example.com");
        userDTO.setPassword("password123!");

        mockMvc.perform(post("/user/updateinfo")
                .session(userSession)
                .flashAttr("userDTO", userDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User updatedUser = userDAO.getUserById(testUser.getUserId());
        assertEquals("alice", updatedUser.getName());
        assertEquals("alice@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteInfo_UserSelf() throws Exception {
        mockMvc.perform(get("/user/deleteinfo")
                .session(userSession))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertEquals("user001", testUser.getUserId());
    }

    @Test
    void testGetAdminInfo_AdminSession() throws Exception {
        mockMvc.perform(get("/admin/admininfo")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admininfo"))
                .andExpect(model().attribute("adminInfo", testAdmin));
    }

    @Test
    void testDeleteInfo_AdminDeleteUser1() throws Exception {
        mockMvc.perform(get("/user/deleteinfo")
                .session(adminSession)
                .param("userId", "user011"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertNull(userDAO.getUserById("user011"));
    }

    @Test
    void testDeleteInfo_AdminDeleteUser2() throws Exception {
        mockMvc.perform(get("/user/deleteinfo")
                .session(adminSession)
                .param("userId", "user012"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertNull(userDAO.getUserById("user012"));
    }

    @Test
    void testEditInfo_UserSession() throws Exception {
        mockMvc.perform(post("/user/editinfo")
                .session(userSession))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/editinfo"))
                .andExpect(model().attribute("isAdmin", false));
    }

    @Test
    void testMyInfoGet_UserSession() throws Exception {
        mockMvc.perform(get("/user/myinfo")
                .session(userSession))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/myinfo"))
                .andExpect(model().attribute("userInfo", testUser))
                .andExpect(model().attribute("isAdmin", false));
    }

    @Test
    void testDeleteInfo_NoSession() throws Exception {
        mockMvc.perform(get("/user/deleteinfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testGetAdminInfo_NoSession() throws Exception {
        mockMvc.perform(get("/admin/admininfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testMyInfoGet_AdminSession() throws Exception {
        mockMvc.perform(get("/user/myinfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testMyInfoGet_NoSession() throws Exception {
        mockMvc.perform(get("/user/myinfo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // SignupControllerTest 메서드들
    @Test
    void testSignupPageAccess() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/signup"));
    }

    @Test
    void testSignupAdminSuccess() throws Exception {
        UserDTO newUser = new UserDTO();
        newUser.setId("user011");
        newUser.setName("TestUser");
        newUser.setPassword("password123!");
        newUser.setEmail("test@example.com");

        doNothing().when(mockedUserDAO).save(any(User.class));

        mockMvc.perform(post("/signup")
                .session(mockSession)
                .param("id", "user011")
                .param("name", "TestUser")
                .param("password", "password123!")
                .param("email", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testSignupSuccess() throws Exception {
        UserDTO newUser = new UserDTO();
        newUser.setId("user012");
        newUser.setName("testuser2");
        newUser.setPassword("password123!");
        newUser.setEmail("test@example.com");

        mockMvc.perform(post("/signup")
                .session(notmockSession)
                .param("id", "user012")
                .param("name", "testuser2")
                .param("password", "password123!")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/login"));
    }

    @Test
    void testSignupError_AdminInId() throws Exception {
        mockMvc.perform(post("/signup")
                .session(mockSession)
                .param("id", "adminUser")
                .param("name", "Invalid User")
                .param("password", "invalidPassword")
                .param("email", "invalid@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "id"));
    }
    
    @Test
    void testSignupError_DuplicateId() throws Exception {
        User existingUser = new User();
        existingUser.setUserId("user001");
        when(mockedUserDAO.getUserById("user001")).thenReturn(existingUser);

        mockMvc.perform(post("/signup")
                .session(mockSession)
                .param("id", "user001")
                .param("name", "Test User")
                .param("password", "testPassword")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "id"));
    }
    
    @Test
    void testSignupError_ValidationFailures() throws Exception {
        mockMvc.perform(post("/signup")
                .session(mockSession)
                .param("id", "")
                .param("name", "Test User")
                .param("password", "")
                .param("email", "invalidEmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/signup"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "id", "password", "email"));
    }
}
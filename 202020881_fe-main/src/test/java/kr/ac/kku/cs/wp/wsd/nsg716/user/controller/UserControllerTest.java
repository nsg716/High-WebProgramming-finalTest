/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.ac.kku.cs.wp.wsd.nsg716.user.dao.UserDAO;
import kr.ac.kku.cs.wp.wsd.nsg716.user.entity.User;
/**
 * UserControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@SpringJUnitConfig
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/app-context.xml")
@WebAppConfiguration
public class UserControllerTest {

    // MockMvc: 스프링 MVC 컨트롤러 테스트를 위한 모의 환경 설정
    private MockMvc mockMvc;

    // 웹 애플리케이션 컨텍스트 주입
    @Autowired
    private WebApplicationContext context;

    // 사용자 데이터 접근 객체 (DAO) 주입
    @Autowired
    private UserDAO userDAO;

    // 샘플 사용자 데이터 리스트
    private List<User> sampleUsers;

    // JSON 직렬화/역직렬화를 위한 ObjectMapper
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // MockMvc 설정 - 웹 애플리케이션 컨텍스트를 사용하여 테스트 환경 초기화
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // UserDAO 목 객체 생성 (실제 데이터베이스 대신 가상 데이터 사용)
        userDAO = org.mockito.Mockito.mock(UserDAO.class);

        // 샘플 사용자 데이터 생성 - 제공된 INSERT 문과 동일한 데이터
        sampleUsers = Arrays.asList(
            createUser("user001", "Alice", "alice@example.com", "active"),
            createUser("user002", "bob", "bob@example.com", "active"),
            createUser("user003", "charlie", "charlie@example.com", "active")
        );

        // JSON 직렬화/역직렬화를 위한 ObjectMapper 초기화
        objectMapper = new ObjectMapper();

        // 목 사용자 DAO의 메서드 동작 정의
        when(userDAO.getUsers(null)).thenReturn(sampleUsers);
        when(userDAO.getAllUsers()).thenReturn(sampleUsers);
        when(userDAO.getUsers(any(User.class))).thenReturn(sampleUsers);
    }

    // 사용자 객체 생성을 위한 헬퍼 메서드 - 코드 중복 방지 및 가독성 개선
    private User createUser(String userId, String name, String email, String status) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setStatus(status);
        return user;
    }

    // 사용자 목록 접근 테스트
    // 예상 결과: 성공적인 접근, 사용자 리스트 뷰, 사용자 데이터 존재
    @Test
    void testUserListAccess() throws Exception {
        mockMvc.perform(get("/user/userlist"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/userlist"))
                .andExpect(model().attributeExists("users"));
    }

    // 사용자 이름으로 필터링 테스트
    // 예상 결과: 사용자 카드 뷰, 필터링된 사용자 데이터
    @Test
    void testFilterUserListByName() throws Exception {
        User filterParams = new User();
        filterParams.setName("Alice");

        mockMvc.perform(post("/user/userlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterParams)))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/userCard"))
                .andExpect(model().attributeExists("users"));
    }

    // 사용자 ID로 필터링 테스트
    // 예상 결과: 사용자 카드 뷰, 필터링된 사용자 데이터
    @Test
    void testFilterUserListByUserId() throws Exception {
        User filterParams = new User();
        filterParams.setUserId("user002");

        mockMvc.perform(post("/user/userlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filterParams)))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/userCard"))
                .andExpect(model().attributeExists("users"));
    }

    // 빈 필터 파라미터로 사용자 리스트 테스트
    // 예상 결과: 전체 사용자 리스트 뷰, 모든 사용자 데이터
    @Test
    void testFilterUserListWithEmptyFilter() throws Exception {
        mockMvc.perform(post("/user/userlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/userCard"))
                .andExpect(model().attributeExists("users"));
    }

    // Null 파라미터로 사용자 리스트 테스트
    // 예상 결과: 전체 사용자 리스트 뷰, 모든 사용자 데이터
    @Test
    void testFilterUserListNullParams() throws Exception {
        mockMvc.perform(post("/user/userlist/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/userlist"))
                .andExpect(model().attributeExists("users"));
    }
}
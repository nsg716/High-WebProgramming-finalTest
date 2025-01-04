/*
 * 저작권 (C) 2024 나성곤 202020881 모든 권리 보유
 * 이 소프트웨어는 고급 웹 프로그래밍 기말고사 프로젝트 제출용입니다.
 * 이 소프트웨어는 개인적, 교육적 또는 비상업적 목적으로 자유롭게 사용할 수 있습니다.
 * 상업적 사용을 위해서는 타인의 권리를 침해하지 않도록 주의해야합니다.
 * 
 * 연략처 : 1029cjswo@naver.com
 */
package kr.ac.kku.cs.wp.wsd.nsg716.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * ViewControllerTest
 * 
 * @author 나성곤 학번-202020881
 * @since 2024. 12. 06.
 * @version 1.0
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/app-context.xml")
@WebAppConfiguration
public class ViewControllerTest {

    private static final Logger logger = LogManager.getLogger(ViewControllerTest.class);

    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    // GET /home 엔드포인트 테스트
    // 예상 결과: 상태 코드 200 OK 및 뷰 이름 "/home" 반환
    @Test
    public void testHomeGet() throws Exception {
        logger.info("Testing GET /home");

        mockMvc.perform(get("/home"))
            .andDo(print()) // Print request/response for debugging
            .andExpect(status().isOk()) // Expect 200 OK
            .andExpect(view().name("/home")); // Expect the view name to be "/home"
    }
    
    // POST /home 엔드포인트 테스트
    // 예상 결과: 상태 코드 200 OK 및 뷰 이름 "/home" 반환
    @Test
    public void testHomePost() throws Exception {
        logger.info("Testing POST /home");

        mockMvc.perform(post("/home"))
            .andDo(print()) // Print request/response for debugging
            .andExpect(status().isOk()) // Expect 200 OK
            .andExpect(view().name("/home")); // Expect the view name to be "/home"
    }
}

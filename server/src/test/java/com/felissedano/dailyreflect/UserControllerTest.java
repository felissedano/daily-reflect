package com.felissedano.dailyreflect;

import com.felissedano.dailyreflect.auth.web.UserController;
import com.felissedano.dailyreflect.auth.service.UserService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

//    @Test
//    public void shouldReturnHelloWorld() throws Exception {
//        mockMvc.perform(get("/api/user/test")).andExpect(status().isOk()).andExpect(content().string("Hello World!"));
//    }
}

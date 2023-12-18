package org.app.api;


import org.app.service.UserQueriesService;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserQueriesControllerTest {

    @InjectMocks
    UserQueriesController userQueriesController;

    @Mock
    UserQueriesService userQueriesService;

    @Autowired
    MockMvc mockMvc;

   //

}

package com.abc.aftersale;

import com.abc.aftersale.dto.OrderDTO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
class AftersaleApplicationTests {

    @Test
    void contextLoads() {
    }
    @Resource
    private RuntimeService runtimeService;


}

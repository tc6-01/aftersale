package com.abc.aftersale;

import com.abc.aftersale.dto.OrderDTO;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AftersaleApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Resource
    private RuntimeService runtimeService;


}

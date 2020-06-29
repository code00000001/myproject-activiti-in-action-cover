package com.code00000001.activiti.chapter9;

import com.code00000001.activiti.AbstractTest;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author code00000001 on 2020/6/27
 */
public class MultiInstanceTest extends AbstractTest {
    @Test
    @Deployment(resources = {"diagrams/chapter9/testMultiInstanceFixedNumbers.bpmn"})
    public void testParallel() throws Exception{
        Map<String, Object> variables = new HashMap<String, Object>();
        // 共建3个实例
        long loop = 3;
        variables.put("loop", loop);
        // 计数器从0开始
        variables.put("counter", 0);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("testMultiInstanceFixedNumbers", variables);
        Object counter = runtimeService.getVariable(processInstance.getId(), "counter");
        assertEquals(loop, counter);
    }
}

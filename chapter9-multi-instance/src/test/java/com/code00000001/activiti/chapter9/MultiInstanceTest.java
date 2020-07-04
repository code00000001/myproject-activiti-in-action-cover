package com.code00000001.activiti.chapter9;

import com.code00000001.activiti.AbstractTest;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.util.*;

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

    @Test
    @Deployment(resources = {"diagrams/chapter9/testMultiInstanceForUserTask.users.sequential.bpmn"})
    public void testForUserCreateByUsersSequential() throws Exception {
        Map<String, Object> variables = new HashedMap();
        // 设置三个会签用户
        List<String> users = Arrays.asList("zhangsan", "lisi", "wangwu");
        variables.put("users", users);
        runtimeService.startProcessInstanceByKey("testMultiInstanceForUserTask", variables);
        for (String user : users) {
            // 遍历三次完成任务
            Task task = taskService.createTaskQuery().taskAssignee(user).singleResult();
            taskService.complete(task.getId());
        }
    }

    @Test
    @Deployment(resources = {"diagrams/chapter9/testMultiInstanceForUserTask.users.nosequential.bpmn"})
    public void testForUserCreateByUsersNosequential() throws Exception {
        Map<String, Object> variables = new HashedMap();
        List<String> users = Arrays.asList("zhangsan", "lisi", "wangwu");
        variables.put("users", users);
        runtimeService.startProcessInstanceByKey("testMultiInstanceForUserTask", variables);
        for (String user : users) {
            // 验证每个用户都有一个任务
            assertEquals(1,taskService.createTaskQuery().taskAssignee(user).count());
        }
    }

    @Test
    @Deployment(resources = {"diagrams/chapter9/testMultiInstanceForUserTask.users.sequential.with.complete.conditon.bpmn"})
    public void testForUserCreateBuUsersSequentialWithCompleteCondition() throws Exception {
        Map<String, Object> variables = new HashedMap();
        List<String> users = Arrays.asList("zhangsan", "lisi", "wangwu");
        variables.put("users", users);
        variables.put("rate", 0.6d);
        runtimeService.startProcessInstanceByKey("testMultiInstanceForUserTask", variables);
        // 完成第一条task
        Task zhangsan = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        taskService.complete(zhangsan.getId());

        // 完成第二条task
        Task lisi = taskService.createTaskQuery().taskAssignee("lisi").singleResult();
        taskService.complete(lisi.getId());

        // 此时因为3个任务已经完成超过60%，流程就已经结束了
        assertEquals(1,historyService.createHistoricProcessInstanceQuery().finished().count());

    }
}

package com.code00000001.activiti.chapter2;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by code00000001 on 2020/6/6
 */
public class SayHelloToLeaveTest {
    @Test
    public void testStartProcess() throws Exception{
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        repositoryService.createDeployment().addClasspathResource("com.code00000001.activiti.helloworld/SayHelloToLeave.bpmn").deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        assertEquals("SayHelloToLeave", processDefinition.getKey());

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> variables = new HashMap();
        variables.put("applyUser", "employee1");
        variables.put("days", 3);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SayHelloToLeave", variables);

        assertNotNull(processInstance);

        System.out.println("pid=" + processInstance.getId() + ", pdid=" + processDefinition.getId());

        // 获取taskService， 查询deptLeader未签收任务，并验证任务的名称
        TaskService taskService = processEngine.getTaskService();
        Task taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        assertNotNull(taskOfDeptLeader);
        assertEquals("领导审批",taskOfDeptLeader.getName());

        // 调用claim方法签收任务，归leaderUser所有
        taskService.claim(taskOfDeptLeader.getId(),"leaderUser");
        variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete(taskOfDeptLeader.getId(), variables);

        // 任务已完成，再次查询deptLeader已经为空
        taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        assertNull(taskOfDeptLeader);

        // 获取historyService，查询已完成流程实例数量，验证预期结果
        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }
}

package com.code00000001.activiti.chapter2;

import com.sun.org.apache.xpath.internal.operations.Equals;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by code00000001 on 2020/6/6
 */

public class VerySimpleLeaveProcessTest {

    @Test
    public void testStartProcess() {
        // 创建流程引擎，使用内存数据库
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();

        // 获取repositoryService，部署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("com.code00000001.activiti.helloworld/sayhelloleave.bpmn").deploy();

        // 验证已部署的流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        assertEquals("leavesayhelloworld", processDefinition.getKey());

        // 启动流程并返回流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leavesayhelloworld");
        assertNotNull(processEngine);

        System.out.println("pid = " + processInstance.getId() + ", pdid = " + processDefinition.getId());
    }
}

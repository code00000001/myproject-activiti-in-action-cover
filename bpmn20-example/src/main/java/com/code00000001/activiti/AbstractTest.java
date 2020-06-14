package com.code00000001.activiti;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

/**
 * 抽象测试类
 *
 * @author code00000001 on 2020/6/14
 */
public abstract class AbstractTest {

    /**
     * 用于测试套件
     */
    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

    /**
     * 开始测试
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpForClass() throws Exception {
        System.out.println("++++++++++++++开始测试++++++++++++++");
    }

    /**
     * 结束测试
     *
     * @throws Exception
     */
    @AfterClass
    public static void testOverClass() throws Exception {
        System.out.println("+++++++++++++测试结束+++++++++++++++");
    }

    /**
     * 初始化变量
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        processEngine = activitiRule.getProcessEngine();
        repositoryService = activitiRule.getRepositoryService();
        runtimeService = activitiRule.getRuntimeService();
        taskService = activitiRule.getTaskService();
        historyService = activitiRule.getHistoryService();
        managementService = activitiRule.getManagementService();
        formService = activitiRule.getFormService();
        identityService = activitiRule.getIdentityService();
    }

}

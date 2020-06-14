package com.code00000001.activiti.chapter5.identity;

import com.code00000001.activiti.AbstractTest;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author code00000001 on 2020/6/14
 */
public class UserAndGroupInTaskTest extends AbstractTest {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        // 创建并保存组对象
        Group group = identityService.newGroup("deptLeader");
        group.setName("deptLeader");
        group.setType("assignment");
        identityService.saveGroup(group);

        // 创建并保存用户对象
        User user = identityService.newUser("code00000001");
        user.setFirstName("code");
        user.setLastName("00000001");
        user.setEmail("code@gmail.com");
        identityService.saveUser(user);

        // 把用户加入到组中
        identityService.createMembership("code00000001", "deptLeader");
    }

    /**
     * 每个方法执行之后清理用户和组
     *
     * @throws Exception
     */
    @After
    public void afterInvokeTestMethod() throws Exception {
        identityService.deleteMembership("code00000001","deptLeader");
        identityService.deleteGroup("deptLeader");
        identityService.deleteUser("code00000001");
    }

    @Test
    @Deployment(resources = {"chapter5/userAndGroupInUserTask.bpmn"})
    public void testUserAndGroupInUserTask() throws Exception {
        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTaskTest");
        assertNotNull(processInstance);
        // 根据角色查询任务
        Task task = taskService.createTaskQuery().taskCandidateUser("code00000001").singleResult();
        taskService.claim(task.getId(),"code00000001");
        taskService.complete(task.getId());
    }
}

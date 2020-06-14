package com.code00000001.activiti.chapter5.identity;

import com.code00000001.activiti.AbstractTest;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * 用户组单元测试
 *
 * @author code00000001 on 2020/6/14
 */
public class IdentityServiceTest extends AbstractTest {

    @Test
    public void testUser() throws Exception{
        // 创建用户
        User user = identityService.newUser("code00000001");
        user.setFirstName("code");
        user.setLastName("00000001");
        user.setEmail("code0000001@gmail.com");

        // 保存用户
        identityService.saveUser(user);

        // 验证用户是否保存成功
        User userQuery = identityService.createUserQuery().userId("code00000001").singleResult();
        assertNotNull(userQuery);

        // 删除用户
        identityService.deleteUser("code00000001");

        // 验证用户是否删除成功
        userQuery = identityService.createUserQuery().userId("code0000001").singleResult();
        assertNull(userQuery);
    }

    @Test
    public void testGroup() throws Exception{
        // 创建组
        Group group = identityService.newGroup("deptLeader");
        group.setName("leader");
        group.setType("assignment");

        identityService.saveGroup(group);

        // 验证组是否创建成功
        Group deptLeader = identityService.createGroupQuery().groupId("deptLeader").singleResult();
        assertNotNull(deptLeader);

        // 删除组
        identityService.deleteGroup("deptLeader");

        // 验证是否删除组成功
        deptLeader = identityService.createGroupQuery().groupId("deptLeader").singleResult();
        assertNull(deptLeader);

    }

    @Test
    public void testUserAndGroupMemberShip() throws Exception {
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
        identityService.createMembership("code00000001","deptLeader");

        // 查询属于组的用户
        User userInGroup = identityService.createUserQuery().memberOfGroup("deptLeader").singleResult();
        assertNotNull(userInGroup);
        assertEquals("code00000001",userInGroup.getId());

        // 查询用户所在组
        Group groupUser = identityService.createGroupQuery().groupMember("code00000001").singleResult();
        assertNotNull(groupUser);
        assertEquals("deptLeader",groupUser.getId());
    }
}

package com.example.springboot.security;

import com.example.springboot.portal.dao.IResourceDao;
import com.example.springboot.portal.dao.IUserDao;
import com.example.springboot.portal.entity.Resource;
import com.example.springboot.portal.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService,
        AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IResourceDao iResourceDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserDao.getUserByPortalName(username);
        if (user != null) {

            List<String> roleIds = iUserDao.getUserRoleIds(user.getUserId());
            user.intiUserRoleString(roleIds);

            AuthenticationUser authenticationUser = new AuthenticationUser();
            authenticationUser.setUsername(username); // 保存登录名
            authenticationUser.setPassword(user.getPassword()); // 保存密码
            authenticationUser.setUser(user);
            authenticationUser.setUserId(user.getUserId());// 保存用户id
            authenticationUser.setRoleIds(new HashSet<String>(roleIds)); // 保存角色ID

            List<Resource> authorizedResources = new ArrayList<Resource>();
            if (roleIds.size() > 0) {
                authorizedResources = iResourceDao
                        .getResourcesByRoleId(roleIds);
            }
            // List<Resource> modules = new ArrayList<Resource>(); // 一级菜单
            StringBuffer allResourcesStr = new StringBuffer();
            for (Resource resource : authorizedResources) {
                allResourcesStr.append(resource.getResourceId());
                allResourcesStr.append(";");
                // if
                // (Constants.RESOURCE_ROOT_ID.equals(resource.getParentNo())) {
                // modules.add(resource);
                // }
            }
            // authenticationUser.setModules(modules); // 保存一级菜单
            // authenticationUser.setAllResources(authorizedResources); //
            // 保存所有授权资源
            authenticationUser.setAllResourcesStr(allResourcesStr.toString()); // 所有资源toString
            return authenticationUser;
        }
        return null;
    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException{
        String name = token.getName();
        System.out.println("获得的用户名："+name);
        UserDetails user = this.loadUserByUsername(name);
        if (user==null){
            throw new UsernameNotFoundException(name+"不存在");
         }
        return user;
    }
}

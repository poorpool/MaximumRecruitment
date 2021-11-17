package com.maximumteam.recruitment.backend.service;

import com.maximumteam.recruitment.backend.entity.Account;
import com.maximumteam.recruitment.backend.entity.JWTToken;
import com.maximumteam.recruitment.backend.util.JWTUtil;
import com.maximumteam.recruitment.backend.util.Utils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String email = JWTUtil.getEmail(principals.toString());
        Account account = accountService.findAccountByEmail(email);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(Utils.permissionToRoles(account.getPermission()));
        // 也许我们只需要role就够了
//        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
//        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得email，用于和数据库进行对比
        String email = JWTUtil.getEmail(token);
        if (email == null) {
            throw new AuthenticationException("token invalid");
        }

        Account account = accountService.findAccountByEmail(email);
        if (account == null) {
            throw new AuthenticationException("Account didn't existed!");
        }

        if (! JWTUtil.verify(token, email, account.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }
}

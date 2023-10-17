package com.vmg.scrum.service.impl;

import com.vmg.scrum.service.LdapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class LdapServiceImpl implements LdapService {
    @Value("${ldap.key}")
    private String ldapKey;

    @Value("${ldap.base.ou}")
    private String ldapBaseOU;

    @Autowired
    LdapTemplate ldapTemplate;

    @Override
    public void login(String username, String password) {
        try {
            Filter filter = new EqualsFilter(ldapKey, username);
            boolean isAuthed = ldapTemplate.authenticate(ldapBaseOU, filter.encode(), password);
            if (!isAuthed) {
                log.info("Không tìm thấy thông tin người dùng trên hệ thông LDAP");
                throw new RuntimeException("Tài khoản hoặc mật khẩu không đúng");
            }
        } catch (Exception e) {
            log.error("Lỗi kết nối ldap", e);
            throw new RuntimeException("Tài khoản hoặc mật khẩu không đúng");
        }
    }
}

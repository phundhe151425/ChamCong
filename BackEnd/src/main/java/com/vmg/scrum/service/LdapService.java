package com.vmg.scrum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import javax.naming.directory.SearchControls;
import java.util.List;


public interface LdapService {
    void login(String username, String password);
}

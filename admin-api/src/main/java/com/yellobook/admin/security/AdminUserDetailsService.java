package com.yellobook.admin.security;

import com.yellobook.admin.domain.AdminMember;
import com.yellobook.admin.domain.AdminMemberRepository;
import com.yellobook.admin.support.error.AdminErrorType;
import com.yellobook.admin.support.error.AdminException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {
    private final AdminMemberRepository adminMemberRepository;

    public AdminUserDetailsService(AdminMemberRepository adminMemberRepository) {
        this.adminMemberRepository = adminMemberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminMember adminMember = adminMemberRepository.findByUsername(username)
                .orElseThrow(() -> new AdminException(AdminErrorType.ACCOUNT_NOT_FOUND));
        return new AdminUserDetails(adminMember.sub(), adminMember.password(), adminMember.role());
    }
}

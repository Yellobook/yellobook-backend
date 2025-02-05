package com.yellobook.api.support.auth.security;

import com.yellobook.api.support.auth.AppMemberRole;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class CustomUserDetails implements UserDetails {
    private final Long memberId;
    private final AppMemberRole role;

    public CustomUserDetails(Long memberId, AppMemberRole role) {
        this.memberId = memberId;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public Long getMemberId() {
        return memberId;
    }

    public AppMemberRole getRole() {
        return role;
    }
}

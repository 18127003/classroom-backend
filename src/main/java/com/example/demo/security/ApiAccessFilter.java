package com.example.demo.security;

import com.example.demo.common.exception.RTException;
import com.example.demo.entity.Account;
import com.example.demo.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class ApiAccessFilter extends OncePerRequestFilter {

    public static final String CLASSROOM_URL_PATTERN = "**/classroom/{id:[\\d+]}/**";

    @Autowired
    private AntPathMatcher antPathMatcher;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ParticipantInfo participantInfo;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var requestUrl = httpServletRequest.getRequestURL().toString();
        if(antPathMatcher.match(CLASSROOM_URL_PATTERN, requestUrl)){
            Map<String, String> pathVariables = antPathMatcher.extractUriTemplateVariables(CLASSROOM_URL_PATTERN, requestUrl);
            var classroomId = Long.valueOf(pathVariables.get("id"));
            Long accountId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            try{
                var participant = classroomService.getAssignedClassroom(classroomId, accountId);
                participantInfo.setParticipant(participant);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        accountId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(participant.getRole().toString())));
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (RTException e){
                // ignore because unauthorized later
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

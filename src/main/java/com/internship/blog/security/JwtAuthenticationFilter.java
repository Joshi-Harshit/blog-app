package com.internship.blog.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. get token

        String requestToken = request.getHeader("Authorization");
        logger.info("Header : {}", requestToken);

        String username = null;

        String token = null;

        if(requestToken!=null && requestToken.startsWith("Bearer")){

            token = requestToken.substring(7);

            try{
                username = this.jwtTokenHelper.getUserNameFromToken(token);

            }catch (IllegalArgumentException e){
                logger.info("Illegal Argument while fetching the username");
                e.printStackTrace();

            }catch (ExpiredJwtException e){
                logger.info("Given jwt token has expired");
                e.printStackTrace();

            } catch (MalformedJwtException e){
                logger.info("Something has been changed in token");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // once we get the token, now we can validate it

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if(this.jwtTokenHelper.validateToken(token, userDetails)){

                    // authentication

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }else{
                    logger.info("Invalid jwt token");
                }

            }else{
                logger.info("Username is null or context is not null");
            }

        }else{
            logger.info("Jwt token does not begin with bearer");
        }

        filterChain.doFilter(request, response);
    }
}
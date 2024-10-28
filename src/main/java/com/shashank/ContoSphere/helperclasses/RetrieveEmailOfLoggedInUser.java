package com.shashank.ContoSphere.helperclasses;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class RetrieveEmailOfLoggedInUser {
    public static String getEmailOfLoggedInUser(Authentication authentication){

        if (authentication instanceof OAuth2AuthenticationToken) {

            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            OAuth2User oAuth2User=(OAuth2User) authentication.getPrincipal();
            String username = "";

            //Finding the Username Using Google Login
            if(clientId.equalsIgnoreCase("google"))
            {
                System.out.println("Getting Email from Google");
                username=oAuth2User.getAttribute("email").toString();
            }

            //Finding the Username Using GitHub Login
            else if(clientId.equalsIgnoreCase("github"))
            {
                System.out.println("Getting Email from GitHub");
                username= oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString()
                        : oAuth2User.getAttribute("login").toString()+"@gmail.com";
            }
            //Finding the Username Using Facebook Login
            else if(clientId.equalsIgnoreCase("facebook"))
            {
                System.out.println("Getting Email from Facebook");
            }return username;

        //Finding the Username Using Manual Login-
        }else {
            return authentication.getName();
        }
    }
}

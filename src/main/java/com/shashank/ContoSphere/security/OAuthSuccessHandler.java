package com.shashank.ContoSphere.security;

import com.shashank.ContoSphere.entity.Providers;
import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.AppConstants;
import com.shashank.ContoSphere.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    Logger logger=LoggerFactory.getLogger(OAuthSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                                                        throws IOException, ServletException {
        logger.info("OAuthSuccessHandler");

        // Identify the Provider
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();//this ID will tell which provider are we using
        logger.info(authorizedClientRegistrationId); // used to print the same id

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User)authentication.getPrincipal();
        oAuth2User.getAttributes().forEach((key, value)->{
            logger.info(key + " : " + value);
        });

        //Default User Setting - this will store for all type of login mentioned below
        User user=new User();
        user.setRolesList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);

        if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            // Google Attributes
            user.setEmail(oAuth2User.getAttribute("email").toString());
            user.setUserName(oAuth2User.getAttribute("name").toString());
            user.setProfilePic(oAuth2User.getAttribute("picture").toString());
            user.setProviderID(oAuth2User.getName());
            user.setProviders(Providers.GOOGLE);

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            // GitHub Attributes

            // We have just user different method but both works same if you want you can user google method here also
            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString()
                    : oAuth2User.getAttribute("login").toString()+"@gmail.com";
            String picture=oAuth2User.getAttribute("avatar_url").toString();
            String name = oAuth2User.getAttribute("login").toString();
            String providerId = oAuth2User.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setUserName(name);
            user.setProviderID(providerId);
            user.setProviders(Providers.GITHUB);
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("facebook")) {
            // Facebook Attributes
            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString()
                    : oAuth2User.getAttribute("login").toString()+"@gmail.com";

            user.setEmail(email);
            user.setUserName(oAuth2User.getAttribute("name").toString());
            user.setProviderID(oAuth2User.getName().toString());
            user.setProviders(Providers.FACEBOOK);

        }else{
            logger.info("oAuthAuthenticationSuccessHandler : Unknown Provider");
        }

        User user2=userRepository.findByEmail(user.getEmail()).orElse(null);
        if(user2==null) {
            userRepository.save(user);
        }


        /*  this is only for google above we can use for multiple things
// Save to database method
      DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();

        String email=user.getAttribute("email").toString();
        String name=user.getAttribute("name").toString();
        String picture=user.getAttribute("picture").toString();

        User user1=new User();
        user1.setEmail(email);
        user1.setUserName(name);
        user1.setProviders(Providers.GOOGLE);
        user1.setProviderID(user.getName());

        User user2=userRepository.findByEmail(email).orElse(null);
        if(user2==null){
            userRepository.save(user1);
            logger.info("User Saved : " + email);
        }

/*  If we want to get that attributes then we can use this
        logger.info(user.getName());
        user.getAttributes().forEach((key, value)->{
            logger.info("{} => {}", key, value);
        });
        logger.info(user.getAuthorities().toString());

 */
        new DefaultRedirectStrategy().sendRedirect(request,response,"/user/signed-in");
    }
}

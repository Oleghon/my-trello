package com.spd.trello.security.extrafilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spd.trello.domain.Domain;
import com.spd.trello.domain.enums.Permission;
import com.spd.trello.domain.resources.Member;
import com.spd.trello.domain.resources.User;
import com.spd.trello.repository_jpa.CommonRepository;
import com.spd.trello.repository_jpa.MemberRepository;
import com.spd.trello.repository_jpa.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

public abstract class AbstractChecker<E extends Domain, R extends CommonRepository<E>> implements ExtraAuthorizationChecker<E> {

    UserRepository userRepository;
    MemberRepository memberRepository;
    R entityRepository;
    private Pattern regex = Pattern.compile("(/\\w+[/a-z]/)");
    ObjectMapper mapper = new ObjectMapper();

    public AbstractChecker(UserRepository userRepository, MemberRepository memberRepository, R entityRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.entityRepository = entityRepository;
    }

    @Override
    public void checkAuthority(HttpServletRequest request) {
        String uri = request.getRequestURI();
        User user = getAuthorizedUser();
        String method = request.getMethod();
        switch (method) {
            case "GET":
                if (regex.matcher(uri).find())
                    checkEntityAccessRights(getIdFromRequest(uri), user);
                break;
            case "PUT":
                checkMembership(getIdFromRequest(uri), user, Permission.UPDATE);
                break;
            case "DELETE":
                checkMembership(getIdFromRequest(uri), user, Permission.WRITE);
                break;
            case "POST":
                checkPostRequest(request, user.getId());
                break;
        }
    }

    protected abstract void checkPostRequest(HttpServletRequest request, UUID userId);

    protected abstract void checkMembership(UUID entityId, User user, Permission permission);

    protected abstract Member findMemberBy(UUID entityId, UUID userId);

    protected abstract void checkEntityAccessRights(UUID entityId, User user);

    //todo
    protected E readFromJson(HttpServletRequest request, Class<E> clazz) {
        try {
            return mapper.readValue(request.getInputStream(), clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private UUID getIdFromRequest(String request) {
        return UUID.fromString(request.replaceAll(regex.pattern(), ""));
    }

    private User getAuthorizedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userByEmail = userRepository.findUserByEmail(authentication.getName()).get();
        return userByEmail;
    }
}

package com.illia.carrental.core.commons.context;

import com.illia.carrental.core.model.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class RequestUserContext {
    private UserDTO user;
}
package com.soccer.ws.validators;

import com.soccer.ws.dto.PasswordRecoveryDTO;
import com.soccer.ws.service.AccountService;
import com.soccer.ws.utils.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by u0090265 on 9/12/14.
 */
@Component
public class PwdRecoveryValidator implements Validator {
    private final AccountService accountService;

    @Autowired
    public PwdRecoveryValidator(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordRecoveryDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordRecoveryDTO form = (PasswordRecoveryDTO) o;
        ValidationUtils.rejectIfEmpty(errors, "email", "validation.notempty.message");

        if (form.getEmail() != null && accountService.getActiveAccountByEmail(form.getEmail()) == null) {
            errors.rejectValue("email", "validation.email.not.exists");
        }

        ValidationUtils.rejectIfEmpty(errors, "code", "validation.notempty.message");
        ValidationUtils.rejectIfEmpty(errors, "password", "validation.notempty.message");

        if (!ValidationHelper.isPasswordMatch(form.getPassword()))
            errors.rejectValue("password", "validation.complexity.password.message");

    }
}

package com.soccer.ws.dto;

import lombok.*;

/**
 * Created by u0090265 on 13.08.17.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PasswordDTO extends BaseClassDTO {
    private String oldPassword;
    private String newPassword;

    public PasswordDTO(long id, String newPassword, String oldPassword) {
        super(id);
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}

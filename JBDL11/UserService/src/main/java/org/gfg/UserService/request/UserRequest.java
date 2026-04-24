package org.gfg.UserService.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gfg.model.UserIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

    String name;
    String email;
    String phoneNo;
    String password;
    UserIdentifier userIdentifier;
    String userIdentifierValue;
    String dob;


}

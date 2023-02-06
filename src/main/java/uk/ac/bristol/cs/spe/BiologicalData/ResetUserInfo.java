package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;

public class ResetUserInfo {
    @Getter
    @Setter
    private String email;
    @Getter @Setter
    @ValidPassword
    private String password;

    public ResetUserInfo(){ }
}

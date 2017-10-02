package com.zoho.app.model.response;

/**
 * Created by hp on 02-10-2017.
 */

public class SignupResponseModel {
   private LoginResponseModel signUpResult;

    public void setSignUpResult(LoginResponseModel signUpResult) {
        this.signUpResult = signUpResult;
    }

    public LoginResponseModel getSignUpResult() {
        return signUpResult;
    }
}

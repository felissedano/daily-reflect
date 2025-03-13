package com.felissedano.dailyreflect.auth.service;

import com.felissedano.dailyreflect.auth.service.dto.PasswordChangeDTO;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;

public interface PasswordService {

    public void sendResetPasswordEmail(String email);

    public void resetPassword(PasswordResetDTO passwordResetDTO);

    public boolean changePassword(PasswordChangeDTO passwordChangeDTO);



}

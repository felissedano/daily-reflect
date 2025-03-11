package com.felissedano.dailyreflect.auth.service;

import com.felissedano.dailyreflect.auth.service.dto.PasswordChangeDTO;
import com.felissedano.dailyreflect.auth.service.dto.PasswordResetDTO;

public interface PasswordService {

    public boolean sendResetPasswordEmail(String email);

    public boolean resetPassword(PasswordResetDTO passwordResetDTO);

    public boolean changePassword(PasswordChangeDTO passwordChangeDTO);



}

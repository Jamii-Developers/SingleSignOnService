package com.jamii.applicationControllers;

import com.jamii.Utils.JamiiDebug;
import com.jamii.Utils.JamiiLoggingUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractApplicationControllers
{

    protected final JamiiDebug jamiiDebug = new JamiiDebug(this.getClass());

    @Autowired protected JamiiLoggingUtils jamiiLoggingUtils;

    @PostConstruct
    protected abstract void initPathing();

    public abstract ResponseEntity<?> processJSONRequest(String operation, Object requestPayload);

    public abstract ResponseEntity<?> processMultipartRequest(String operation, String userKey, String deviceKey, String sessionKey, MultipartFile file);
}

package CanalDev.Financia.Config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class StorageInitializer {

    @Value("${app.storage.path:${user.home}/Financia}")
    private String path;

    @PostConstruct
    public void init() {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
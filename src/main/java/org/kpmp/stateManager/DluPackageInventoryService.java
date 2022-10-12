package org.kpmp.stateManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class DluPackageInventoryService {

    @Value("${data-manager.service.host}")
    private String dataManagerHost;
    @Value("${data-manager.service.endpoint}")
    private String dataManagerEndpoint;
    private RestTemplate restTemplate;

    private static final Log log = LogFactory.getLog(DluPackageInventoryService.class);

    @Autowired
    public DluPackageInventoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String setPackageInError(String packageId) {
        HashMap payload = new HashMap<>();
        payload.put("dlu_error", true);
        String retPackageId = null;
        String url = dataManagerHost + dataManagerEndpoint + "/package/" + packageId;
        try {
            retPackageId = restTemplate.postForObject(url, payload, String.class);
        } catch (Exception e) {
            log.error("URI: " + url + " | PKGID: " + packageId + " | MSG: Setting DMD package error");
        }
        return retPackageId;
    }
}

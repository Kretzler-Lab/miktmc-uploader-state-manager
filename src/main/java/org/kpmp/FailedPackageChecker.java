package org.kpmp;

import org.kpmp.stateManager.CustomStateRepository;
import org.kpmp.stateManager.State;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@ComponentScan(basePackages = { "org.kpmp" })
public class FailedPackageChecker implements CommandLineRunner {

    CustomStateRepository stateRepository;

    public FailedPackageChecker(CustomStateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FailedPackageChecker.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<State> states = stateRepository.findPackagesUploadStarted();
        List<State> failedPackages = new ArrayList<State>();

        for (State state: states) {
            State succeededState = stateRepository.findPackageUploadSucceedById(state.getPackageId());
            if (succeededState == null) {
                System.out.println(state.getPackageId() + " did not succeed");
                failedPackages.add(state);
            }
            
        }
    }
}

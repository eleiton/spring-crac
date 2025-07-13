package dev.eleiton.crac;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CracConfig implements Resource {

    @PostConstruct
    public void init() {
        Core.getGlobalContext().register(this);
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        System.out.println("Before checkpoint");
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        System.out.println("After restore");
    }

}

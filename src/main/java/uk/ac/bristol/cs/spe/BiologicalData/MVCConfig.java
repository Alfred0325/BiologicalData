package uk.ac.bristol.cs.spe.BiologicalData;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class to manage additional configuration for the webapp.
 */
@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/404").setViewName("error");
        registry.addViewController("/logout").setViewName("logout");

        
    }

    /**
     * Adds a resource handler to allow for the webapp to recognise the dynamic folder as valid storage location.
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("dynamic/**")
                .addResourceLocations("file:" + new StorageProperties().getLocation() + "/");
    }
}

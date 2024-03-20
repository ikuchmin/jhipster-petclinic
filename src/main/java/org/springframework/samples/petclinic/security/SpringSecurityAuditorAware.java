package org.springframework.samples.petclinic.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.samples.petclinic.config.Constants;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM));
    }
}

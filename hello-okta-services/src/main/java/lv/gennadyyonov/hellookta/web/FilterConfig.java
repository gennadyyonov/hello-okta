package lv.gennadyyonov.hellookta.web;

import jakarta.servlet.Filter;
import lv.gennadyyonov.hellookta.config.okta.OktaServiceConfig;
import lv.gennadyyonov.hellookta.dto.FilterOrder;
import lv.gennadyyonov.hellookta.dto.FilterOrderProperties;
import lv.gennadyyonov.hellookta.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
@AutoConfigureAfter(OktaServiceConfig.class)
public class FilterConfig {

    private static final String ALL_URL_PATTERN = "/*";
    private static final int MAX_PAYLOAD_LENGTH = 10000;

    private final FilterOrderProperties filterOrderProperties;
    private final SecurityProperties securityProperties;
    private final AuthenticationService authenticationService;

    @Autowired
    public FilterConfig(FilterOrderProperties filterOrderProperties,
                        SecurityProperties securityProperties,
                        AuthenticationService authenticationService) {
        this.filterOrderProperties = filterOrderProperties;
        this.securityProperties = securityProperties;
        this.authenticationService = authenticationService;
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter(AutowireCapableBeanFactory beanFactory) {
        FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, new ForwardedHeaderFilter()
        );
        filterRegistrationBean.setOrder(HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserLoggingFilter> userLoggingFilter(AutowireCapableBeanFactory beanFactory) {
        FilterRegistrationBean<UserLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, new UserLoggingFilter(authenticationService)
        );
        bean.setOrder(getFilterOrder(FilterOrder.USER_LOGGING));
        return bean;
    }

    @Bean
    public FilterRegistrationBean<HttpRequestLoggingFilter> loggingFilter(AutowireCapableBeanFactory beanFactory) {
        FilterRegistrationBean<HttpRequestLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, new HttpRequestLoggingFilter()
        );
        bean.setOrder(FilterOrder.REQUEST_LOGGING.getOrder());
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter(AutowireCapableBeanFactory beanFactory) {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(MAX_PAYLOAD_LENGTH);
        filter.setIncludeHeaders(true);
        FilterRegistrationBean<CommonsRequestLoggingFilter> bean = makeFilterRegistrationBean(
                beanFactory, ALL_URL_PATTERN, filter
        );
        bean.setOrder(getFilterOrder(FilterOrder.COMMONS_REQUEST_LOGGING));
        return bean;
    }

    private <T extends Filter> FilterRegistrationBean<T> makeFilterRegistrationBean(AutowireCapableBeanFactory beanFactory,
                                                                                    String urlPatterns, T filter) {
        FilterRegistrationBean<T> filterRegistrationBean = new FilterRegistrationBean<>();

        beanFactory.autowireBean(filter);
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns(urlPatterns);
        return filterRegistrationBean;
    }

    private int getFilterOrder(FilterOrder filterOrder) {
        int order = filterOrderProperties.getOrder(filterOrder);
        return getFilterOrder(order);
    }

    private int getFilterOrder(int order) {
        int securityFilterChainOrder = securityProperties.getFilter().getOrder();
        return securityFilterChainOrder + order;
    }
}

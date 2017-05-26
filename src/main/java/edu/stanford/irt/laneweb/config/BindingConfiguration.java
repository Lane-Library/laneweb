package edu.stanford.irt.laneweb.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.ipgroup.CIDRRange;
import edu.stanford.irt.laneweb.servlet.binding.ActiveSunetidDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BasePathDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BaseProxyURLDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BooleanSessionParameterDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.ContentBaseDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.servlet.binding.DisasterModeDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.LiveChatScheduleBinder;
import edu.stanford.irt.laneweb.servlet.binding.LoginExpirationCookieDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.ModelDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.ParameterMapDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.ProxyLinks;
import edu.stanford.irt.laneweb.servlet.binding.RemoteProxyIPDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestHeaderDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestMethodDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.RequestParameterDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.StringSessionParameterDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.TemplateChooser;
import edu.stanford.irt.laneweb.servlet.binding.TemplateDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.TicketDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.TodayDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.TodaysHoursBinder;
import edu.stanford.irt.laneweb.servlet.binding.UnividDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.VersionDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.user.CookieUserFactory;
import edu.stanford.irt.laneweb.servlet.binding.user.RequestAttributeUserFactory;
import edu.stanford.irt.laneweb.servlet.binding.user.UserFactory;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.libraryhours.LibraryHoursService;

@Configuration
public class BindingConfiguration {

    @Autowired
    @Qualifier("edu.stanford.irt.laneweb.servlet.binding.DataBinder/bookmark")
    private DataBinder bookmarkDataBinder;

    private URL contentBase;

    @Value("${edu.stanford.irt.laneweb.disaster-mode}")
    private Boolean disasterMode;

    @Value("${edu.stanford.irt.laneweb.ezproxy-key}")
    private String ezproxyKey;

    @Autowired
    private LDAPDataAccess ldapDataAccess;

    private LibraryHoursService libraryHoursService;

    @Autowired
    private ObjectMapper objectMapper;

    private ServletContext servletContext;

    private String userCookieKey;

    @Value("${edu.stanford.irt.laneweb.useridhashkey}")
    private String userIdHashKey;

    private String version;

    @Autowired
    public BindingConfiguration(@Value("${edu.stanford.irt.laneweb.useridcookiecodec.key}") final String userCookieKey,
            @Value("${edu.stanford.irt.laneweb.live-base}") final URL contentBase,
            @Value("${edu.stanford.irt.laneweb.version}") final String version,
            final ServletContext servletContext,
            final LibraryHoursService libraryHoursService) {
        this.userCookieKey = userCookieKey;
        this.contentBase = contentBase;
        this.version = version;
        this.servletContext = servletContext;
        this.libraryHoursService = libraryHoursService;
    }

    @Bean
    public DataBinder activeSunetidDataBinder() {
        return new ActiveSunetidDataBinder(this.ldapDataAccess);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/base-path")
    public DataBinder basePathDataBinder() {
        return new BasePathDataBinder(this.servletContext);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/base-proxy-url")
    public DataBinder baseProxyUrlDataBinder() {
        return new BaseProxyURLDataBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/content-base")
    public DataBinder contentBaseDataBinder() {
        return new ContentBaseDataBinder(this.contentBase);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder")
    public DataBinder dataBinder() {
        List<DataBinder> dataBinders = new ArrayList<>(22);
        dataBinders.add(userDataBinder());
        dataBinders.add(activeSunetidDataBinder());
        dataBinders.add(ticketDataBinder());
        dataBinders.add(debugDataBinder());
        dataBinders.add(remoteProxyIPDataBinder());
        dataBinders.add(templateDataBinder());
        dataBinders.add(emridDataBinder());
        dataBinders.add(requestParameterDataBinder());
        dataBinders.add(requestMethodDataBinder());
        dataBinders.add(requestHeaderDataBinder());
        dataBinders.add(userCookieDataBinder());
        dataBinders.add(basePathDataBinder());
        dataBinders.add(contentBaseDataBinder());
        dataBinders.add(versionDataBinder());
        dataBinders.add(disasterModeDataBinder());
        dataBinders.add(liveChatScheduleDataBinder());
        dataBinders.add(this.bookmarkDataBinder);
        dataBinders.add(todaysHoursDataBinder());
        dataBinders.add(parameterMapDataBinder());
        dataBinders.add(baseProxyUrlDataBinder());
        dataBinders.add(modelDataBinder());
        dataBinders.add(todayDataBinder());
        return new CompositeDataBinder(dataBinders);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/debug")
    public DataBinder debugDataBinder() {
        return new BooleanSessionParameterDataBinder("debug", "debug");
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/disaster-mode")
    public DataBinder disasterModeDataBinder() {
        return new DisasterModeDataBinder(this.disasterMode);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/emrid")
    public DataBinder emridDataBinder() {
        return new StringSessionParameterDataBinder("emrid", "emrid");
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/live-chat-schedule")
    public DataBinder liveChatScheduleDataBinder() {
        return new LiveChatScheduleBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/model")
    public DataBinder modelDataBinder() {
        Set<String> keys = new HashSet<>();
        keys.add("auth");
        keys.add("base-path");
        keys.add("disaster-mode");
        keys.add("isActiveSunetID");
        keys.add("ipgroup");
        keys.add("proxy-links");
        keys.add("url-encoded-query");
        keys.add("url-encoded-source");
        return new ModelDataBinder(keys, this.objectMapper);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/parameter-map")
    public DataBinder parameterMapDataBinder() {
        return new ParameterMapDataBinder();
    }

    @Bean
    public ProxyLinks proxyLinks() {
        List<CIDRRange> proxyCIDRRange = new ArrayList<>();
        // VPN 171.66.16.0 - 171.66.31.255
        proxyCIDRRange.add(new CIDRRange("171.66.16.0/20"));
        // hospital 171.65.44.0 - 171.65.44.255
        proxyCIDRRange.add(new CIDRRange("171.65.44.0/24"));
        // hospital 171.65.125.0 - 171.65.125.255
        proxyCIDRRange.add(new CIDRRange("171.65.125.0/24"));
        // hospital 171.65.128.0 - 171.65.255.255
        proxyCIDRRange.add(new CIDRRange("171.65.128.0/17"));
        List<CIDRRange> noProxyCIDRRange = new ArrayList<>();
        //
        noProxyCIDRRange.add(new CIDRRange("171.64.0.0/14"));
        // 10.34.0.0 - 10.35.255.255 Wireless NAT range for clients registered in NetDB connected to the "Stanford" SSID
        // in academic buildings
        noProxyCIDRRange.add(new CIDRRange("10.34.0.0/15"));
        // 10.39.0.0 - 10.39.255.255 Wireless NAT range for School of Medicine
        noProxyCIDRRange.add(new CIDRRange("10.39.0.0/16"));
        // 10.38.0.0 - 10.38.127.25 Wireless NAT range for "Stanford Secure" SSID for School of Medicine
        noProxyCIDRRange.add(new CIDRRange("10.38.0.0/17"));
        // 10.98.0.0 - 10.99.255.255 Wireless NAT range for "Stanford Secure" SSID in academic buildings
        noProxyCIDRRange.add(new CIDRRange("10.98.0.0/15"));
        // 10.128.0.0 - 10.135.255.255 Wireless NAT range for "Stanford Secure" SSID for Student Residences
        noProxyCIDRRange.add(new CIDRRange("10.128.0.0/13"));
        return new ProxyLinks(proxyCIDRRange, noProxyCIDRRange);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/remote-proxy-ip")
    public DataBinder remoteProxyIPDataBinder() {
        RemoteProxyIPDataBinder dataBinder = new RemoteProxyIPDataBinder();
        dataBinder.setProxyLinks(proxyLinks());
        return dataBinder;
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/request-header")
    public DataBinder requestHeaderDataBinder() {
        return new RequestHeaderDataBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/request-method")
    public DataBinder requestMethodDataBinder() {
        return new RequestMethodDataBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/request-parameter")
    public DataBinder requestParameterDataBinder() {
        return new RequestParameterDataBinder();
    }

    @Bean
    public TemplateChooser templateChooser() {
        Set<String> templateNames = new HashSet<>();
        templateNames.add("template");
        templateNames.add("bassettLargerView");
        templateNames.add("none");
        templateNames.add("history");
        Map<String, String> templateMap = new HashMap<>();
        templateMap.put("^/biomed-resources/bassett/raw/bassettLargerView.html", "bassettLargerView");
        templateMap.put("^/discoveryLoginPage.html", "none");
        templateMap.put("^/devDiscoveryLoginPage.html", "none");
        templateMap.put("^/help/live-chat-only.html", "none");
        return new TemplateChooser("template", templateNames, templateMap);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/template")
    public DataBinder templateDataBinder() {
        return new TemplateDataBinder(templateChooser());
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/ticket")
    public DataBinder ticketDataBinder() {
        return new TicketDataBinder(this.ezproxyKey);
    }

    @Bean
    public TodayDataBinder todayDataBinder() {
        return new TodayDataBinder();
    }

    @Bean
    public TodaysHours todaysHours() {
        return new TodaysHours(this.libraryHoursService);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/todays-hours")
    public DataBinder todaysHoursDataBinder() {
        return new TodaysHoursBinder(todaysHours());
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/univid")
    public DataBinder unividDataBinder() {
        return new UnividDataBinder();
    }

    @Bean
    public UserCookieCodec userCookieCodec() {
        return new UserCookieCodec(this.userCookieKey);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/user-cookie")
    public DataBinder userCookieDataBinder() {
        return new LoginExpirationCookieDataBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/user")
    public DataBinder userDataBinder() {
        List<UserFactory> userFactories = new ArrayList<>();
        userFactories.add(new RequestAttributeUserFactory(this.userIdHashKey));
        userFactories.add(new CookieUserFactory(userCookieCodec(), this.userIdHashKey));
        return new UserDataBinder(userFactories);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/userdata")
    public DataBinder userDataDataBinder() {
        List<DataBinder> dataBinders = new ArrayList<>();
        dataBinders.add(userDataBinder());
        dataBinders.add(activeSunetidDataBinder());
        dataBinders.add(ticketDataBinder());
        dataBinders.add(remoteProxyIPDataBinder());
        dataBinders.add(emridDataBinder());
        dataBinders.add(basePathDataBinder());
        dataBinders.add(disasterModeDataBinder());
        dataBinders.add(this.bookmarkDataBinder);
        dataBinders.add(baseProxyUrlDataBinder());
        return new CompositeDataBinder(dataBinders);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/version")
    public DataBinder versionDataBinder() {
        return new VersionDataBinder(this.version);
    }
}

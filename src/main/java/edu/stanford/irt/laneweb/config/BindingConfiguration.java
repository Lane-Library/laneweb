package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.ipgroup.CIDRRange;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.ActiveSunetidDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BasePathDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BaseProxyURLDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BaseURIDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.BooleanSessionParameterDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
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

    @Bean
    public ActiveSunetidDataBinder activeSunetidDataBinder(final LDAPDataAccess ldapDataAccess) {
        return new ActiveSunetidDataBinder(ldapDataAccess);
    }

    @Bean
    public BasePathDataBinder basePathDataBinder(final ServletContext servletContext) {
        return new BasePathDataBinder(servletContext);
    }

    @Bean
    public BaseProxyURLDataBinder baseProxyUrlDataBinder() {
        return new BaseProxyURLDataBinder();
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/cme")
    public DataBinder cmeDataBinder(final UserDataBinder userDataBinder, final BasePathDataBinder basePathDataBinder) {
        List<DataBinder> dataBinders = new ArrayList<>(4);
        dataBinders.add(userDataBinder);
        dataBinders.add(basePathDataBinder);
        dataBinders.add(emridDataBinder());
        dataBinders.add(remoteProxyIPDataBinder());
        return new CompositeDataBinder(dataBinders);
    }

    @Bean
    public BaseURIDataBinder contentBaseDataBinder(
            @Value("${edu.stanford.irt.laneweb.live-base}") final URI contentBase) {
        return new BaseURIDataBinder(Model.CONTENT_BASE, contentBase);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder")
    public DataBinder dataBinder(final UserDataBinder userDataBinder,
            final ActiveSunetidDataBinder activeSunetidDataBinder, final TicketDataBinder ticketDataBinder,
            final BookmarkDataBinder bookmarkDataBinder, final BasePathDataBinder basePathDataBinder,
            final BaseURIDataBinder contentBaseDataBinder, final VersionDataBinder versionDataBinder,
            final DisasterModeDataBinder disasterModeDataBinder, final TodaysHoursBinder todaysHoursDataBinder,
            final ModelDataBinder modelDataBinder) {
        List<DataBinder> dataBinders = new ArrayList<>(22);
        dataBinders.add(userDataBinder);
        dataBinders.add(activeSunetidDataBinder);
        dataBinders.add(ticketDataBinder);
        dataBinders.add(new BooleanSessionParameterDataBinder("debug", "debug"));
        dataBinders.add(remoteProxyIPDataBinder());
        dataBinders.add(templateDataBinder());
        dataBinders.add(emridDataBinder());
        dataBinders.add(new RequestParameterDataBinder());
        dataBinders.add(new RequestMethodDataBinder());
        dataBinders.add(requestHeaderDataBinder());
        dataBinders.add(new LoginExpirationCookieDataBinder());
        dataBinders.add(basePathDataBinder);
        dataBinders.add(contentBaseDataBinder);
        dataBinders.add(versionDataBinder);
        dataBinders.add(disasterModeDataBinder);
        dataBinders.add(new LiveChatScheduleBinder());
        dataBinders.add(bookmarkDataBinder);
        dataBinders.add(todaysHoursDataBinder);
        dataBinders.add(new ParameterMapDataBinder());
        dataBinders.add(baseProxyUrlDataBinder());
        dataBinders.add(modelDataBinder);
        dataBinders.add(new TodayDataBinder());
        return new CompositeDataBinder(dataBinders);
    }

    @Bean
    public DisasterModeDataBinder disasterModeDataBinder(
            @Value("${edu.stanford.irt.laneweb.disaster-mode}") final Boolean disasterMode) {
        return new DisasterModeDataBinder(disasterMode);
    }

    @Bean
    public DataBinder emridDataBinder() {
        return new StringSessionParameterDataBinder("emrid", "emrid");
    }

    @Bean
    public ModelDataBinder modelDataBinder(final ObjectMapper objectMapper) {
        Set<String> keys = new HashSet<>();
        keys.add("auth");
        keys.add("base-path");
        keys.add("disaster-mode");
        keys.add("isActiveSunetID");
        keys.add("ipgroup");
        keys.add("proxy-links");
        keys.add("url-encoded-query");
        keys.add("url-encoded-source");
        return new ModelDataBinder(keys, objectMapper);
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

    @Bean
    public RemoteProxyIPDataBinder remoteProxyIPDataBinder() {
        return new RemoteProxyIPDataBinder(proxyLinks());
    }

    @Bean
    public RequestHeaderDataBinder requestHeaderDataBinder() {
        return new RequestHeaderDataBinder();
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
        templateMap.put("^/contacts/live-chat.html", "none");
        return new TemplateChooser("template", templateNames, templateMap);
    }

    @Bean
    public TemplateDataBinder templateDataBinder() {
        return new TemplateDataBinder(templateChooser());
    }

    @Bean
    public TicketDataBinder ticketDataBinder(
            @Value("${edu.stanford.irt.laneweb.ezproxy-key}") final String ezproxyKey) {
        return new TicketDataBinder(ezproxyKey);
    }

    @Bean
    public TodaysHours todaysHours(final LibraryHoursService libraryHoursService) {
        return new TodaysHours(libraryHoursService);
    }

    @Bean
    public TodaysHoursBinder todaysHoursDataBinder(final TodaysHours todaysHours) {
        return new TodaysHoursBinder(todaysHours);
    }

    @Bean
    public UnividDataBinder unividDataBinder() {
        return new UnividDataBinder();
    }

    @Bean
    public UserCookieCodec userCookieCodec(
            @Value("${edu.stanford.irt.laneweb.useridcookiecodec.key}") final String userCookieKey) {
        return new UserCookieCodec(userCookieKey);
    }

    @Bean
    public UserDataBinder userDataBinder(@Value("${edu.stanford.irt.laneweb.useridhashkey}") final String userIdHashKey,
            final UserCookieCodec userCookieCodec) {
        List<UserFactory> userFactories = new ArrayList<>();
        userFactories.add(new RequestAttributeUserFactory(userIdHashKey));
        userFactories.add(new CookieUserFactory(userCookieCodec, userIdHashKey));
        return new UserDataBinder(userFactories);
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/userdata")
    public DataBinder userDataDataBinder(final UserDataBinder userDataBinder,
            final ActiveSunetidDataBinder activeSunetidDataBinder, final TicketDataBinder ticketDataBinder,
            final BasePathDataBinder basePathDataBinder, final DisasterModeDataBinder disasterModeDataBinder,
            final BookmarkDataBinder bookmarkDataBinder) {
        List<DataBinder> dataBinders = new ArrayList<>();
        dataBinders.add(userDataBinder);
        dataBinders.add(activeSunetidDataBinder);
        dataBinders.add(ticketDataBinder);
        dataBinders.add(remoteProxyIPDataBinder());
        dataBinders.add(emridDataBinder());
        dataBinders.add(basePathDataBinder);
        dataBinders.add(disasterModeDataBinder);
        dataBinders.add(bookmarkDataBinder);
        dataBinders.add(baseProxyUrlDataBinder());
        return new CompositeDataBinder(dataBinders);
    }

    @Bean
    public VersionDataBinder versionDataBinder(@Value("${edu.stanford.irt.laneweb.version}") final String version) {
        return new VersionDataBinder(version);
    }
}

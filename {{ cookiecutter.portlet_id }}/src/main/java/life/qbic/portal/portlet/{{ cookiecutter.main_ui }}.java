package life.qbic.portal.portlet;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.WrappedPortletSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import life.qbic.portal.utils.PortalUtils;

/**
 * Entry point for portlet {{ cookiecutter.portlet_id }}. This class derives from {@link QBiCPortletUI}, which is found in the {@code portal-utils-lib} library.
 */
@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.portlet.AppWidgetSet")
public class {{ cookiecutter.main_ui }} extends QBiCPortletUI {

    private static final Logger LOG = LogManager.getLogger({{ cookiecutter.main_ui }}.class);

    @Override
    protected Layout getPortletContent(final VaadinRequest request) {
        LOG.info("Generating content for portlet {{ cookiecutter.portlet_id }}");
        // TODO: build the content for your portlet, this is just some sample code

        final StringBuilder builder = new StringBuilder();
        if (PortalUtils.isLiferayPortlet()) {
            builder.append("Hello, ").append(PortalUtils.getUser().getScreenName()).append("!<br/>");
            builder.append("This is portlet ").append(getPortletContextName(request)).append(".<br/>");
            builder.append("This portal has ").append(getPortalCountOfRegisteredUsers()).append(" registered users (according to the data returned by Liferay's API call)");            
        } else {
            builder.append("You are currently in a local testing mode. No Liferay Portlet context found.<br/>");            
        }
        builder.append("<br/>You can now start developing. Start by modifying the <font face='monospace'>getPortletContent</font> method in the generated <font face='monospace'>src/mainLayout/java/life/qbic/portlet/{{ cookiecutter.main_ui}}.java</font> file.<br/><br/>");
        builder.append("<hr width='100%'/><br/>This is just a simple logging test. Enter some text in the text field, select a log level and then click on the button to see <i>log4j2</i> in action (configuration taken from: <font face='monospace'>src/resources/log4j2.xml</font>).");
        builder.append("<br>Note: <font face='monospace'>ERROR</font> and <font face='monospace'>FATAL</font> log levels will also log an exception.");

        LOG.info("These are the variables you provided to generate this portlet:");            
        // generated by cookiecutter
        {%- for key, value in cookiecutter.items() -%}
        LOG.info("\"{{ key }}\": \"{{ value }}\"<br/>");
        {%- endfor %}
        // --
        final Label welcomeLabel = new Label(builder.toString(), ContentMode.HTML);

        final TextField logTextField = new TextField();
        logTextField.setDescription("Write something to log");
        logTextField.setMaxLength(120);
        logTextField.setWidth("500px");

        final ComboBox logLevelComboBox = new ComboBox();
        logLevelComboBox.setImmediate(true);
        logLevelComboBox.setNullSelectionAllowed(false);
		logLevelComboBox.setDescription("Log level");
        logLevelComboBox.setInputPrompt("Log level");
        logLevelComboBox.addItems("TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL");
        logLevelComboBox.setValue("INFO");
        
        final Button logButton = new Button("Log!");
        // Confused about "->"? Check out https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
        logButton.addClickListener((Button.ClickListener) event -> {
            final String logEntry = logTextField.getValue();
            switch((String)logLevelComboBox.getValue()) {
                // yeah, it could be done via reflection, but this is just a template
                case "TRACE":
                    LOG.trace(logEntry);
                    break;
                case "DEBUG":
                    LOG.debug(logEntry);
                    break;
                case "INFO":
                    LOG.info(logEntry);
                    break;
                case "WARN":
                    LOG.warn(logEntry);
                    break;
                case "ERROR":
                case "FATAL":
                    LOG.fatal(logEntry, new RuntimeException("DO NOT PANIC! This is just a demo of how stack traces are logged"));
                    break;
                default:
                    LOG.warn("Invalid log level selected!");
            }
        });

        final HorizontalLayout logLayout = new HorizontalLayout();
        logLayout.setSpacing(true);
        logLayout.setMargin(true);
        logLayout.addComponent(logTextField);
        logLayout.addComponent(logLevelComboBox);
        logLayout.addComponent(logButton);

        final VerticalLayout panelContent = new VerticalLayout();
        panelContent.setSpacing(true);
        panelContent.setMargin(true);
        panelContent.addComponent(welcomeLabel);
        panelContent.addComponent(logLayout);

        final Panel mainPanel = new Panel("{{ cookiecutter.display_name }}");
        mainPanel.setContent(panelContent);
            
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.addComponent(mainPanel);

        return mainLayout;
    }

    private String getPortletContextName(VaadinRequest request) {
        WrappedPortletSession wrappedPortletSession = (WrappedPortletSession) request
                .getWrappedSession();
        PortletSession portletSession = wrappedPortletSession
                .getPortletSession();

        final PortletContext context = portletSession.getPortletContext();
        final String portletContextName = context.getPortletContextName();
        return portletContextName;
    }

    private Integer getPortalCountOfRegisteredUsers() {
        Integer result = null;

        try {
            result = UserLocalServiceUtil.getUsersCount();
        } catch (Exception e) {
            LOG.warn("Could not get user count", e);
        }

        return result;
    }
}
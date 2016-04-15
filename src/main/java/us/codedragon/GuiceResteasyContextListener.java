package us.codedragon;

import com.google.inject.Injector;
import org.jboss.resteasy.plugins.guice.ModuleProcessor;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GuiceResteasyContextListener extends ResteasyBootstrap implements ServletContextListener {
	@Inject
	private Injector injector = null;

	public GuiceResteasyContextListener() {
	}

	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext context = event.getServletContext();
		Registry registry = (Registry) context.getAttribute(Registry.class.getName());
		ResteasyProviderFactory providerFactory = (ResteasyProviderFactory) context.getAttribute(ResteasyProviderFactory.class.getName());
		ModuleProcessor processor = new ModuleProcessor(registry, providerFactory);
		processor.processInjector(injector);
		while (injector.getParent() != null) {
			injector = injector.getParent();
			processor.processInjector(injector);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}

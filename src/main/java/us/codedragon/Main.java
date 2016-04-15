package us.codedragon;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.util.ImmediateInstanceHandle;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static Undertow undertow;
	private static SchedulerFactory schedulerFactory;

	public static void main(String[] args) throws Exception {
		start(args);
	}

	public static void start(String[] args) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("Uncaught exception", e));
		Injector injector = Guice.createInjector(new MyAbstractModule());
		DeploymentManager manager = Servlets.defaultContainer().addDeployment(Servlets.deployment()
				.setClassLoader(ClassLoader.getSystemClassLoader())
				.setContextPath("/").setDeploymentName("Name")
				.addListener(Servlets.listener(GuiceResteasyContextListener.class, () -> new ImmediateInstanceHandle<>(injector.getInstance(GuiceResteasyContextListener.class))))
				.addServlet(
						Servlets.servlet("ResteaasyServlet", HttpServlet30Dispatcher.class)
								.setAsyncSupported(true)
								.setLoadOnStartup(Integer.valueOf(1))
								.addMapping("/*"))
				.addInitParameter("resteasy.servlet.mapping.prefix", "/*"));
		manager.deploy();
		Undertow undertow = Undertow.builder().addHttpListener(9998, "localhost").setHandler(manager.start()).build();
		undertow.start();

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.setJobFactory(injector.getInstance(GuiceJobFactory.class));

		scheduler.start();

	}

	public static void stop(String[] args) {
	}

	private static class MyAbstractModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(String.class).annotatedWith(Names.named("Test")).toInstance("Hello!");
			bind(SchedulerFactory.class).toInstance(null);
			bind(GuiceResteasyContextListenerInstanceHandleFactory.class);
		}
	}
}

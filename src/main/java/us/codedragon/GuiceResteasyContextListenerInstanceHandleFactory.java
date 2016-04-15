package us.codedragon;

import com.google.inject.Injector;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;

import javax.inject.Inject;


public class GuiceResteasyContextListenerInstanceHandleFactory implements InstanceFactory<GuiceResteasyContextListener> {
	@Inject
	private Injector injector;
	@Override
	public InstanceHandle<GuiceResteasyContextListener> createInstance() throws InstantiationException {
		return new ImmediateInstanceHandle<>(injector.getInstance(GuiceResteasyContextListener.class));
	}
}

package us.codedragon;

import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import javax.inject.Inject;

final class GuiceJobFactory implements JobFactory {
	@Inject
	private Injector guice;

	@Override
	public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
		return guice.getInstance(triggerFiredBundle.getJobDetail().getJobClass());
	}
}


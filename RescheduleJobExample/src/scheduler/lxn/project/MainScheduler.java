package scheduler.lxn.project;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class MainScheduler {

	public static void main(String[] args) throws SchedulerException, InterruptedException{
		JobDetail job = JobBuilder.newJob(MyJob.class).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CroneTrigger")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(02).repeatForever())
				.build();
		Scheduler schedule = StdSchedulerFactory.getDefaultScheduler();
		schedule.start();
		schedule.scheduleJob(job, trigger);
		TimeUnit.SECONDS.sleep(3);
		rescheduleTrigger();
	}

	private static void rescheduleTrigger() throws SchedulerException {
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the new time");
		int time=sc.nextInt();
		// Define a new Trigger
		Trigger trigger = TriggerBuilder.newTrigger()
		    .withIdentity("newTrigger", "group1")
		    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(time).repeatForever())
		    .startNow()
		    .build();
		
		Scheduler schedule = StdSchedulerFactory.getDefaultScheduler();
		// tell the scheduler to remove the old trigger with the given key, and put the new one in its place
		schedule.rescheduleJob(TriggerKey.triggerKey("CroneTrigger"), trigger);
	}

}

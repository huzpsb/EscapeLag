package com.mcml.space.monitor.inject;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.mcml.space.config.ConfigFunction;
import com.mcml.space.monitor.MonitorUtils;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Reflection;
import com.mcml.space.util.Reflection.FieldAccessor;

public class SchedulerTaskInjector extends AbstractInjector implements Runnable {
	/**
	 * 
	 * @author jiongjionger,Vlvxingze
	 */

	// 替换原本的Runnable为带性能统计的版本
	public static void inject(Plugin plg) {
		if (plg != null) {
			for (BukkitTask pendingTask : Bukkit.getScheduler().getPendingTasks()) {
				if (pendingTask.isSync() && pendingTask.getOwner().equals(plg)) {
					try {
						FieldAccessor<Runnable> field = Reflection.getField(pendingTask.getClass(), "task", Runnable.class);
						field.set(pendingTask, new SchedulerTaskInjector(plg, field.get(pendingTask)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 替换带性能统计版本的Runnable为原版本
	public static void uninject(Plugin plg) {
		if (plg != null) {
			for (BukkitTask pendingTask : Bukkit.getScheduler().getPendingTasks()) {
				if (pendingTask.isSync() && pendingTask.getOwner().equals(plg)) {
					try {
						FieldAccessor<Runnable> field = Reflection.getField(pendingTask.getClass(), "task", Runnable.class);
						Runnable runnable = field.get(pendingTask);
						if (runnable instanceof SchedulerTaskInjector) {
							field.set(pendingTask, ((SchedulerTaskInjector) runnable).getRunnable());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private final Runnable runnable;

	private long totalCount = 0L;
	private long totalTime = 0L;
	private long maxExecuteTime = 0L;

	public SchedulerTaskInjector(Plugin plugin, Runnable runnable) {
		super(plugin);
		this.runnable = runnable;
	}

	public long getAvgExecuteTime() {
		if (this.totalCount == 0) {
			return 0;
		}
		return this.totalTime / this.totalCount;
	}

	public long getMaxExecuteTime() {
		return this.maxExecuteTime;
	}

	public Runnable getRunnable() {
		return this.runnable;
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public long getTotalTime() {
		return this.totalTime;
	}

	// 统计定时任务的耗时、次数、最大耗时
	@Override
	public void run() {
		long startTime = System.nanoTime();
		try {
			try{
				this.runnable.run();
			}catch(Throwable ex){
				MonitorUtils.AExceptionCatcher(plugin, ex);
			}
		} finally {
			long endTime = System.nanoTime();
			long useTime = endTime - startTime;
			if(ConfigFunction.MonitorPluginLagWarningenable){
				if(useTime/1000000 > ConfigFunction.MonitorPluginLagWarningPeriod && this.getPlugin().getName().equalsIgnoreCase("EscapeLag") == false){
					AzureAPI.log("警告！服务器主线程陷入停顿超过配置设定值！因为插件" + this.getPlugin().getName() + " 执行了一次耗时 " + useTime/1000000 + " 毫秒的位于 " + this.getRunnable().getClass().getName() + " 的任务 " + this.getRunnable().toString() + " 的操作！");
				}
			}
			if (useTime > this.maxExecuteTime) {
				this.maxExecuteTime = useTime;
			}
			this.totalTime += useTime;
			this.totalCount += 1L;
		}
	}

}

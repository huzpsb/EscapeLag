package com.mcml.space.monitor.inject;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Reflection;
import com.mcml.space.util.Reflection.FieldAccessor;

public class EventExecutorInjector extends AbstractInjector implements EventExecutor {
	/**
	 * 
	 * @author jiongjionger,Vlvxingze
	 */

	// 将监听器原本的EventExecutor替换成带性能统计的版本
	public static void inject(Plugin plg) {
		if (plg != null) {
			for (RegisteredListener listener : HandlerList.getRegisteredListeners(plg)) {
				try {
					FieldAccessor<EventExecutor> field = Reflection.getField(RegisteredListener.class, "executor", EventExecutor.class);
					EventExecutor fieldEventExecutor = field.get(listener);
					field.set(listener, new EventExecutorInjector(plg, fieldEventExecutor));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 将监听器带性能统计的版本替换回原始的EventExecutor版本
	public static void uninject(Plugin plg) {
		if (plg != null) {
			for (RegisteredListener listener : HandlerList.getRegisteredListeners(plg)) {
				try {
					FieldAccessor<EventExecutor> field = Reflection.getField(RegisteredListener.class, "executor", EventExecutor.class);
					EventExecutor executor = field.get(listener);
					if (executor instanceof EventExecutorInjector) {
						field.set(listener, ((EventExecutorInjector) executor).getEventExecutor());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private final EventExecutor eventExecutor;
	private String eventName;
	private long totalCount = 0L;

	private long totalTime = 0L;

	private long maxExecuteTime = 0L;

	public EventExecutorInjector(Plugin plugin, EventExecutor eventExecutor) {
		super(plugin);
		this.eventExecutor = eventExecutor;
	}

	@Override
	// 计算调用次数和花费总时间以及花费最多的时间
	public void execute(Listener listener, Event e) throws EventException {
		if (e.isAsynchronous()) {
			this.eventExecutor.execute(listener, e);
		} else {
			long startTime = System.nanoTime();
			try {
				try{
					this.eventExecutor.execute(listener, e);
				}catch(Throwable ex){
					AzureAPI.log("警告！插件 " + this.getPlugin().getName() + " 出错，刷错信息为：");
					StackTraceElement[] stes = ex.getStackTrace();
					int stesl = stes.length;
					for(int i = 0;i<stesl;i++){
						StackTraceElement ste = stes[i];
						String stestring = ste.toString();
						if(stestring.contains("com.mcml.space.monitor") == false){
							System.out.println(stestring);
						}else{
							if(this.getPlugin().getName().equalsIgnoreCase("EscapeLag")){
								System.out.println(ste.toString());
							}
						}
					}
				}
			} finally {
				long endTime = System.nanoTime();
				long executeTime = endTime - startTime;
				if(ConfigOptimize.MonitorPluginLagWarningenable){
					if(executeTime/1000000 > ConfigOptimize.MonitorPluginLagWarningPeriod){
						AzureAPI.log("警告！服务器主线程陷入停顿超过配置设定值！因为插件" + this.getPlugin().getName() + " 执行了一次耗时 " + executeTime/1000000 + " 毫秒的位于 " + listener.getClass().getName() + " 的监听器 " + e.getEventName() + " 的操作！");
					}
				}
				this.record(e.getEventName(), executeTime);
			}
		}
	}

	public long getAvgExecuteTime() {
		if (this.totalCount == 0) {
			return 0;
		}
		return this.totalTime / this.totalCount;
	}

	// 获取原本的EventExecutor
	public EventExecutor getEventExecutor() {
		return this.eventExecutor;
	}

	public String getEventName() {
		return this.eventName;
	}

	public long getMaxExecuteTime() {
		return this.maxExecuteTime;
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public long getTotalTime() {
		return this.totalTime;
	}

	private void record(String eventName, long executeTime) {
		if (this.eventName == null) {
			this.eventName = eventName;
		}
		this.totalTime += executeTime;
		this.totalCount += 1L;
		if (executeTime > this.maxExecuteTime) {
			this.maxExecuteTime = executeTime;
		}
	}
}

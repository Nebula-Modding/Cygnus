package top.girlkisser.cygnus.foundation;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TickScheduler
{
	public static final TickScheduler SERVER = new TickScheduler();

	private final List<Pair<AtomicInteger, Runnable>> scheduled = new ArrayList<>();

	public void tick()
	{
		scheduled.forEach(it -> {
			if (it.getFirst().decrementAndGet() <= 0)
				it.getSecond().run();
		});
		scheduled.removeIf(it -> it.getFirst().get() <= 0);
	}

	public void scheduleWork(int inTicks, Runnable runnable)
	{
		scheduled.add(new Pair<>(new AtomicInteger(inTicks), runnable));
	}
}

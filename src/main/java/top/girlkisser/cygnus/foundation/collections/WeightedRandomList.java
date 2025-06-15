package top.girlkisser.cygnus.foundation.collections;

import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;

// Mojang does have a WeightedRandomList implementation, however theirs uses an
// immutable list. This one uses a mutable list, which makes it a little more
// flexible.
public class WeightedRandomList<T extends WeightedRandomList.IWeighted>
{
	protected final List<Entry<T>> entries;
	protected int accumulatedWeight;

	public WeightedRandomList(List<T> entries)
	{
		this.entries = new ArrayList<>();
		for (T element : entries)
			this.addEntry(element);
	}

	public List<Entry<T>> getEntries()
	{
		return entries;
	}

	public T pick(RandomSource random)
	{
		double r = random.nextInt(accumulatedWeight);
		for (var entry : entries)
		{
			if (entry.accumulatedWeight > r)
			{
				return entry.it;
			}
		}
		return null; // this should only occur when the list is empty
	}

	public void addEntry(T it)
	{
		accumulatedWeight += it.weight();
		entries.add(new Entry<>(it, accumulatedWeight));
	}

	public record Entry<T>(T it, int accumulatedWeight)
	{
	}

	public interface IWeighted
	{
		int weight();
	}
}

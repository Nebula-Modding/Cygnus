package top.girlkisser.cygnus.foundation.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A string codec that only allows specified values.
 */
public record LimitedStringCodec(Set<String> values) implements Codec<String>
{
	@Override
	public <T> DataResult<Pair<String, T>> decode(DynamicOps<T> ops, T input)
	{
		DataResult<String> v = ops.getStringValue(input);
		if (v.isSuccess())
			if (values.contains(v.getOrThrow()))
				return DataResult.success(new Pair<>(v.getOrThrow(), input));
			else
				return DataResult.error(() -> "Invalid value: " + v.getOrThrow() + ". Allowed values are " + values);
		else
			return DataResult.error(v.error().orElseThrow().messageSupplier());
	}

	@Override
	public <T> DataResult<T> encode(String input, DynamicOps<T> ops, T prefix)
	{
		return DataResult.success(ops.createString(input));
	}

	public static LimitedStringCodec limitedStringCodec(Set<String> values)
	{
		return new LimitedStringCodec(values);
	}

	public static <T extends Enum<T>> Codec<T> enumStringCodec(T[] values, Function<String, T> valueOf)
	{
		Set<String> set = Arrays.stream(values).map(it -> it.name().toLowerCase()).collect(Collectors.toSet());
		return new LimitedStringCodec(set).xmap(it -> valueOf.apply(it.toUpperCase()), Enum::name);
	}
}

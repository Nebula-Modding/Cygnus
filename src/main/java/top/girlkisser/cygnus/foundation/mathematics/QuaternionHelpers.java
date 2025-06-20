package top.girlkisser.cygnus.foundation.mathematics;

import net.minecraft.util.Mth;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public final class QuaternionHelpers
{
	private QuaternionHelpers()
	{
	}

	/**
	 * Creates a {@link Quaterniond} using the provided roll (X), pitch (Y), and yaw (Z).
	 * Angles are in radians.
	 * <br/>
	 * Reference: <a href="https://en.m.wikipedia.org/wiki/Conversion_between_quaternions_and_Euler_angles">...</a>
	 *
	 * @param roll The roll in radians.
	 * @param pitch The pitch in radians.
	 * @param yaw The yaw in radians.
	 * @return A quaternion pointing in the provided direction.
	 */
	public static Quaterniond fromRollPitchYaw(double roll, double pitch, double yaw)
	{
		// Variable names are the abbreviations of the function, i.e, cosine of roll is `cr`
		double cr = Math.cos(roll * 0.5d);
		double sr = Math.sin(roll * 0.5d);
		double cp = Math.cos(pitch * 0.5d);
		double sp = Math.sin(pitch * 0.5d);
		double cy = Math.cos(yaw * 0.5d);
		double sy = Math.sin(yaw * 0.5d);

		Quaterniond q = new Quaterniond();
		q.w = cr * cp * cy + sr * sp * sy;
		q.x = sr * cp * cy - cr * sp * sy;
		q.y = cr * sp * cy + sr * cp * sy;
		q.z = cr * cp * sy - sr * sp * cy;
		return q;
	}

	public static Quaterniond fromRollPitchYawDegrees(double rollDegrees, double pitchDegrees, double yawDegrees)
	{
		return fromRollPitchYaw(rollDegrees * Mth.DEG_TO_RAD, pitchDegrees * Mth.DEG_TO_RAD, yawDegrees * Mth.DEG_TO_RAD);
	}

	public static Quaternionf castDoublesToFloats(Quaterniondc quaterniondc)
	{
		return new Quaternionf(
			quaterniondc.x(),
			quaterniondc.y(),
			quaterniondc.z(),
			quaterniondc.w()
		);
	}

	public static Quaterniond castFloatsToDoubles(Quaternionfc quaternionfc)
	{
		return new Quaterniond(
			quaternionfc.x(),
			quaternionfc.y(),
			quaternionfc.z(),
			quaternionfc.w()
		);
	}
}

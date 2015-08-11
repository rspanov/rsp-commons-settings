package org.rspanov.commons.settings;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.rspanov.commons.settings.ann.Setting;

/**
 * 
 * @author r_panov
 */
public class SampleStaticSettings {

	@Setting(description = "sample of AtomicLong")
	public static volatile AtomicLong SAMPLE_ATOMIC_LONG = new AtomicLong(100);

	@Setting(description = "sample of AtomicInteger")
	public static volatile AtomicInteger SAMPLE_ATOMIC_INTEGER = new AtomicInteger(400);

	@Setting(description = "sample of AtomicBoolean")
	public static volatile AtomicBoolean SAMPLE_ATOMIC_BOOL = new AtomicBoolean(true);

	@Setting(description = "sample string")
	public static volatile String SAMPLE_STRING = "Test string";

	@Setting(description = "sample of long")
	public static volatile long SAMPLE_LONG = 200L;

	@Setting(description = "sample of int")
	public static volatile int SAMPLE_INT = 300;

	@Setting(description = "sample of double")
	public static volatile double SAMPLE_DOUBLE = 4.0;

	@Setting(description = "sample of boolean")
	public static volatile boolean SAMPLE_BOOLEAN = true;

	@Setting(description = "sample of non-initialized AtomicLong")
	public static AtomicLong SAMPLE_NULL_ATOMIC_LONG;

	@Setting(description = "sample of non-initialized AtomicBoolean")
	public static AtomicBoolean SAMPLE_NULL_ATOMIC_BOOL;

	@Setting(description = "sample of non-initialized string")
	public static volatile String SAMPLE_NULL_STRING;

	@Setting(description = "sample of enum")
	public static volatile SampleEnum SAMPLE_ENUM = SampleEnum.ONE;

	@Setting(description = "sample of dinamic string")
	public volatile String SAMPLE_DYN_STANG = "dyn test";

}

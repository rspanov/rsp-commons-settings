package org.rspanov.commons.settings;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.rspanov.commons.settings.ann.Setting;

/**
 * 
 * @author r_panov
 */
public class SampleObjectSettings {

	@Setting(description = "sample of AtomicLong")
	private AtomicLong sampleAtomicLong = new AtomicLong(100);

	@Setting(description = "sample of AtomicInteger")
	public AtomicInteger sampleAtomicInteger = new AtomicInteger(400);

	@Setting(description = "sample of AtomicBoolean")
	private AtomicBoolean sampleAtomicBool = new AtomicBoolean(true);

	@Setting(description = "sample of string")
	private volatile String sampleString = "Test string";

	@Setting(description = "sample of long")
	private volatile long sampleLong = 200L;

	@Setting(description = "sample of int")
	private volatile int sampleInt = 300;

	@Setting(description = "sample of double")
	private volatile double sampleDouble = 4.0;

	@Setting(description = "sample of boolean")
	private volatile boolean sampleBoolean = true;

	@Setting(description = "sample of enum")
	public volatile SampleEnum sampleEnum = SampleEnum.ONE;

	public AtomicLong getSampleAtomicLong() {
		return sampleAtomicLong;
	}

	public AtomicBoolean getSampleAtomicBool() {
		return sampleAtomicBool;
	}

	public String getSampleString() {
		return sampleString;
	}

	public double getSampleDouble() {
		return sampleDouble;
	}

	public int getSampleInt() {
		return sampleInt;
	}

	public long getSampleLong() {
		return sampleLong;
	}

	public boolean isSampleBoolean() {
		return sampleBoolean;
	}

	public void setSampleAtomicBool(AtomicBoolean sampleAtomicBool) {
		this.sampleAtomicBool = sampleAtomicBool;
	}

	public void setSampleAtomicLong(AtomicLong sampleAtomicLong) {
		this.sampleAtomicLong = sampleAtomicLong;
	}

	public AtomicInteger getSampleAtomicInteger() {
		return sampleAtomicInteger;
	}

	public void setSampleAtomicInteger(AtomicInteger sampleAtomicInteger) {
		this.sampleAtomicInteger = sampleAtomicInteger;
	}

	public void setSampleString(String sampleString) {
		this.sampleString = sampleString;
	}

	public void setSampleBoolean(boolean sampleBoolean) {
		this.sampleBoolean = sampleBoolean;
	}

	public void setSampleDouble(double sampleDouble) {
		this.sampleDouble = sampleDouble;
	}

	public void setSampleInt(int sampleInt) {
		this.sampleInt = sampleInt;
	}

	public void setSampleLong(long sampleLong) {
		this.sampleLong = sampleLong;
	}

	public SampleEnum getSampleEnum() {
		return sampleEnum;
	}

	public void setSampleEnum(SampleEnum sampleEnum) {
		this.sampleEnum = sampleEnum;
	}

}

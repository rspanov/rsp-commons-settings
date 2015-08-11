package org.rspanov.commons.settings;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;

/**
 * 
 * @author r_panov
 */
public class PropertySettingStorageTest {

	private static String TEST_PROPERTIES_FILENAME = "sample-settings.properties";

	public PropertySettingStorageTest() {
	}

	@Test
	public void testObjectLoad() throws Exception {
		File f = new File(TEST_PROPERTIES_FILENAME);
		if (f.exists()) {
			f.delete();
		}
		PropertySettingStorage settingsStorage = new PropertySettingStorage(
				TEST_PROPERTIES_FILENAME);
		SampleObjectSettings settings = new SampleObjectSettings();

		settingsStorage.load(settings);

		assertTrue(settings.getSampleAtomicBool().get());
		assertEquals(100, settings.getSampleAtomicLong().get());
		assertEquals(400, settings.getSampleAtomicInteger().get());
		assertEquals("Test string", settings.getSampleString());
		assertEquals(200, settings.getSampleLong());
		assertEquals(300, settings.getSampleInt());
		assertTrue(settings.isSampleBoolean());
		assertEquals(4.0, settings.getSampleDouble());
		assertEquals(SampleEnum.ONE, settings.getSampleEnum());

		Properties props = new Properties();
		props.load(this.getClass().getResourceAsStream(
				"/" + TEST_PROPERTIES_FILENAME));
		FileWriter fw = new FileWriter(f);
		try {
			props.store(fw, "");
		} finally {
			fw.close();
		}

		settingsStorage.load(settings);

		assertFalse(settings.getSampleAtomicBool().get());
		assertEquals(200, settings.getSampleAtomicLong().get());
		assertEquals(400, settings.getSampleAtomicInteger().get());
		assertEquals("Modified test string",
				settings.getSampleString());
		assertEquals(201, settings.getSampleLong());
		assertEquals(301, settings.getSampleInt());
		assertFalse(settings.isSampleBoolean());
		assertEquals(5.1, settings.getSampleDouble());
		assertEquals(SampleEnum.TWO, settings.getSampleEnum());

		// test of values saving
		settings.getSampleAtomicLong().set(300);
		settings.getSampleAtomicInteger().set(550);
		settings.getSampleAtomicBool().set(true);
		settings.setSampleString("Test string 3");
		settings.setSampleBoolean(true);
		settings.setSampleLong(202);
		settings.setSampleInt(302);
		settings.setSampleDouble(6.2);
		settings.setSampleEnum(SampleEnum.THREE);

		settingsStorage.save(settings);

		SampleObjectSettings changedSettings = new SampleObjectSettings();
		settingsStorage.load(changedSettings);
		assertTrue(changedSettings.getSampleAtomicBool().get());
		assertEquals(300, changedSettings.getSampleAtomicLong().get());
		assertEquals(550, changedSettings.getSampleAtomicInteger().get());
		assertEquals("Test string 3", changedSettings.getSampleString());
		assertEquals(true, changedSettings.isSampleBoolean());
		assertEquals(202, changedSettings.getSampleLong());
		assertEquals(302, changedSettings.getSampleInt());
		assertEquals(6.2, changedSettings.getSampleDouble());
		assertEquals(SampleEnum.THREE, changedSettings.getSampleEnum());

	}
}
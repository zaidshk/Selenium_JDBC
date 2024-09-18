package xpo.qa.sc.wmx.tests;

import xpo.qa.common.selenium.XPOSeleniumTest;
import xpo.qa.sc.omx.data.OmxData;
import xpo.qa.sc.wmx.data.WmxData;

public class TestBase extends XPOSeleniumTest {

	protected String getApplicationURLWMX() {
		return WmxData.url_WMX;

	}

	protected String getApplicationURLWMXM() {
		return WmxData.url_m_WMX;

	}

	protected String getApplicationURLOMX() {
		return OmxData.url_OMX;

	}
}

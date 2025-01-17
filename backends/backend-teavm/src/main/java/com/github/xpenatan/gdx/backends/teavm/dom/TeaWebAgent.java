package com.github.xpenatan.gdx.backends.teavm.dom;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import com.github.xpenatan.gdx.backends.web.WebAgentInfo;

/**
 * @author xpenatan
 */
public class TeaWebAgent {

	@JSBody(script =
			"var userAgent = navigator.userAgent.toLowerCase();"
			+ "return {"
			+ "firefox : userAgent.indexOf('firefox') != -1,"
			+ "chrome : userAgent.indexOf('chrome') != -1,"
			+ "safari : userAgent.indexOf('safari') != -1,"
			+ "opera : userAgent.indexOf('opera') != -1,"
			+ "IE : userAgent.indexOf('msie') != -1,"
			+ "macOS : userAgent.indexOf('mac') != -1,"
			+ "linux : userAgent.indexOf('linux') != -1,"
			+ "windows : userAgent.indexOf('win') != -1"
			+ "};")
	private static native JSObject createAgent();

	public static WebAgentInfo computeAgentInfo() {
		JSObject jsObj = TeaWebAgent.createAgent();
		WebAgentInfo agent = (WebAgentInfo)jsObj;
		return agent;
	}
}

package ajechess.chess;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Resource {
	protected static ResourceBundle resources;
	static {
		try {
			resources = ResourceBundle.getBundle("ajechess.chess.res.AjechessProperties", Locale.getDefault());
		} catch (Exception exception) {
			System.exit(1);
		}
	}

	public String getResourceString(String key) {
		String string;
		try {
			string = resources.getString(key);
		} catch (Exception exception) {
			string = null;
		}
		return string;
	}

	protected URL getResource(String key) {
		String name = getResourceString(key);
		if (name != null) {
			URL url = this.getClass().getResource(name);
			return url;
		}
		return null;
	}
}

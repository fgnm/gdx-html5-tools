package com.github.xpenatan.gdx.backends.web;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.xpenatan.gdx.backends.web.dom.StorageWrapper;

/**
 * @author xpenatan
 */
public class WebPreferences implements Preferences{
	final String prefix;
	ObjectMap<String, Object> values = new ObjectMap<String, Object>();

	private StorageWrapper storage;

	public WebPreferences (StorageWrapper storage , String prefix) {
		this.storage = storage;
		this.prefix = prefix + ":";
		int prefixLength = this.prefix.length();
		try {
			for (int i = 0; i < storage.getLength(); i++) {
				String key = storage.key(i);
				if (key.startsWith(prefix)) {
					String value = storage.getItem(key);
					values.put(key.substring(prefixLength, key.length() - 1), toObject(key, value));
				}
			}
		} catch (Exception e) {
			values.clear();
		}
	}

	private Object toObject (String key, String value) {
		if (key.endsWith("b")) return new Boolean(Boolean.parseBoolean(value));
		if (key.endsWith("i")) return new Integer(Integer.parseInt(value));
		if (key.endsWith("l")) return new Long(Long.parseLong(value));
		if (key.endsWith("f")) return new Float(Float.parseFloat(value));
		return value;
	}

	private String toStorageKey (String key, Object value) {
		if (value instanceof Boolean) return prefix + key + "b";
		if (value instanceof Integer) return prefix + key + "i";
		if (value instanceof Long) return prefix + key + "l";
		if (value instanceof Float) return prefix + key + "f";
		return prefix + key + "s";
	}

	@Override
	public void flush () {
		try {
			// remove all old values
			for (int i = 0; i < storage.getLength(); i++) {
				String key = storage.key(i);
				if (key.startsWith(prefix)) storage.removeItem(key);
			}

			// push new values to LocalStorage
			for (String key : values.keys()) {
				String storageKey = toStorageKey(key, values.get(key));
				String storageValue = "" + values.get(key).toString();
				storage.setItem(storageKey, storageValue);
			}

		} catch (Exception e) {
			throw new GdxRuntimeException("Couldn't flush preferences");
		}
	}

	@Override
	public Preferences putBoolean (String key, boolean val) {
		values.put(key, val);
		return this;
	}

	@Override
	public Preferences putInteger (String key, int val) {
		values.put(key, val);
		return this;
	}

	@Override
	public Preferences putLong (String key, long val) {
		values.put(key, val);
		return this;
	}

	@Override
	public Preferences putFloat (String key, float val) {
		values.put(key, val);
		return this;
	}

	@Override
	public Preferences putString (String key, String val) {
		values.put(key, val);
		return this;
	}

	@Override
	public Preferences put (Map<String, ?> vals) {
		for (String key : vals.keySet()) {
			values.put(key, vals.get(key));
		}
		return this;
	}

	@Override
	public boolean getBoolean (String key) {
		Boolean v = (Boolean)values.get(key);
		return v == null ? false : v;
	}

	@Override
	public int getInteger (String key) {
		Integer v = (Integer)values.get(key);
		return v == null ? 0 : v;
	}

	@Override
	public long getLong (String key) {
		Long v = (Long)values.get(key);
		return v == null ? 0 : v;
	}

	@Override
	public float getFloat (String key) {
		Float v = (Float)values.get(key);
		return v == null ? 0 : v;
	}

	@Override
	public String getString (String key) {
		String v = (String)values.get(key);
		return v == null ? "" : v;
	}

	@Override
	public boolean getBoolean (String key, boolean defValue) {
		Boolean res = (Boolean)values.get(key);
		return res == null ? defValue : res;
	}

	@Override
	public int getInteger (String key, int defValue) {
		Integer res = (Integer)values.get(key);
		return res == null ? defValue : res;
	}

	@Override
	public long getLong (String key, long defValue) {
		Long res = (Long)values.get(key);
		return res == null ? defValue : res;
	}

	@Override
	public float getFloat (String key, float defValue) {
		Float res = (Float)values.get(key);
		return res == null ? defValue : res;
	}

	@Override
	public String getString (String key, String defValue) {
		String res = (String)values.get(key);
		return res == null ? defValue : res;
	}

	@Override
	public Map<String, ?> get () {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (String key : values.keys()) {
			map.put(key, values.get(key));
		}
		return map;
	}

	@Override
	public boolean contains (String key) {
		return values.containsKey(key);
	}

	@Override
	public void clear () {
		values.clear();
	}

	@Override
	public void remove (String key) {
		values.remove(key);
	}
}

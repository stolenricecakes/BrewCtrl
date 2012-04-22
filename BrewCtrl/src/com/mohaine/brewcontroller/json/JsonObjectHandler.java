package com.mohaine.brewcontroller.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mohaine.brewcontroller.util.StringUtils;

public abstract class JsonObjectHandler<T> {
	final static String RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(RFC1123);

	private Map<String, JsonObjectPropertyHandler<T, ?>> fieldHandlers = new HashMap<String, JsonObjectPropertyHandler<T, ?>>();

	{
		List<JsonObjectPropertyHandler<T, ?>> propertyHandlers = getPropertyHandlers();
		if (propertyHandlers != null) {
			for (JsonObjectPropertyHandler<T, ?> jsonObjectPropertyHandler : propertyHandlers) {
				fieldHandlers.put(jsonObjectPropertyHandler.getName(), jsonObjectPropertyHandler);
			}
		}
	}

	public abstract Class<? extends T> getObjectType();

	public abstract String getType();

	public abstract List<JsonObjectPropertyHandler<T, ?>> getPropertyHandlers();

	protected String encodeBoolean(Boolean bool) {
		if (bool != null && bool.booleanValue()) {
			return "true";
		}
		return "false";
	}

	protected Boolean decodeBoolean(String str) {
		return "true".equals(str) ? Boolean.TRUE : Boolean.FALSE;
	}

	protected String encodeDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return DATE_FORMAT.format(date);
		}
	}

	protected Date decodeDate(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		try {
			return DATE_FORMAT.parse(dateStr);
		} catch (ParseException pe) {
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processFromUnknown(T obj, JsonUnknownObject unknownObject, JsonConverterConfig config) {
		List<JsonObjectPropertyHandler<T, ?>> propertyHandlers = getPropertyHandlers();
		if (propertyHandlers != null) {
			for (JsonObjectPropertyHandler<T, ?> ph : propertyHandlers) {
				JsonObjectPropertyHandler<T, Object> phT = (JsonObjectPropertyHandler<T, Object>) ph;

				Object property = unknownObject.getProperty(phT.getName());
				if (property instanceof JsonUnknownObject) {

					if (phT.isJson()) {
						StringBuffer sb = new StringBuffer();
						new JsonEncoder(config).appendObject(sb, property);
						property = sb.toString();
					} else {
						Class<Object> expectedType = phT.getExpectedType();
						if (expectedType != null) {
							property = config.convertToObject((JsonUnknownObject) property, expectedType);
						}
					}
				}

				// TODO
				if (property instanceof List) {
					Class<Object> expectedType = phT.getExpectedType();
					if (expectedType != null) {
						if (!List.class.isAssignableFrom(expectedType)) {
							List list = (List) property;
							for (int i = 0; i < list.size(); i++) {
								Object listObj = list.get(i);
								if (listObj instanceof JsonUnknownObject) {
									listObj = config.convertToObject((JsonUnknownObject) listObj, expectedType);
									list.set(i, listObj);
								}

							}
						}

					}
				}

				phT.setValue(obj, property);
			}
		}
	}

	public void processParameters(T value, JsonEncoder jsonEncoder, StringBuffer sb, boolean first) {
		List<JsonObjectPropertyHandler<T, ?>> propertyHandlers = getPropertyHandlers();
		if (propertyHandlers != null) {
			for (JsonObjectPropertyHandler<T, ?> ph : propertyHandlers) {
				@SuppressWarnings("unchecked")
				JsonObjectPropertyHandler<T, Object> phT = (JsonObjectPropertyHandler<T, Object>) ph;
				Object fieldValue = phT.getValue(value);

				if (!first) {
					sb.append(",");
				}
				first = false;
				if (phT.isJson() && fieldValue instanceof String) {

					String fv = (String) fieldValue;

					if (!StringUtils.hasLength(fv)) {
						fv = "null";
					}
					jsonEncoder.appendNamedJsonValue(sb, phT.getName(), fv);
				} else {
					jsonEncoder.appendNamedValue(sb, phT.getName(), fieldValue);
				}
			}
		}
	}
}

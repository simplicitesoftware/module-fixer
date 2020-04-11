package com.simplicite.objects.Fixer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.AppLog;
import com.simplicite.util.Message;
import com.simplicite.util.Tool;
import com.simplicite.util.tools.BusinessObjectTool;
import com.simplicite.util.tools.RESTTool;

/**
 * Fixer.io conversion rates rates.
 */
public class FixerRates extends com.simplicite.util.ObjectDB {
	private static final long serialVersionUID = 1L;

	private String getCurrencyId(String code) {
		return getGrant().simpleQuery("select row_id from iso_currency where cur_code = '" + code+ "'");
	}

	/**
	 * Action method: refresh rates (settings are in the <code>FIXER_SETTINGS</code> system parameter)
	 * @return Message
	 */
	public String fixerRefresh() {
		AppLog.info(getClass(), "fixerRefresh", "Refreshing Fixer.io conversion rates", getGrant());
		try {
			String s = getGrant().getParameter("FIXER_SETTINGS", "{}");
			AppLog.info(getClass(), "fixerRefresh", "Settings: " + s, getGrant());
			JSONObject settings = new JSONObject(s);
			AppLog.info(getClass(), "fixerRefresh", "Settings: " + settings.toString(), getGrant());

			if (settings.optBoolean("replace")) {
				AppLog.info(getClass(), "fixerRefresh", "Deleting all rates", getGrant());
				getGrant().update("delete from " + getTable());
			}

			BusinessObjectTool fixt = new BusinessObjectTool(this);
			resetFilters();
			String url = settings.getString("url");
			JSONArray bases = settings.getJSONArray("bases");

			for (int i = 0; i < bases.length(); i++) {
				String base = bases.getString(i);
				String baseId = getCurrencyId(base);
				if (Tool.isEmpty(baseId))
					continue;

				String r = RESTTool.get(url + "?access_key=" + settings.getString("accessKey") + "&base=" + base);
				AppLog.info(getClass(), "fixerRefresh", "Rates: " + r, getGrant());
				JSONObject rates = new JSONObject(r);
				AppLog.info(getClass(), "fixerRefresh", "Rates (JSON): " + rates.toString(), getGrant());

				String date = rates.getString("date");
				JSONObject targets = rates.getJSONObject("rates");
				for (String target : targets.keySet()) {
					String targetId = getCurrencyId(target);
					if (Tool.isEmpty(targetId))
						continue;

					double rate = targets.optDouble(target);
					AppLog.info(getClass(), "fixerRefresh", base + " to " + target + ": " + rate, getGrant());
					resetValues();
					setFieldValue("fixerDate", date);
					setFieldValue("fixerBaseCurId", baseId);
					setFieldValue("fixerTargetCurId", targetId);
					setFieldValue("fixerConversionRate", rate);
					fixt.validateAndSave();
				}

			}

			return Message.formatSimpleText(settings.toString(2));
		} catch (Exception e) {
			return Message.formatSimpleError(e.getMessage());
		}
	}
}

package com.vinsasoft.security.AntiVirus;

import org.json.JSONException;
import org.json.JSONObject;


public interface IJSONSerializer
{
    public JSONObject buildJSONObject() throws JSONException;
    public void loadFromJSON(JSONObject appObject);
}

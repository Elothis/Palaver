package fabian.de.palaver;

import org.json.JSONObject;

public class ApiResult {

    private JSONObject jsonobj;
    private NetworkHelper.ApiCommand apicmd;

    public ApiResult(NetworkHelper.ApiCommand apicmd, JSONObject jsonobj) {
        this.apicmd = apicmd;
        this.jsonobj = jsonobj;
    }

    public JSONObject getJsonobj() {
        return jsonobj;
    }

    public void setJsonobj(JSONObject jsonobj) {
        this.jsonobj = jsonobj;
    }

    public NetworkHelper.ApiCommand getApicmd() {
        return apicmd;
    }

    public void setApicmd(NetworkHelper.ApiCommand apicmd) {
        this.apicmd = apicmd;
    }
}

package rs.org.habiprot.alciphron.alci;

import com.zerocodeteam.network.ZctRequest;
import com.zerocodeteam.network.ZctResponse;

import java.util.HashMap;
import java.util.Map;

import rs.org.habiprot.alciphron.alci.common.AppUtils;

/**
 * Created by milorad on 6.12.17..
 */
public class API {

    //API urls
    private static final String API_BASE_URL = "http://alciphron.habiprot.org.rs/index.php/";
    private static final String API_LOGIN = "auth/login";

    private static API sInstance;

    public static API getInstance() {
        if (sInstance == null) {
            sInstance = new API();
        }
        return sInstance;
    }

    /**
     * Performs login procedure for given loginData
     *
     * @param listener
     */
    public void loginUser(ZctResponse<String> listener, HashMap<String, String> fields) {
        Map<String, String> headers = new HashMap<>();
        String boundary = "---------alciphron-" + System.currentTimeMillis();
        String multipartData = AppUtils.addParameters(fields, boundary);

        ZctRequest request = new ZctRequest.Builder(API_BASE_URL + API_LOGIN)
                .responseClass(String.class)
                .method(ZctRequest.Method.POST)
                .response(listener)
                .bodyContentType("multipart/form-data; boundary=" + boundary)
                .headers(headers)
                .bodyContent(multipartData)
                .build();

        App.getNetwork().sendRequest(request);
    }



}

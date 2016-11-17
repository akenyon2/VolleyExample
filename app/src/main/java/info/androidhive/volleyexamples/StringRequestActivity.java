package info.androidhive.volleyexamples;

import info.androidhive.volleyexamples.app.AppController;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

public class StringRequestActivity extends Activity {

	private String TAG = StringRequestActivity.class.getSimpleName();
	private Button btnStringReq;
	private TextView msgResponse;
	private ProgressDialog pDialog;
	private String result;

	// This tag will be used to cancel the request
	private String tag_string_req = "string_req";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_string);

		btnStringReq = (Button) findViewById(R.id.btnStringReq);
		msgResponse = (TextView) findViewById(R.id.msgResponse);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		btnStringReq.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					makeStringReq();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showProgressDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}





	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
		String algorithm="HmacSHA256";
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return mac.doFinal(data.getBytes("UTF8"));
	}

	static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
		byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
		byte[] kDate = HmacSHA256(dateStamp, kSecret);
		byte[] kRegion = HmacSHA256(regionName, kDate);
		byte[] kService = HmacSHA256(serviceName, kRegion);
		byte[] kSigning = HmacSHA256("aws4_request", kService);
		return kSigning;
	}

	/**
	 * Making json object request
	 * */
	private void makeStringReq() throws Exception {
		showProgressDialog();
		//StringRequest strReq = new StringRequest(Method.GET,
				//"http://api.walmartlabs.com/v1/search?apiKey=52pcepcteuhhwx7gtg5z7dbe&query="
						//+ "apples", new Response.Listener<String>() {

byte[] test = getSignatureKey("Z98+BD38JMHybp4hgYlHAoJ2dShEWYmIGGDfeANJ", "2016-11-18", "us-east-1", "AWSECommerceService");
System.out.println("blah = " + test.toString());
		String hmac = hamc(test);
		StringRequest strReq = new StringRequest(Method.GET,
		"http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=AKIAIDAFRAB6CFIGFNNA&AssociateTag=draft05a-20&Operation=ItemSearch&Keywords=the%20hunger%20games&SearchIndex=Books&Timestamp=2016-11-18&signature="
		+ test.toString(), new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//Log.d(TAG, response.toString());
						result = response;
						int a = result.indexOf("salePrice");
						int b = result.indexOf("upc");
						String finalResult;
						//finalResult = result.substring(a+11, b-2);
						//System.out.println("final result = " + finalResult);
						msgResponse.setText(response.toString());
						hideProgressDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hideProgressDialog();
					}
				});
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}
}
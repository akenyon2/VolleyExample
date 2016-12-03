package info.androidhive.volleyexamples;

import info.androidhive.volleyexamples.app.AppController;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
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

import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import java.text.DateFormat;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.nio.charset.StandardCharsets;
/*
import uk.co.lucasweb.aws.v4.signer.*;
import uk.co.lucasweb.aws.v4.signer.credentials.AwsCredentials;
import java.net.URI;

import uk.co.lucasweb.aws.v4.signer.functional.Streams;
import uk.co.lucasweb.aws.v4.signer.functional.Throwables.*;
import uk.co.lucasweb.aws.v4.signer.hash.*;
*/

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class StringRequestActivity extends Activity {


	private String TAG = StringRequestActivity.class.getSimpleName();
	private Button btnStringReq;
	private TextView msgResponse;
	private ProgressDialog pDialog;
	private String result;

	String secretKey = "K8Ncx5gzpKSWuEmrWVxn9svOIkxJp5lAKbeu37P7";
	String regionName = "us-east-1";
	String serviceName = "AWSECommerceService"; //or try "execute-api";
	String accessKey = "AKIAIDAFRAB6CFIGFNNA";

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

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars).toLowerCase();
	}

	/**
	 * Making json object request
	 * */
	private void makeStringReq() throws Exception {
		showProgressDialog();
		//StringRequest strReq = new StringRequest(Method.GET,
		//"http://api.walmartlabs.com/v1/search?apiKey=52pcepcteuhhwx7gtg5z7dbe&query="
		//+ "apples", new Response.Listener<String>() {


		/*
		String uri = "http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=AKIAIDAFRAB6CFIGFNNA&AssociateTag=draft05a-20&Operation=ItemSearch&Keywords=the%20hunger%20games&SearchIndex=Books&Timestamp="
				+ format + "&Signature=" + strHexSignature;
		URL url = new URL(uri);
		HttpURLConnection connection =
				(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");

		InputStream xml = connection.getInputStream();
		*/




	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");
	String format = simpleDateFormat.format(new Date());
	System.out.println("Current Timestamp: " + format);


	byte[] test = getSignatureKey(secretKey, format, regionName, serviceName);
	String strHexSignature = bytesToHex(test);
	System.out.println("blah2 = " + strHexSignature);

	String url = "http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&Operation=ItemSearch&AWSAccessKeyId=AKIAIDAFRAB6CFIGFNNA&AssociateTag=draft05a-20&Keywords=the%20hunger%20games&SearchIndex=Books&Timestamp="
			+ format + "&Signature=" + strHexSignature;
	System.out.println("URL = " + url);


	/*
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db = dbf.newDocumentBuilder();
	Document doc = db.parse(new URL(url).openStream());

		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //pretty printing
		transformer.transform(domSource, result);
		System.out.println("XML IN String format is: \n" + writer.toString());

	System.out.println("Doc = " + doc);*/
/*
		StringRequest strReq = new StringRequest(Method.GET,
				"http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=AKIAIDAFRAB6CFIGFNNA&AssociateTag=draft05a-20&Operation=ItemSearch&Keywords=the%20hunger%20games&SearchIndex=Books&Timestamp=" + format + "&Signature="
						+ strHexSignature, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				//Log.d(TAG, response.toString());
				 String result = response;
				System.out.println("blah 3 = " + result);
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
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);*/
	}
}

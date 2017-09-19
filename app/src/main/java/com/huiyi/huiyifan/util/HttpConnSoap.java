package com.huiyi.huiyifan.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class HttpConnSoap {

	/*获取接口xml数据的方法参考用返回ArrayList开始*/
	public ArrayList<String> GetWebServre(String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues,String fuwuqi_url) {
		ArrayList<String> Values = new ArrayList<String>();

		//ServerUrl是指webservice的url
		String ServerUrl = fuwuqi_url;//通过adout页面获取

		String soapAction = "http://tempuri.org/" + methodName;
		String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
				" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body />";
		String tps, vps, ts;
		String mreakString = "";

		mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";
		for (int i = 0; i < Parameters.size(); i++) {
			tps = Parameters.get(i).toString();
			//设置该方法的参数为.net webService中的参数名称
			vps = ParValues.get(i).toString();
			ts = "<" + tps + ">" + vps + "</" + tps + ">";
			mreakString = mreakString + ts;
		}
		mreakString = mreakString + "</" + methodName + ">";
		String soap2 = "</soap:Envelope>";
		String requestData = soap + mreakString + soap2;

		try {
			URL url = new URL(ServerUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			byte[] bytes = requestData.getBytes("utf-8");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(6000000);// 设置超时时间
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
			con.setRequestProperty("SOAPAction", soapAction);
			con.setRequestProperty("Content-Length", "" + bytes.length);
			OutputStream outStream = con.getOutputStream();
			outStream.write(bytes);
			outStream.flush();
			outStream.close();
			InputStream inStream = con.getInputStream();
			Values = inputStreamtovaluelist(inStream, methodName);
			return Values;

		} catch (Exception e) {
			System.out.print("连接出错");
			return Values;
		}
	}


	public ArrayList<String> inputStreamtovaluelist(InputStream in, String MonthsName) throws IOException {
		StringBuffer out = new StringBuffer();
		String s1 = "";
		byte[] b = new byte[4096];
		ArrayList<String> Values = new ArrayList<String>();
		Values.clear();

		for (int n; (n = in.read(b)) != -1;) {
			s1 = new String(b, 0, n);
			out.append(s1);
		}

		System.out.println(out);
		String[] s13 = s1.split("><");
		String ifString = MonthsName + "Result";
		String TS = "";
		String vs = "";

		Boolean getValueBoolean = false;
		for (int i = 0; i < s13.length; i++) {
			TS = s13[i];
			System.out.println(TS);
			int j, k, l;
			j = TS.indexOf(ifString);
			k = TS.lastIndexOf(ifString);

			if (j >= 0) {
				System.out.println(j);
				if (getValueBoolean == false) {
					getValueBoolean = true;
				} else {

				}

				if ((j >= 0) && (k > j)) {
					System.out.println("FFF" + TS.lastIndexOf("/" + ifString));
					l = ifString.length() + 1;
					vs = TS.substring(j + l, k - 2);
					Values.add(vs);
					System.out.println("退出" + vs);
					getValueBoolean = false;
					return Values;
				}

			}
			if (TS.lastIndexOf("/" + ifString) >= 0) {
				getValueBoolean = false;
				return Values;
			}
			if ((getValueBoolean) && (TS.lastIndexOf("/" + ifString) < 0) && (j < 0)) {
				k = TS.length();
				vs = TS.substring(7, k - 8);
				Values.add(vs);
			}

		}

		return Values;
	}
	/*获取接口xml数据的方法参考用返回ArrayList结束*/

	public String GetWebServreForJson(String methodName,String fuwuqi_url) {

		String namespace="http://tempuri.org/";
		SoapObject soapObject=new SoapObject(namespace, methodName);
		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet=true;
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE httpTranstation=new HttpTransportSE(fuwuqi_url,600000);
		try {
			httpTranstation.call(namespace+methodName, envelope);
			Object result=envelope.getResponse();
			String str=(String) result.toString();//获得请求的字符串
			return str;
		}catch(Exception e)
		{

			System.out.print(e);
			return "";
		}
	}



	public String Getjson(String fmethodName, String fuwuqi_url) {

		// 命名空间
		String nameSpace = "http://tempuri.org/";
		// 调用方法的名称
		String methodName = fmethodName;
		// URL
		String endPoint = fuwuqi_url;
		// SOAP Action
		String soapAction = "http://tempuri.org/"+fmethodName+"";
		// 指定WebService的命名空间和调用方法
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		// 设置需要调用WebService接口的两个参数mobileCode UserId

		// 生成调用WebService方法调用的soap信息，并且指定Soap版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.bodyOut = soapObject;

		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE transport = new HttpTransportSE(endPoint,600000);
		try {
			transport.call(soapAction, envelope);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		SoapObject object = (SoapObject) envelope.bodyIn;

		String result = object.getProperty(0).toString();
		return result;
	}


	public String Getjson2(String sproducer_id,String fmethodName, String fuwuqi_url) {

		String nameSpace = "http://tempuri.org/";
		String methodName = fmethodName;
		// URL
		String endPoint = fuwuqi_url;
		// SOAP Action
		String soapAction = "http://tempuri.org/"+fmethodName+"";
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("producerID", sproducer_id);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		envelope.bodyOut = soapObject;
		// 是否调用DotNet开发的WebService
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE transport = new HttpTransportSE(endPoint,600000);
		try {
			transport.call(soapAction, envelope);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}

		SoapObject object = (SoapObject) envelope.bodyIn;

		String result = object.getProperty(0).toString();
		return result;
	}


	public String Getjson3(String ma,String fmethodName, String fuwuqi_url) {

		// 命名空间
		String nameSpace = "http://tempuri.org/";

		String methodName = fmethodName;
		// URL
		String endPoint = fuwuqi_url;
		// SOAP Action
		String soapAction = "http://tempuri.org/"+fmethodName+"";

		SoapObject soapObject = new SoapObject(nameSpace, methodName);

		soapObject.addProperty("xcode", ma);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.bodyOut = soapObject;

		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		HttpTransportSE transport = new HttpTransportSE(endPoint,600000);
		try {
			transport.call(soapAction, envelope);
		} catch (IOException e) {

			e.printStackTrace();
		} catch (XmlPullParserException e) {

			e.printStackTrace();
		}

		SoapObject object = (SoapObject) envelope.bodyIn;

		String result = object.getProperty(0).toString();
		return result;
	}



}

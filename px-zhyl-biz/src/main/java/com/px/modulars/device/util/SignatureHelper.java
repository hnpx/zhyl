package com.px.modulars.device.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import net.sf.json.xml.XMLSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignatureHelper {

    /* *//**
     * 在线签名
     *
     * @param creditCode
     * @param text
     * @return
     *//*
    public static String Sign(String creditCode, String text) {
        String url = "http://218.29.120.86:8091/sign";
        String rel = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("creditCode", creditCode);
        map.put("text", text);
        String Message = HTTPHelper.doPost(url, map, "", "");
        JSONObject object = JSONObject.parseObject(Message);
        String code = object.getString("code");
        if (code.equals("200")) {
            rel = object.getString("data");
        } else {
            rel = object.getString("message");
        }
        return rel;
    }*/

    /**
     * 向指定URL发送post方法的请求，字符串
     *
     * @param urlString 地址
     * @param reqString 请求参数 name1=value1&name2=value2 的形式。
     * @return String 返回内容
     */
    public static String postString(String urlString, String reqString) {
        StringBuffer sb = new StringBuffer("");
        try {
            // 创建连接
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            //下边这行注释掉
            /***connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");**
             */connection.connect();
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            out.write(reqString.getBytes("UTF-8"));
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String lines;

            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (sb.toString().length() == 0 | sb.toString().trim().length() == 0) {
            return "";
        }
        return sb.toString();
    }

    /**
     * 发送post请求
     *
     * @param jsonObject 参数(json类型)
     * @return
     * @throws IOException
     */
    public static String send(String url, JSONObject jsonObject) throws IOException {
        String body = "";
        //String url = "http://218.29.120.86:8091/sign";
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        s.setContentEncoding(new BasicHeader("Content-Type",
                "application/json;charset=UTF-8"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
        System.out.println("请求地址：" + url);
//        System.out.println("请求参数："+nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "utf-8");
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }


    /**
     * Json to xml string.
     *
     * @param json the json
     * @return the string
     */
    public static String jsonToXml(String json) {
        try {
            StringBuffer buffer = new StringBuffer();
            JSONObject jObj = JSONObject.parseObject(json);
            jsonToXmlstr(jObj, buffer);
            String tempStr = buffer.toString();
            String _Headerstr = tempStr.substring(tempStr.indexOf("<Header>"), tempStr.indexOf("</Header>") + 9) + tempStr.substring(0, tempStr.indexOf("<Header>"));
            String _Mainstr = tempStr.substring(tempStr.indexOf("<Main>"), tempStr.indexOf("</Main>") + 9) + tempStr.substring(0, tempStr.indexOf("<Main>"));
            StringBuffer buffers = new StringBuffer();
            //buffers.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            //buffers.append("<EInvoice>");
            buffers.append(_Headerstr);
            /*buffers.append("<EInvoiceData>");
            buffers.append(_Mainstr);
            buffers.append("</EInvoiceData>");*/
            //buffers.append("</EInvoice>");
            return buffers.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * Json to xmlstr string.
     *
     * @param jObj   the j obj
     * @param buffer the buffer
     * @return the string
     */
    public static String jsonToXmlstr(JSONObject jObj, StringBuffer buffer) {
        Set<Map.Entry<String, Object>> se = jObj.entrySet();
        for (Iterator<Map.Entry<String, Object>> it = se.iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> en = it.next();
            if (en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONObject")) {
                buffer.append("<" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">");
                JSONObject jo = jObj.getJSONObject(en.getKey());
                jsonToXmlstr(jo, buffer);
                buffer.append("</" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">");
            } else if (en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONArray")) {
                JSONArray jarray = jObj.getJSONArray(en.getKey());
                for (int i = 0; i < jarray.size(); i++) {
                    buffer.append("<" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">");
                    JSONObject jsonobject = jarray.getJSONObject(i);
                    jsonToXmlstr(jsonobject, buffer);
                    buffer.append("</" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">");
                }
            } else if (en.getValue().getClass().getName().equals("java.lang.String")) {
                buffer.append("<" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">" + en.getValue());
                buffer.append("</" + en.getKey().substring(0, 1).toUpperCase() + en.getKey().substring(1) + ">");
            }
        }
        return buffer.toString();
    }

    /**
     * xml转json
     *
     * @param xmlString
     * @return
     */
    public static String XmlToJson(String xmlString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(xmlString);
        return json.toString(1);
    }

    public static JSONObject toJsonObj(Map<String, String> map) {
        JSONObject resultJson = new JSONObject();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            resultJson.put(key, map.get(key));
        }
        return resultJson;
    }
}

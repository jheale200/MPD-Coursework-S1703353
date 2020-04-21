package com.example.mpd_coursework_s1703353;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static String TITLE = "title";
    private static String DESCRIPTION = "description";
    private static String GEORSS = "georss:point";
    private static String PUBDATE = "pubDate";
    private static String ITEM = "item";
    private static String LINK = "link";


    public Parser(){

    }

    public List<TrafficItems> getFeedItems(String RSS_URL) {
        List<TrafficItems> ItemList = new ArrayList<TrafficItems>();
        String xml_feed;

        xml_feed = this.getXmlUrl(RSS_URL);
        if (xml_feed != null) {

            XmlPullParserFactory parserFactory;

            try {
                parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(xml_feed));
                TrafficItems RSSTraffic = null;
                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String eltName = null;

                    switch (eventType) {

                        case XmlPullParser.START_TAG:
                            eltName = parser.getName();

                            if (ITEM.equals(eltName)) {
                                RSSTraffic = new TrafficItems();
                                ItemList.add(RSSTraffic);

                            } else if (RSSTraffic != null) {
                                if (TITLE.equals(eltName)) {
                                    RSSTraffic.setTitle(parser.nextText());
                                } else if (DESCRIPTION.equals(eltName)) {
                                    RSSTraffic.setDescription(parser.nextText().replaceAll("<.*?>","\n"));
                                } else if (LINK.equals(eltName)) {
                                    RSSTraffic.setLink(parser.nextText());
                                } else if (GEORSS.equals(eltName)) {
                                    RSSTraffic.setGeorss(parser.nextText());
                                }  else if (PUBDATE.equals(eltName)) {
                                    RSSTraffic.setPubDate(parser.nextText());
                                }
                            }
                            break;
                    }

                    eventType = parser.next();

                }
            } catch (XmlPullParserException | IOException e) {
                System.out.println(e);
            }
        }
        return ItemList;
    }

    private String getXmlUrl(String url){
        String RSS_Xml = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            RSS_Xml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return RSS_Xml;
    }



}

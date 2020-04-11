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

    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_GEORSS = "georss:point";
    private static String TAG_PUBDATE = "pubDate";
    private static String TAG_ITEM = "item";
    private static String TAG_LINK = "link";

    public Parser(){

    }

    public List<TrafficItems> getRSSFeedItems(String rss_url) {
        List<TrafficItems> ItemList = new ArrayList<TrafficItems>();
        String rss_feed_xml;

        rss_feed_xml = this.getXmlFromUrl(rss_url);
        if (rss_feed_xml != null) {

            XmlPullParserFactory parserFactory;
            try {
                parserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(rss_feed_xml));
                TrafficItems rssTraffic = null;
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String eltName = null;

                    switch (eventType) {

                        case XmlPullParser.START_TAG:
                            eltName = parser.getName();

                            if (TAG_ITEM.equals(eltName)) {
                                rssTraffic = new TrafficItems();
                                ItemList.add(rssTraffic);

                            } else if (rssTraffic != null) {
                                if (TAG_TITLE.equals(eltName)) {
                                    rssTraffic.setTitle(parser.nextText());
                                } else if (TAG_DESCRIPTION.equals(eltName)) {
                                    rssTraffic.setDescription(parser.nextText().replaceAll("<.*?>","\n"));
                                } else if (TAG_LINK.equals(eltName)) {
                                    rssTraffic.setLink(parser.nextText());
                                } else if (TAG_GEORSS.equals(eltName)) {
                                    rssTraffic.setGeorss(parser.nextText());
                                }  else if (TAG_PUBDATE.equals(eltName)) {
                                    rssTraffic.setPubDate(parser.nextText());
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

    public String getXmlFromUrl(String url){
        String rssxml = null;

        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            rssxml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (ClientProtocolException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
        return rssxml;
    }



}

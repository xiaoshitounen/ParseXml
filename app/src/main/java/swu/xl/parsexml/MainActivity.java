package swu.xl.parsexml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sex方式解析
        /*try {
            //解析Xml数据
            testSexParse();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //PULL解析
        //testPULLParse();

        //DOM解析
        testDOMParse();
    }

    /**
     * Sex方式解析
     */
    private void testSexParse() throws ParserConfigurationException, SAXException, IOException {
        /*SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();

        SAXParseHandler saxParseHandler = new SAXParseHandler();
        xmlReader.setContentHandler(saxParseHandler);

        InputStream is = getResources().openRawResource(R.raw.test);
        InputSource inputSource = new InputSource(is);

        xmlReader.parse(inputSource);
        saxParseHandler.getXmlList();*/

        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(new SAXParseHandler());
        xmlReader.parse(new InputSource(getResources().openRawResource(R.raw.test)));

        SAXParseHandler saxParseHandler = (SAXParseHandler) xmlReader.getContentHandler();
        for (WebItem webItem : saxParseHandler.getXmlList()) {
            //Toast.makeText(this, "id:"+webItem.getId()+" url:"+webItem.getUrl()+" content:"+webItem.getContent(), Toast.LENGTH_SHORT).show();
            //显示数据
            addToRootLayout((LinearLayout) findViewById(R.id.root),webItem);
        }
    }

    /**
     * PULL解析 使用XmlResourceParser 只有xml里面才可以读到
     */
    private void testPULLParse(){
        //存储解析的模型资源
        List<WebItem> webItems = null;

        //临时的模型类
        WebItem webItem = null;

        //获取Xml资源解析器
        XmlResourceParser xmlResourceParser = getResources().getXml(R.xml.test);

        //解析
        try {

            //事件类型
            int eventType = xmlResourceParser.getEventType();

            //只要还没有到文档结束的地方就继续解析
            while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT) {

                if (eventType == XmlResourceParser.START_DOCUMENT){

                    //如果当前的位置是文档的开始位置，实例化模型集合
                    webItems = new ArrayList<>();

                }else if (eventType == XmlResourceParser.START_TAG){

                    //如果当前位置是标签开始的位置

                    //如果当前标签是item，实例化WebItem，根据索引获得属性值
                    if (TextUtils.equals(xmlResourceParser.getName(),"item")){
                        webItem = new WebItem();

                        webItem.setId(Integer.parseInt(xmlResourceParser.getAttributeValue(0)));
                        webItem.setUrl(xmlResourceParser.getAttributeValue(1));
                        webItem.setContent(xmlResourceParser.nextText());

                        assert webItems != null;
                        webItems.add(webItem);
                    }
                }

                //继续解析
                eventType = xmlResourceParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();

            Toast.makeText(this, "没有XMlPull解析器", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(this, "IO异常", Toast.LENGTH_SHORT).show();
        }

        for (WebItem item : webItems) {
            //Toast.makeText(this, "id:"+item.getId()+" url:"+item.getUrl()+" content:"+item.getContent(), Toast.LENGTH_SHORT).show();
            //显示数据
            addToRootLayout((LinearLayout) findViewById(R.id.root),item);
        }
    }

    /**
     * DOM解读
     */
    private void testDOMParse(){
        //存储解析的模型资源
        List<WebItem> webItems = new ArrayList<>();

        //临时的模型类
        WebItem webItem = null;

        try {
            //创建一个DocumentBuilder工厂类
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            //根据DocumentBuilder工厂类创建DocumentBuilder
            DocumentBuilder db = dbf.newDocumentBuilder();

            //使用DocumentBuilder加载xml文件
            Document document = db.parse(getResources().openRawResource(R.raw.test));

            //获取所有的item节点集合
            NodeList items = document.getElementsByTagName("item");

            //遍历每一个item节点
            for (int i = 0; i < items.getLength(); i++) {
                //初始化模型类
                webItem = new WebItem();

                //获取一个具体的item
                Node item = items.item(i);

                //获取一个item的所有属性集合
                NamedNodeMap attrs = item.getAttributes();

                //遍历item的所有属性
                for (int j = 0; j < attrs.getLength(); j++) {
                    //获取某个属性
                    Node node = attrs.item(j);
                    //获取节点属性名
                    String nodeName = node.getNodeName();
                    //获取节点属性值
                    String nodeValue = node.getNodeValue();

                    if (TextUtils.equals(nodeName,"id")){
                        webItem.setId(Integer.parseInt(nodeValue));
                    }

                    if (TextUtils.equals(nodeName,"url")){
                        webItem.setUrl(nodeValue);
                    }
                }

                //获取item的值
                String content = item.getFirstChild().getNodeValue();
                webItem.setContent(content);

                //加入集合
                webItems.add(webItem);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (WebItem item : webItems) {
            //Toast.makeText(this, "id:"+item.getId()+" url:"+item.getUrl()+" content:"+item.getContent(), Toast.LENGTH_SHORT).show();
            //显示数据
            addToRootLayout((LinearLayout) findViewById(R.id.root),item);
        }
    }

    /**
     * 将WebItem显示到布局中
     * @param layout
     * @param webItem
     */
    private void addToRootLayout(LinearLayout layout, WebItem webItem){
        View inflate = LayoutInflater.from(this).inflate(R.layout.web_item_layout, null);
        TextView id_text = inflate.findViewById(R.id.id);
        TextView content_text = inflate.findViewById(R.id.content);
        TextView url_text = inflate.findViewById(R.id.url);

        id_text.setText(String.valueOf(webItem.getId()));
        content_text.setText(webItem.getContent());
        url_text.setText(webItem.getUrl());

        layout.addView(inflate);
    }
}
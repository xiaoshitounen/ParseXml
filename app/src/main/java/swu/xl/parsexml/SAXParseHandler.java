package swu.xl.parsexml;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXParseHandler extends DefaultHandler {

    //日志
    public static final String TAG = SAXParseHandler.class.getSimpleName();

    //解析的数据集合
    private List<WebItem> webItems;

    //子项的name
    private String NODE_NAME = "item";

    //模型类
    private WebItem webItem;

    //当前读取的node_name的值
    private String current_node_name;

    //开始这个文件 test.xml
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        Log.d(TAG,"startDocument");

        webItems = new ArrayList<>();
    }

    //开始这个元素 item
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        Log.d(TAG,"startElement localName:"+localName+" qName:"+qName);

        //new 一个模型类
        webItem = new WebItem();

        //如果元素名相同
        if (TextUtils.equals(localName, NODE_NAME)){
            //设置当前的node_name
            current_node_name = localName;

            //读取属性
            for (int i = 0; i < attributes.getLength(); i++) {
                //循环读取属性 id
                if (TextUtils.equals(attributes.getLocalName(i),"id")){
                    webItem.setId(Integer.parseInt(attributes.getValue(i)));
                }

                //循环读取属性 url
                if (TextUtils.equals(attributes.getLocalName(i),"url")){
                    webItem.setUrl(attributes.getValue(i));
                }
            }
        }
    }

    //内容 百度
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        //获取内容
        String content = String.valueOf(ch, start, length);
        Log.d(TAG,"characters ch:"+content);

        //设置
        if (TextUtils.equals(current_node_name,NODE_NAME)){
            webItem.setContent(content);
        }
    }

    //结束这个元素 item
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        Log.d(TAG,"endElement localName:"+localName+" qName:"+qName);

        if (TextUtils.equals(current_node_name,NODE_NAME)){
            //将读取的数据加入到集合中
            webItems.add(webItem);
        }

        //取消当前的node_name
        current_node_name = null;
    }

    //结束这个文件 test.xml
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        Log.d(TAG,"endDocument");
    }

    //返回数据
    public List<WebItem> getXmlList(){
        return webItems;
    }
}

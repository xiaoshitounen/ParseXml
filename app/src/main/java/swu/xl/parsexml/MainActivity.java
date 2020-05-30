package swu.xl.parsexml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //解析Xml数据
            testSexParse();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
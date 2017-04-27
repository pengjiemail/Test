package te;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;



public class TestXml {
  public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
    SAXParserFactory saxf=SAXParserFactory.newInstance();
    SAXParser saxp=saxf.newSAXParser();
    InputStream is=new FileInputStream("test.xml");
    saxp.parse(is,new myParseHandle());
  }
  
  static class myParseHandle extends DefaultHandler{

    private StringBuffer sb;
    
    @Override
    public void startDocument() throws SAXException {
      sb=new StringBuffer();
      System.out.println("文档开始了");
    }

    @Override
    public void endDocument() throws SAXException {
      System.out.println(sb.toString());
      System.out.println("文档结束了");
      sb.delete(0, sb.length());
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      if(!qName.equals("employees")){
        if(attributes.getValue("id")!=null){
          sb.append("<"+qName+" id=\""+attributes.getValue("id")+"\">");//需要事先知道属性的名称
        }else{
          sb.append("<"+qName+">");
        }
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if(!qName.equals("employees")){
        sb.append("</"+qName+">");
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      String str=new String(ch,start,length);
      if(str!=null&&!str.trim().equals("")){
        sb.append(str);
      }
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
      System.out.println("SAX解析失败");
    }
    
  }
}

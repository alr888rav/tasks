package com.alr.tasks;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import android.content.Context;
import org.w3c.dom.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class XMLio {
    private Context context;
    private MyDb myDb;

    XMLio(Context context, MyDb myDb) {
        this.context = context;
        this.myDb = myDb;
    }

/*    private String getTextValue(Element doc, String tag, String defValue) {
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        if (nl.getLength() > 0 && nl.item(0).hasChildNodes())
            return nl.item(0).getFirstChild().getTextContent();
        else
            return defValue;
    }
    private boolean getBoolValue(Element doc, String tag, boolean defValue) {
        String v = getTextValue(doc, tag, "");
        if (v.length() > 0)
            return Boolean.parseBoolean(v);
        else
            return defValue;
    }
    private int getIntValue(Element doc, String tag, int defValue) {
        String v = getTextValue(doc, tag, "");
        if (v.length() > 0)
            return Integer.parseInt(v);
        else
            return defValue;
    }
    private String getSubNodeTextValue(Element doc, String tag, String subTag, String defValue) {
        NodeList nl;
        nl = doc.getElementsByTagName(tag);
        for (int i=0; i<nl.item(0).getChildNodes().getLength(); i++) {
            if (nl.item(0).getChildNodes().item(i).getNodeName().toLowerCase().equals(subTag.toLowerCase())) {
                return nl.item(0).getChildNodes().item(i).getTextContent();
            }
        }
        return defValue;
    }
*/

    public void read(String xmlFile) {
        Document dom;
        try {
            File file = new File(xmlFile);
            if (file.exists()) {
                // Make an  instance of the DocumentBuilderFactory
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                // parse using the builder to get the DOM mapping of the XML file
                dom = db.parse("file://" + xmlFile);
                dom.getDocumentElement().normalize();

                NodeList nodeList = dom.getElementsByTagName("task");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node firstNode = nodeList.item(i);
                    if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element firstElement = (Element) firstNode;

                        NodeList elementList = firstElement.getElementsByTagName("id");
                        Element element = (Element) elementList.item(0);
                        NodeList node = element.getChildNodes();
                        String id = node.item(0).getNodeValue();

                        NodeList elementList2 = firstElement.getElementsByTagName("hour");
                        Element element2 = (Element) elementList2.item(0);
                        NodeList node2 = element2.getChildNodes();
                        String hour = node2.item(0).getNodeValue();

                        NodeList elementList3 = firstElement.getElementsByTagName("minute");
                        Element element3 = (Element) elementList3.item(0);
                        NodeList node3 = element3.getChildNodes();
                        String minute = node3.item(0).getNodeValue();

                        NodeList elementList4 = firstElement.getElementsByTagName("active");
                        Element element4 = (Element) elementList4.item(0);
                        NodeList node4 = element4.getChildNodes();
                        String active = node4.item(0).getNodeValue();

                        NodeList elementList5 = firstElement.getElementsByTagName("mobile");
                        Element element5 = (Element) elementList5.item(0);
                        NodeList node5 = element5.getChildNodes();
                        String mobile = node5.item(0).getNodeValue();

                        NodeList elementList6 = firstElement.getElementsByTagName("bluetooth");
                        Element element6 = (Element) elementList6.item(0);
                        NodeList node6 = element6.getChildNodes();
                        String bluetooth = node6.item(0).getNodeValue();

                        NodeList elementList61 = firstElement.getElementsByTagName("ring");
                        Element element61 = (Element) elementList61.item(0);
                        NodeList node61 = element61.getChildNodes();
                        String ring = node61.item(0).getNodeValue();

                        NodeList elementList62 = firstElement.getElementsByTagName("notify");
                        Element element62 = (Element) elementList62.item(0);
                        NodeList node62 = element62.getChildNodes();
                        String notify = node62.item(0).getNodeValue();

                        NodeList elementList63 = firstElement.getElementsByTagName("ring_value");
                        Element element63 = (Element) elementList63.item(0);
                        NodeList node63 = element63.getChildNodes();
                        String ringv = node63.item(0).getNodeValue();

                        NodeList elementList64 = firstElement.getElementsByTagName("notify_value");
                        Element element64 = (Element) elementList64.item(0);
                        NodeList node64 = element64.getChildNodes();
                        String notifyv = node64.item(0).getNodeValue();

                        NodeList elementList7 = firstElement.getElementsByTagName("wifi");
                        Element element7 = (Element) elementList7.item(0);
                        NodeList node7 = element7.getChildNodes();
                        String wifi = node7.item(0).getNodeValue();

                        NodeList elementList8 = firstElement.getElementsByTagName("onoff");
                        Element element8 = (Element) elementList8.item(0);
                        NodeList node8 = element8.getChildNodes();
                        String onoff = node8.item(0).getNodeValue();

                        NodeList elementList9 = firstElement.getElementsByTagName("day1");
                        Element element9 = (Element) elementList9.item(0);
                        NodeList node9 = element9.getChildNodes();
                        String day1 = node9.item(0).getNodeValue();

                        NodeList elementList10 = firstElement.getElementsByTagName("day2");
                        Element element10 = (Element) elementList10.item(0);
                        NodeList node10 = element10.getChildNodes();
                        String day2 = node10.item(0).getNodeValue();

                        NodeList elementList11 = firstElement.getElementsByTagName("day3");
                        Element element11 = (Element) elementList11.item(0);
                        NodeList node11 = element11.getChildNodes();
                        String day3 = node11.item(0).getNodeValue();

                        NodeList elementList12 = firstElement.getElementsByTagName("day4");
                        Element element12 = (Element) elementList12.item(0);
                        NodeList node12 = element12.getChildNodes();
                        String day4 = node12.item(0).getNodeValue();

                        NodeList elementList13 = firstElement.getElementsByTagName("day5");
                        Element element13 = (Element) elementList13.item(0);
                        NodeList node13 = element13.getChildNodes();
                        String day5 = node13.item(0).getNodeValue();

                        NodeList elementList14 = firstElement.getElementsByTagName("day6");
                        Element element14 = (Element) elementList14.item(0);
                        NodeList node14 = element14.getChildNodes();
                        String day6 = node14.item(0).getNodeValue();

                        NodeList elementList15 = firstElement.getElementsByTagName("day7");
                        Element element15 = (Element) elementList15.item(0);
                        NodeList node15 = element15.getChildNodes();
                        String day7 = node15.item(0).getNodeValue();

                        MyTask ts = new MyTask(context, Consts.Mode.READ_TASK);
                        ts.id = Integer.valueOf(id);
                        ts.active = Boolean.valueOf(active);
                        ts.hour = Integer.valueOf(hour);
                        ts.minute = Integer.valueOf(minute);
                        ts.mobile = Boolean.valueOf(mobile);
                        ts.bluetooth = Boolean.valueOf(bluetooth);
                        ts.wifi = Boolean.valueOf(wifi);
                        ts.ring = Boolean.valueOf(ring);
                        ts.notify =Boolean.valueOf(notify);
                        ts.ring_value = Integer.valueOf(ringv);
                        ts.notify_value = Integer.valueOf(notifyv);
                        ts.onOff = Boolean.valueOf(onoff);
                        ts.day1 = Boolean.valueOf(day1);
                        ts.day2 = Boolean.valueOf(day2);
                        ts.day3 = Boolean.valueOf(day3);
                        ts.day4 = Boolean.valueOf(day4);
                        ts.day5 = Boolean.valueOf(day5);
                        ts.day6 = Boolean.valueOf(day6);
                        ts.day7 = Boolean.valueOf(day7);

                        myDb.addTask(ts);
                    }
                }

                /*  sample read alone values
                stopTray = getBoolValue(doc, "stopTray", false);
                // список
                NodeList nodeList = dom.getElementsByTagName("BlockList");
                Node node = nodeList.item(0);
                NodeList ips = node.getChildNodes();
                for (int i = 0; i < ips.getLength(); i++) {
                    Node n = ips.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println("ip: " + n.getTextContent());
                    }
                }
                */
          }
        } catch (Exception e) {
            Logger.e(Consts.LOG, "setup.read " + e.getMessage());
        }

    }


    public void write(String xmlFile)  {
        Document dom;

        Logger.d(Consts.LOG, "xml write");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            // create the root element
            Element root = dom.createElement("alrTasks");
            // список
            Element tasks = dom.createElement("tasks");
            root.appendChild(tasks);
            // data to write
            ArrayList<MyTask> ar = myDb.getAllTasksData();

            if (ar != null) {
                for (MyTask t : ar) {
                    Element task = dom.createElement("task");

                    Element id = dom.createElement("id");
                    id.appendChild(dom.createTextNode(Integer.toString(t.id)));
                    task.appendChild(id);

                    Element hour = dom.createElement("hour");
                    hour.appendChild(dom.createTextNode(Integer.toString(t.hour)));
                    task.appendChild(hour);

                    Element minute = dom.createElement("minute");
                    minute.appendChild(dom.createTextNode(Integer.toString(t.minute)));
                    task.appendChild(minute);

                    Element active = dom.createElement("active");
                    active.appendChild(dom.createTextNode(Boolean.toString(t.active)));
                    task.appendChild(active);

                    Element mobile = dom.createElement("mobile");
                    mobile.appendChild(dom.createTextNode(Boolean.toString(t.mobile)));
                    task.appendChild(mobile);

                    Element bluetooth = dom.createElement("bluetooth");
                    bluetooth.appendChild(dom.createTextNode(Boolean.toString(t.bluetooth)));
                    task.appendChild(bluetooth);

                    Element wifi = dom.createElement("wifi");
                    wifi.appendChild(dom.createTextNode(Boolean.toString(t.wifi)));
                    task.appendChild(wifi);

                    Element ring = dom.createElement("ring");
                    ring.appendChild(dom.createTextNode(Boolean.toString(t.ring)));
                    task.appendChild(ring);

                    Element notify = dom.createElement("notify");
                    notify.appendChild(dom.createTextNode(Boolean.toString(t.notify)));
                    task.appendChild(notify);

                    Element onoff = dom.createElement("onoff");
                    onoff.appendChild(dom.createTextNode(Boolean.toString(t.onOff)));
                    task.appendChild(onoff);

                    Element ringv = dom.createElement("ring_value");
                    ringv.appendChild(dom.createTextNode(Integer.toString(t.ring_value)));
                    task.appendChild(ringv);

                    Element notifyv = dom.createElement("notify_value");
                    notifyv.appendChild(dom.createTextNode(Integer.toString(t.notify_value)));
                    task.appendChild(notifyv);

                    Element day1 = dom.createElement("day1");
                    day1.appendChild(dom.createTextNode(Boolean.toString(t.day1)));
                    task.appendChild(day1);

                    Element day2 = dom.createElement("day2");
                    day2.appendChild(dom.createTextNode(Boolean.toString(t.day2)));
                    task.appendChild(day2);

                    Element day3 = dom.createElement("day3");
                    day3.appendChild(dom.createTextNode(Boolean.toString(t.day3)));
                    task.appendChild(day3);

                    Element day4 = dom.createElement("day4");
                    day4.appendChild(dom.createTextNode(Boolean.toString(t.day4)));
                    task.appendChild(day4);

                    Element day5 = dom.createElement("day5");
                    day5.appendChild(dom.createTextNode(Boolean.toString(t.day5)));
                    task.appendChild(day5);

                    Element day6 = dom.createElement("day6");
                    day6.appendChild(dom.createTextNode(Boolean.toString(t.day6)));
                    task.appendChild(day6);

                    Element day7 = dom.createElement("day7");
                    day7.appendChild(dom.createTextNode(Boolean.toString(t.day7)));
                    task.appendChild(day7);

                    tasks.appendChild(task);
                }
            }
            // add all
            dom.appendChild(root);
            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                // send DOM to file
                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(xmlFile)));

            } catch (Exception e) {
                Logger.e(Consts.LOG, "xml write: " + e.getMessage());
            }
        } catch (Exception e){
            Logger.e(Consts.LOG, "xml write: " + e.getMessage());
        }
    }
}

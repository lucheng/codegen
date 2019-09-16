package org.chen.codegen.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.chen.codegen.exception.CodegenException;
import org.chen.codegen.model.ConfigModel;
import org.chen.codegen.model.Files;
import org.chen.codegen.model.SubTable;
import org.chen.codegen.model.Table;
import org.chen.codegen.util.FileHelper;
import org.chen.codegen.util.StringUtil;
import org.chen.codegen.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CodeDelete {
	private static String xmlPath;

	public static void setXmlPath(String paramString) {
		xmlPath = paramString;
	}

	public ConfigModel loadXml(String xmlPath) throws Exception {
		String xsdPath = new File("").getAbsolutePath() + "\\codegen.xsd";
		String str2 = XmlUtil.validXmlBySchema(xsdPath,xmlPath);
		if (!"".equals(str2)) {
			throw new CodegenException("XML文件通过XSD文件校验失败:" + str2);
		}
		ConfigModel localConfigModel = new ConfigModel();
		SAXReader localSAXReader = new SAXReader();
		Document localDocument = null;
		try {
			localDocument = localSAXReader.read(new File(xmlPath));
		} catch (DocumentException localDocumentException) {
			throw new CodegenException("读取XML出错!", localDocumentException);
		}
		Element localElement1 = localDocument.getRootElement();
		Properties localProperties = new Properties();
		String str3 = new File("").getAbsolutePath();
		BufferedInputStream localBufferedInputStream = new BufferedInputStream(
				new FileInputStream(str3 + "\\" + "codegen.properties"));
		localProperties.load(localBufferedInputStream);
		String str4 = localProperties.getProperty("charset");
		String str5 = localProperties.getProperty("system");
		localConfigModel.setCharset(str4);
		Iterator<?> localObject1 = localElement1.elementIterator("table");
		while (localObject1.hasNext()) {
			Element localObject2 = (Element)localObject1.next();
			String str6 = localObject2.attributeValue("tableName");
			Table localObject3 = new Table(str6);
			
			Iterator<?> localObject4 = localObject2.elementIterator("variable");
			while (localObject4.hasNext()) {
				Element localObject5 = (Element)localObject4.next();
				String str7 = localObject5.attributeValue("name");
				String str8 = localObject5.attributeValue("value");
				localObject3.getVariable().put(str7, str8);
				if (StringUtil.isNotEmpty(localObject3.getVariable().get("class"))) {
					String localObject6 = StringUtil
							.toFirstLowerCase(localObject3.getVariable().get("class"));
					localObject3.getVariable().put("classVar", localObject6);
				}
			}
			localObject3.getVariable().put("tabname", str6);
			localObject4 = ((Element) localObject2).elementIterator("subtable");
			while (localObject4.hasNext()) {
				Element localObject5 = (Element)localObject4.next();
				String str7 = localObject5.attributeValue("tablename");
				String str8 = localObject5.attributeValue("foreignKey");
				Map<String,String> localObject6 = new HashMap<>();
				Iterator<?> localObject7 = localObject5.elementIterator("variable");
				while (localObject7.hasNext()) {
					Element localElement2 = (Element)localObject7.next();
					String str9 = localElement2.attributeValue("name");
					String str10 = localElement2.attributeValue("value");
					localObject6.put(str9, str10);
				}
				if (StringUtil.isNotEmpty(localObject6.get("class"))) {
					String classVal = StringUtil.toFirstLowerCase(localObject6.get("class"));
					localObject6.put("classVar", classVal);
				}
				localObject6.put("tabname", str7);
				localObject3.addSubTable(str7, str8, localObject6);
			}
			localConfigModel.getTables().add(localObject3);
		}
		Element filesElement = localElement1.element("files");
		Files localObject2 = new Files();
		localConfigModel.setFiles(localObject2);
		String str6 = ((Element) filesElement).attributeValue("baseDir");
		localObject2.setBaseDir(str6);
		Iterator<?> localObject3 = filesElement.elementIterator("file");
		while (localObject3.hasNext()) {
			Element localObject4 = (Element)localObject3.next();
			//String localObject5 = localObject4.attributeValue("refTemplate");
			String str7 = localObject4.attributeValue("filename");
			String str8 = StringUtil.replaceVariable(((Element) localObject4).attributeValue("dir"), str5);
			String localObject6 = localObject4.attributeValue("sub");
			String localObject7 = localObject4.attributeValue("override");
			boolean bool = false;
			if ((StringUtil.isNotEmpty((String) localObject7)) && (((String) localObject7).equals("true"))) {
				bool = true;
			}
			String str9 = ((Element) localObject4).attributeValue("append");
			if ((str9 != null) && (str9.toLowerCase().equals("true"))) {
				String str10 = ((Element) localObject4).attributeValue("insertTag");
				String str11 = ((Element) localObject4).attributeValue("startTag");
				String str12 = ((Element) localObject4).attributeValue("endTag");
				if (str10 == null) {
					str10 = "<!--insertbefore-->";
				}
				if (StringUtil.isEmpty(str11)) {
					str11 = "";
				}
				if (StringUtil.isEmpty(str12)) {
					str12 = "";
				}
				if ((localObject6 != null) && (((String) localObject6).toLowerCase().equals("true"))) {
					localObject2.addFile(null, str7, str8, true, bool, true, str10, str11, str12);
				} else {
					localObject2.addFile(null, str7, str8, false, bool, true, str10, str11,
							str12);
				}
			} else if ((localObject6 != null) && (((String) localObject6).toLowerCase().equals("true"))) {
				localObject2.addFile(null, str7, str8, true, bool, false, "", "", "");
			} else {
				localObject2.addFile(null, str7, str8, false, bool, false, "", "", "");
			}
		}
		return localConfigModel;
	}

	public void deleteFileByConfig(ConfigModel paramConfigModel) throws Exception {
		Files localFiles = paramConfigModel.getFiles();
		String str1 = localFiles.getBaseDir();
		String str2 = paramConfigModel.getCharset();
		for(Table localTable:paramConfigModel.getTables()){
			Map<String,String> localMap = localTable.getVariable();
			List<org.chen.codegen.model.File> localList2 = localFiles.getFileList();
			Iterator<org.chen.codegen.model.File> localObject1 = localList2.iterator();
			while (localObject1.hasNext()) {
				org.chen.codegen.model.File localObject2 = localObject1.next();
				String localObject3 = localObject2.getFilename();
				String localObject4 = localObject2.getDir();
				localObject3 = StringUtil.replaceVariable((String) localObject3, localMap);
				localObject4 = StringUtil.replaceVariable((String) localObject4, localMap);
				String localObject5 = str1 + "\\" + (String) localObject4;
				String localObject6 = StringUtil.replaceVariable(localObject2.getStartTag(),
						localMap);
				String str3 = StringUtil.replaceVariable(localObject2.getEndTag(), localMap);
				boolean bool1 = localObject2.getAppend();
				if (bool1) {
					deleteAppendFile((String) localObject5, (String) localObject3, str2, (String) localObject6, str3);
				} else {
					deleteFile((String) localObject5, (String) localObject3);
				}
			}
			List<SubTable> subTableList = localTable.getSubtable();
			if ((localObject1 != null) && (subTableList.size() != 0)) {
				for (SubTable localObject3:subTableList) {
					Map<String,String> localObject4 = localObject3.getVars();
					for (org.chen.codegen.model.File localObject6:localList2) {
						String str3 = localObject6.getFilename();
						String str4 = localObject6.getDir();
						if (str3.indexOf("{") != -1) {
							String str5 = str3.substring(str3.indexOf('{') + 1, str3.indexOf('}'));
							str3 = (String) (localObject4).get(str5) + str3.substring(str3.indexOf('}') + 1);
						}
						if (str4.indexOf("{") != -1) {
							String str5 = str4.substring(str4.indexOf('{') + 1, str4.indexOf('}'));
							str4 = str4.substring(0, str4.indexOf('{')) + (String) (localObject4).get(str5);
						}
						String str5 = str1 + "\\" + str4;
						boolean bool2 = localObject6.isSub();
						boolean bool3 = localObject6.getAppend();
						/*String str6 = StringUtil.replaceVariable(((ConfigModel.Files.File) localObject6).getStartTag(),
								localObject4);
						String str7 = StringUtil.replaceVariable(((ConfigModel.Files.File) localObject6).getStartTag(),
								localObject4);*/
						if (bool2) {
							if (bool3) {
								deleteAppendFile(str5, str3, str2,
										localObject6.getStartTag(),
										localObject6.getEndTag());
							} else {
								deleteFile(str5, str3);
							}
						}
					}
				}
			}
		}
	}

	private void deleteFile(String paramString1, String paramString2) {
		String str = StringUtil.combilePath(paramString1, paramString2);
		File localFile = new File(str);
		if (localFile.exists()) {
			localFile.delete();
			System.out.println("删除了文件" + paramString2);
		} else {
			System.out.println(paramString2 + "该文件不存在");
		}
		if (!FileHelper.isExistFile(paramString1)) {
			localFile = new File(paramString1);
			localFile.delete();
		}
	}

	private void deleteAppendFile(String paramString1, String paramString2, String paramString3, String paramString4,
			String paramString5) throws Exception {
		String str1 = StringUtil.combilePath(paramString1, paramString2);
		File localFile = new File(str1);
		if (localFile.exists()) {
			String str2 = FileHelper.readFile(str1, paramString3);
			if (str2.indexOf(paramString4) != -1) {
				str2 = str2.substring(0, str2.indexOf(paramString4)).trim() + "\r\n"
						+ str2.substring(str2.indexOf(paramString5) + paramString5.length()).trim();
				localFile.delete();
				localFile = new File(str1);
				FileHelper.writeFile(localFile, paramString3, str2.trim());
				System.out.println("删除了内容" + paramString4 + "-----" + paramString5);
			}
		}
	}

	public void execute() throws Exception {
		String str = new File("").getAbsolutePath();
		if ((xmlPath == null) || (xmlPath == "")) {
			setXmlPath(str + "\\" + "codegenconfig.xml");
		}
		System.out.println("execute:" + xmlPath);
		if (xmlPath == null) {
			throw new CodegenException("未指定XML路径!");
		}
		ConfigModel localConfigModel = loadXml(xmlPath);
		deleteFileByConfig(localConfigModel);
	}

	public static void main(String[] paramArrayOfString) throws Exception {
		CodeDelete localCodeDelete = new CodeDelete();
		//setXmlPath("D:\\workspace\\codegen\\src\\codegenconfig.xml");
		localCodeDelete.execute();
	}
}

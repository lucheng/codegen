package org.chen.codegen.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.chen.codegen.db.IDbHelper;
import org.chen.codegen.exception.CodegenException;
import org.chen.codegen.model.ConfigModel;
import org.chen.codegen.model.Database;
import org.chen.codegen.model.Files;
import org.chen.codegen.model.GenAll;
import org.chen.codegen.model.SubTable;
import org.chen.codegen.model.Table;
import org.chen.codegen.model.TableModel;
import org.chen.codegen.model.Templates;
import org.chen.codegen.util.FileHelper;
import org.chen.codegen.util.StringUtil;
import org.chen.codegen.util.XmlUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Codegen {

	private static String rootPath = "";

	public static void setRootPath(String path) {
		rootPath = path;
	}

	private static String getRootPath() {
		if (rootPath.isEmpty()) {
			rootPath = Codegen.class.getResource(File.separator).getPath();
		}
		if (!rootPath.endsWith(File.separator)) {
			rootPath += File.separator;
		}
		return rootPath;
	}

	/**
	 * 返回Xsd路径
	 * 
	 * @return
	 * @throws CodegenException
	 */
	private static String getXsdPath() throws CodegenException {
		String str = getRootPath() + "codegen.xsd";
		File localFile = new File(str);
		if (!localFile.exists()) {
			throw new CodegenException("schema文件" + str + "不存在");
		}
		return str;
	}

	/**
	 * 返回Xml路径
	 * 
	 * @return
	 * @throws CodegenException
	 */
	private static String getXmlPath() throws CodegenException {
		String str = getRootPath() + "codegenconfig.xml";
		File localFile = new File(str);
		if (!localFile.exists()) {
			throw new CodegenException("请确认配置文件：" + str + "是否存在!");
		}
		return str;
	}

	/**
	 * 返回properties路径
	 * 
	 * @return
	 * @throws CodegenException
	 */
	private static String getPropertiesPath() throws CodegenException {
		String str = getRootPath() + "codegen.properties";
		File localFile = new File(str);
		if (!localFile.exists()) {
			throw new CodegenException("代码生成配置文件" + str + "不存在");
		}
		return str;
	}

	public ConfigModel loadXml() throws Exception {
		String xsdPath = getXsdPath();
		String xmlPath = getXmlPath();
		String validTip = XmlUtil.validXmlBySchema(xsdPath, xmlPath);
		if (!"".equals(validTip)) {
			throw new CodegenException("XML文件通过XSD文件校验失败:" + validTip);
		}

		SAXReader sAXReader = new SAXReader();
		Document document = null;
		try {
			document = sAXReader.read(new File(xmlPath));
		} catch (DocumentException e) {
			throw new CodegenException("读取XML出错!", e);
		}

		Properties properties = new Properties();
		String propertiesPath = getPropertiesPath();
		InputStream inputStream = new BufferedInputStream(new FileInputStream(propertiesPath));
		properties.load(inputStream);

		Element rootElement = document.getRootElement();

		Templates templates = getTemplates(rootElement);

		ConfigModel configModel = new ConfigModel();
		configModel.setVariables(getVariables(rootElement));
		configModel.setTemplates(templates);
		configModel.setCharset(properties.getProperty("charset"));
		configModel.setDatabase(getDatebase(properties));
		configModel.setFiles(getFiles(rootElement, templates));
		configModel.setTables(getTables(rootElement));

		Iterator<?> genAllIterator = rootElement.elementIterator("genAll");
		while (genAllIterator.hasNext()) {
			Element element = (Element) genAllIterator.next();
			String tableNames = element.attributeValue("tableNames");
			GenAll genAll = new GenAll(tableNames);
			configModel.setGenAll(genAll);
			Iterator<?> fileIterator = element.elementIterator("file");

			while (fileIterator.hasNext()) {
				Element fileElement = (Element) fileIterator.next();
				String refTemplate = fileElement.attributeValue("refTemplate");
				String filename = fileElement.attributeValue("filename");
				String extName = fileElement.attributeValue("extName");
				String dir = fileElement.attributeValue("dir");
				String genMode = fileElement.attributeValue("genMode");
				String template = configModel.getTemplates().getTemplate().get(refTemplate);
				if (template == null) {
					throw new CodegenException("找不到模板： " + refTemplate + "!");
				}

				if (("SingleFile".equals(genMode)) && (filename == null)) {
					throw new CodegenException("当SingleFile模式时，必须要填filename!");
				}

				if (("MultiFile".equals(genMode)) && (extName == null)) {
					throw new CodegenException("当MultiFile模式时，必须要填extName!");
				}

				GenAll.File localFile = genAll.new File(template, filename, extName, dir, genMode);
				genAll.getFile().add(localFile);
				Iterator<?> fileVariableIterator = fileElement.elementIterator("variable");
				while (fileVariableIterator.hasNext()) {
					Element localObject17 = (Element) fileVariableIterator.next();
					String name = localObject17.attributeValue("name");
					String value = localObject17.attributeValue("value");
					localFile.getVariable().put(name, value);
				}
			}
		}
		return configModel;
	}

	private Files getFiles(Element rootElement, Templates templates) throws CodegenException {
		Files files = null;
		Element filesElement = rootElement.element("files");
		if (filesElement != null) {
			files = new Files();
			String baseDir = filesElement.attributeValue("baseDir");
			files.setBaseDir(baseDir);
			files.setFileList(getFileList(templates, filesElement));
		}
		return files;
	}

	private List<org.chen.codegen.model.File> getFileList(Templates templates, Element filesElement)
			throws CodegenException {
		List<org.chen.codegen.model.File> files = new ArrayList<>();
		Iterator<?> fileIterator = filesElement.elementIterator("file");
		while (fileIterator.hasNext()) {
			Element fileElement = (Element) fileIterator.next();
			String refTemplate = fileElement.attributeValue("refTemplate");
			String filename = fileElement.attributeValue("filename");
			String dir = fileElement.attributeValue("dir");// StringUtil.replaceVariable(fileElement.attributeValue("dir"),
															// configModel.getVariables());
			String refTemplatePath = templates.getTemplate().get(refTemplate);
			if (refTemplatePath == null) {
				throw new CodegenException("没有找到" + (String) refTemplate + "对应的模版!");
			}
			String sub = fileElement.attributeValue("sub");
			String override = fileElement.attributeValue("override");
			boolean bool = false;
			if ((StringUtil.isNotEmpty(override)) && (override.equals("true"))) {
				bool = true;
			}

			if ((sub != null) && (sub.toLowerCase().equals("true"))) {
				files.add(
						new org.chen.codegen.model.File(refTemplatePath, filename, dir, true, bool, false, "", "", ""));
			} else {
				files.add(new org.chen.codegen.model.File(refTemplatePath, filename, dir, false, bool, false, "", "",
						""));
			}
		}
		return files;
	}

	private Database getDatebase(Properties properties) {
		String dbHelperClass = properties.getProperty("dbHelperClass");
		String url = properties.getProperty("url");
		String username = properties.getProperty("username");
		String password = properties.getProperty("password");
		return new Database(dbHelperClass, url, username, password);
	}

	private List<Table> getTables(Element rootElement) {
		List<Table> tableList = new ArrayList<>();
		Iterator<?> iterator = rootElement.elementIterator("table");

		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			String tableName = element.attributeValue("tableName");
			Table table = new Table(tableName);
			table.setVariable(getTableVariable(element));
			table.setSubtable(getSubTableList(element));
			tableList.add(table);
		}

		return tableList;
	}

	private List<SubTable> getSubTableList(Element tableElement) {

		List<SubTable> list = new ArrayList<>();

		Iterator<?> iterator = tableElement.elementIterator("subtable");
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			String tablename = element.attributeValue("tablename");
			String foreignKey = element.attributeValue("foreignKey");
			Map<String, String> subTableMap = getTableVariable(element);
			list.add(new SubTable(tablename, foreignKey, subTableMap));
		}
		return list;
	}

	/**
	 * 获得table的变量
	 * 
	 * @param tableElement
	 * @return
	 */
	private Map<String, String> getTableVariable(Element tableElement) {

		Map<String, String> map = getMapByElement(tableElement, "variable", "name", "value");

		if (StringUtil.isNotEmpty(map.get("class")) && StringUtil.isEmpty(map.get("classVar"))) {
			String classVar = StringUtil.toFirstLowerCase(map.get("class"));
			map.put("classVar", classVar);
		}
		return map;
	}

	/**
	 * 获取系统变量
	 * 
	 * @param configModel
	 * @param rootElement
	 * @return
	 * @author luCheng
	 */
	private Map<String, String> getVariables(Element rootElement) {
		Element variables = rootElement.element("variables");
		return getMapByElement(variables, "variable", "name", "value");
	}

	private Map<String, String> getMapByElement(Element rootElement, String subName, String keyName, String valueName) {
		Map<String, String> map = new HashMap<>();
		Iterator<?> iterator = rootElement.elementIterator(subName);
		while (iterator.hasNext()) {
			Element variable = (Element) iterator.next();
			String key = variable.attributeValue(keyName);
			String value = variable.attributeValue(valueName);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 获取模板属性
	 * 
	 * @param rootElement
	 * @return
	 */
	private Templates getTemplates(Element rootElement) {
		String templatePath = getRootPath() + "template";
		Element templatesElement = rootElement.element("templates");

		Templates templates = new Templates(templatePath);
		templates.setTemplate(getMapByElement(templatesElement, "template", "id", "path"));
		return templates;
	}

	private IDbHelper getDbHelper(ConfigModel configModel) throws CodegenException {
		IDbHelper localIDbHelper = null;
		String str = configModel.getDatabase().getDbHelperClass();
		try {
			localIDbHelper = (IDbHelper) Class.forName(str).newInstance();
		} catch (InstantiationException localInstantiationException) {
			throw new CodegenException("指定的dbHelperClass：" + str + "无法实例化，可能该类是一个抽象类、接口、数组类、基本类型或 void, 或者该类没有默认构造方法!",
					localInstantiationException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new CodegenException("指定的dbHelperClass： " + str + "没有默认构造方法或不可访问!", localIllegalAccessException);
		} catch (ClassNotFoundException localClassNotFoundException) {
			throw new CodegenException("找不到指定的dbHelperClass:" + str + "!", localClassNotFoundException);
		}
		localIDbHelper.setUrl(configModel.getDatabase().getUrl(), configModel.getDatabase().getUsername(),
				configModel.getDatabase().getPassword());
		return localIDbHelper;
	}

	private List<TableModel> getTableModelList(IDbHelper dbHelper, ConfigModel configModel) throws CodegenException {
		ArrayList<TableModel> tableModelList = new ArrayList<>();
		for (Table table : configModel.getTables()) {
			String tableName = table.getTableName();
			TableModel tableModel = dbHelper.getByTable(tableName);
			tableModel.setVariables(table.getVariable());
			tableModel.setSub(false);
			List<SubTable> subTableList = table.getSubtable();

			for (SubTable subTable : subTableList) {
				Map<String, String> localMap = subTable.getVars();
				TableModel subTableModel = dbHelper.getByTable(subTable.getTableName());
				subTableModel.setVariables(localMap);
				subTableModel.setSub(true);
				subTableModel.setForeignKey(subTable.getForeignKey());
				tableModel.getSubTableList().add(subTableModel);
				tableModelList.add(subTableModel);
			}
			tableModelList.add(tableModel);
		}
		return tableModelList;
	}

	private void appendFile(String paramString1, String paramString2, String paramString3, String paramString4,
			Configuration paramConfiguration, Object paramMap, String paramString5, String paramString6,
			String paramString7) throws IOException, TemplateException {
		String str1 = StringUtil.combilePath(paramString1, paramString2);
		File localFile = new File(paramString1, paramString2);
		StringWriter writer = new StringWriter();
		Template localTemplate = paramConfiguration.getTemplate(paramString3, paramString4);
		localTemplate.process(paramMap, writer);
		String str2 = writer.toString();
		int i = 0;
		String str3 = "";
		if (localFile.exists()) {
			str3 = FileHelper.readFile(str1, paramString4);
			if ((StringUtil.isNotEmpty(paramString6)) && (StringUtil.isNotEmpty(paramString7))
					&& (StringUtil.isExist(str3, paramString6, paramString7))) {
				str3 = StringUtil.replace(str3, paramString6, paramString7, str2);
				i = 1;
			}
		}
		if (i == 0) {
			if ((StringUtil.isNotEmpty(paramString6)) && (StringUtil.isNotEmpty(paramString7))) {
				str2 = paramString6.trim() + "\r\n" + str2 + "\r\n" + paramString7.trim();
			}
			if (str3.indexOf(paramString5) != -1) {
				String[] arrayOfString = FileHelper.getBySplit(str3, paramString5);
				str3 = arrayOfString[0] + str2 + "\r\n" + paramString5 + arrayOfString[1];
			} else {
				str3 = str3 + "\r\n" + str2;
			}
		}
		FileHelper.writeFile(localFile, paramString4, str3);
	}

	private void genTableByConfig(IDbHelper dbHelper, ConfigModel configModel, Configuration configuration)
			throws CodegenException {
		try {
			List<TableModel> tableModelList = getTableModelList(dbHelper, configModel);
			if ((tableModelList == null) || (tableModelList.size() == 0)) {
				System.out.println("没有指定生成的表!");
				return;
			}
			Files files = configModel.getFiles();
			String baseDir = files.getBaseDir();
			if (files.getFileList() == null || files.getFileList().isEmpty()) {
				System.out.println("没有指定生成的文件!");
				return;
			}
			System.out.println("*********开始生成**********");

			for (TableModel tableModel : tableModelList) {
				String tableName = tableModel.getTableName();
				Map<String, String> variablesMap = tableModel.getVariables();

				variablesMap.putAll(configModel.getVariables());

				boolean sub = tableModel.getSub();

				for (org.chen.codegen.model.File file : files.getFileList()) {
					String filename = file.getFilename();
					String startTag = file.getStartTag();
					String endTag = file.getEndTag();
					boolean isSub = file.isSub();
					boolean isOverride = file.isOverride();
					if ((sub != true) || (isSub)) {
						startTag = StringUtil.replaceVariable(startTag, tableName);
						endTag = StringUtil.replaceVariable(endTag, tableName);
						String dir = file.getDir();
						filename = StringUtil.replaceVariable(filename, variablesMap);
						dir = StringUtil.replaceVariable(dir, variablesMap);
						dir = StringUtil.combilePath(baseDir, dir);
						Map<String, Object> localHashMap = new HashMap<String, Object>();
						localHashMap.put("model", tableModel);
						localHashMap.put("vars", configModel.getVariables());
						localHashMap.put("date", new Date());
						if (file.getAppend()) {
							appendFile(dir, filename, file.getTemplate(), configModel.getCharset(), configuration,
									localHashMap, file.getInsertTag(), startTag, endTag);
						} else if (isOverride) {
							genFile(dir, filename, file.getTemplate(), configModel.getCharset(), configuration,
									localHashMap);
						} else {
							File localFile1 = new File(dir + "\\" + filename);
							if (!localFile1.exists()) {
								genFile(dir, filename, file.getTemplate(), configModel.getCharset(), configuration,
										localHashMap);
							}
						}
						System.out.println(filename + " 生成成功!");
					}
				}
			}
			System.out.println("*********所有文件生成成功!**********");
		} catch (IOException localIOException) {
			throw new CodegenException(localIOException);
		} catch (TemplateException localTemplateException) {
			throw new CodegenException("freemarker执行出错!", localTemplateException);
		}
	}

	private void genFile(String paramString1, String paramString2, String paramString3, String paramString4,
			Configuration paramConfiguration, Map<String, Object> paramMap) throws IOException, TemplateException {
		File localFile = new File(paramString1, paramString2);
		if (!localFile.exists()) {
			if (!localFile.getParentFile().exists()) {
				localFile.getParentFile().mkdirs();
			}
			localFile.createNewFile();
		}
		OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(new FileOutputStream(localFile),
				paramString4);
		Template localTemplate = paramConfiguration.getTemplate(paramString3, paramString4);
		localTemplate.process(paramMap, localOutputStreamWriter);
	}

	private void getAllTable(IDbHelper dbHelper, ConfigModel configModel, Configuration configuration)
			throws CodegenException, IOException, TemplateException {
		GenAll localGenAll = configModel.getGenAll();
		if (localGenAll == null) {
			return;
		}
		List<String> tableList = null;
		if (localGenAll.getTableNames() == null) {
			tableList = dbHelper.getAllTable();
		} else {
			tableList = new ArrayList<>();
			for (String tableName : localGenAll.getTableNames().replaceAll(" ", "").split(",")) {
				if (tableName.length() > 0) {
					tableList.add(tableName);
				}
			}
		}
		int i = localGenAll.getFile().size();
		if (i == 0) {
			return;
		}
		System.out.println("----------------生成多表开始------------------");

		for (GenAll.File localFile : localGenAll.getFile()) {
			if ("MultiFile".equals(localFile.getGenMode())) {
				Iterator<String> localObject2 = tableList.iterator();
				while (localObject2.hasNext()) {
					String localObject3 = localObject2.next();
					TableModel localObject4 = dbHelper.getByTable(localObject3);
					Map<String, Object> localObject5 = new HashMap<>();
					localObject5.put("model", localObject4);
					localObject5.put("date", new Date());
					genFile(localFile.getDir(), (String) localObject3 + "." + localFile.getExtName(),
							localFile.getTemplate(), configModel.getCharset(), configuration, localObject5);
					System.out.println(localObject3 + "." + localFile.getExtName() + " 生成成功!");
				}
			} else if ("SingleFile".equals(localFile.getGenMode())) {
				List<TableModel> tableModelList = new ArrayList<>();
				Iterator<String> localObject3 = tableList.iterator();
				while (localObject3.hasNext()) {
					String localObject4 = localObject3.next();
					TableModel localObject5 = dbHelper.getByTable((String) localObject4);
					tableModelList.add(localObject5);
				}
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("models", tableModelList);
				tempMap.put("date", new Date());
				genFile(localFile.getDir(), localFile.getFilename(), localFile.getTemplate(),
						configModel.getCharset(), configuration, tempMap);
				System.out.println(localFile.getFilename() + " 生成成功!");
			}
		}
		System.out.println("----------------生成多表结束------------------");
	}

	public void execute() throws Exception {
		try {
			String str = getRootPath() + "codegenconfig.xml";
			File file = new File(str);
			if (!file.exists()) {
				throw new CodegenException("请确认配置文件：" + str + "是否存在!");
			}
			ConfigModel configModel = loadXml();
			Configuration configuration = new Configuration();
			configuration.setDirectoryForTemplateLoading(new File(configModel.getTemplates().getBasepath()));
			IDbHelper dbHelper = getDbHelper(configModel);
			genTableByConfig(dbHelper, configModel, configuration);
			getAllTable(dbHelper, configModel, configuration);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Codegen.class.getResource("/"));
		Codegen codegen = new Codegen();
		codegen.execute();
	}
}

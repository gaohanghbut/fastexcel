# fastexcel介绍
基于apache-poi，利用注解与Serializer/Deserializer实现excel的导入导出

应用中经常会遇到将数据导出为excel或者将excel导入的场景，针对不是非常的表单，不需要每次都需要手动写，容易想到利用注解来处理。

#使用方式：
定义bean:  
public class Person {  
&nbsp;&nbsp;&nbsp;&nbsp;@Column("编号")  
&nbsp;&nbsp;&nbsp;&nbsp;private int id;  
&nbsp;&nbsp;&nbsp;&nbsp;@Column("姓名")  
&nbsp;&nbsp;&nbsp;&nbsp;private String name;  
}  
利用@Column可以自定义序列化与反序列化方式，指定serializer和deserializer即可。  

导出:  
List<Person> personList = xxx;  
Workbook workbook =  
&nbsp;&nbsp;&nbsp;&nbsp;WorkbookBuilder.withWorkbook(new XSSFWorkbook())  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;.columnWidth(12)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;.sheetName("人名")  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;.data(personList)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;.build();  
            
导入：  
try (InputStream in = xxx) {  
&nbsp;&nbsp;&nbsp;&nbsp;List<Person> persionList = SimpleExcelTable.createXlsx(in).map(Person.class);  
} catch (IOException e) {  
&nbsp;&nbsp;&nbsp;&nbsp;e.printStackTrace();  
}  


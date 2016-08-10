# 匯出排程服務說明

本服務程式使用JAVA語言，建構工具為MAVEN，主要功能為定時於指定DB執行SQL，並將執行結果匯出為UTF-8(含BOM)編碼的CSV檔案。

### 開發環境

+ 必須安裝JDK7以上
+ 必須安裝MAVEN

### 執行環境

+ 必須安裝JRE7以上

### 如何啟動

從**Source**啟動: 
```
mvn clean spring-boot:run
```
從**Jar**啟動: 
```
java -jar [JAR_NAME].jar
```

### 如何進入管理介面

```
http://127.0.0.1:8080
```

### 如何打包

在專案位置下，執行：
```
mvn clean package
```
輸出位置：
```
專案位置\target\batch.jar
```

### 如何修改預設值

修改 **src/main/resources/application.yml**

### 匯出的檔案位置

${JAR_LOCATION}/export/${資料集ID}/${匯出時間}.csv

### 已支援的資料庫

| 資料庫 | 驅動名稱 |
| ----- | ----- |
| ORACLE | oracle.jdbc.OracleDriver |
| Microsoft SQL Server | com.microsoft.sqlserver.jdbc.SQLServerDriver |
| MySQL | com.mysql.jdbc.Driver |

### 增加新的JDBC引用

編輯**pom.xml**，增加
```
<dependency>
	<groupId>...</groupId>
	<artifactId>...</artifactId>
	<version>...</version>
</dependency>
```

### 手動安裝新的JAR檔

```
mvn install:install-file -Dfile=[jar檔位置] -DgroupId=[group id] -DartifactId=[artifact id] -Dversion=[version] -Dpackaging=jar -DlocalRepositoryPath=[專案位置/repo] -DcreateChecksum=true
```

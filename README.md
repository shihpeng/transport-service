# 服務化demo

為了配合未來業務的快速發展，系統內部架構的服務化與領域化是良好系統架構的第一步。因此我們的技術棧必須要能滿足幾個重點需求：1) 經企業廣泛使用驗證、2) 生態完善、3) 網路資源眾多者。Python/Django一開始是用以服務單體Web應用開發而生的，對於服務化架構天生有著支持較弱的缺點。在survey Django應用服務化的過程中，我們發現Python/Django應用在企業級別的需求有著很大的不足，例如uWSGI不兼容任何RPC協議，消息與tracing等middleware的支持則對程式碼有著極強的侵入性等，這對後續的開發工作都是戰和問題。

> REST API可作為開放open API時的協議使用，但企業系統內部服務間使用HTTP協議是巨大的性能耗損，一般來說gRPC/Dubbo/Thrift等RPC協議的效能約是HTTP1.x的8~10倍。因此企業內部應用的通訊大多使用各類型的RPC協議而不會採用HTTP。

此demo的結構100%由DI (dependency injection) 的方式完成，DI一詞雖然常出現在各類經典書籍中，但先前大多只會在靜態語言的技術棧中發現。Python由於其動態型別的特徵，很少會使用到DI。但DI模式能極大的增加程式模組的鬆耦合 (loosely coupled) 程度，並增加模組內聚力 (cohesion)，近年來的Python社群也開始在推動DI的使用 (例如Google的Pinject等)。

此demo包含了Spring Boot、Spring MVC/Restful API、MyBatis、Dubbo、Kafka以及Zipkin的使用，涵蓋了資料庫連結與ORM、API服務化、消息處理以及tracing ID的使用等主題，同時程式碼量也非常精簡，去除imports與空行不到150行。



# RPC框架: Dubbo

## RPC: gRPC vs Dubbo

gRPC號稱其跨語言跨平台，並使用HTTP/2而非自有協議，但其問題是其service介面碼生成過程太過麻煩、不便管理，而且使用上對業務代碼的侵入非常嚴重，即便使用了目前公認比較便捷的 LogNet/grpc-spring-boot-starter 還是有一些boilerplate code出現。

Dubbo的設計則是一開始就以非代碼侵入為首要目標，使用上便捷非常多。以demo內容來看是直接0侵入的，加上annotation就可用。Dubbo的問題是要達成高可用的話需要另外維護註冊中心，而且客戶端在其他的語言上支持不太好。這兩點可以分別透過直連和外加Rest層解決。

gRPC與Dubbo的效能基本不相上下，都很優越。基於以上幾點，選用Dubbo。

## Service Provider

假設我們已有interface `QRCodeValidateService`

```java
public interface QRCodeValidateService {

     Result<String> validate(String qrCodeString);
}
```

要為其建立service provider對外提供服務只需要要在其實現類加上Dubbo的@Service annotation，並在`application.properties`中加入dubbo掃描設置即可。

```
# application.properties
dubbo.scan.base-packages = com.jkopay.industry.transport.dubbo
dubbo.registry.address = N/A
```

```java
import org.apache.dubbo.config.annotation.Service;

@Service
public class QRCodeValidateServiceImpl implements QRCodeValidateService {
  ...
}  
```

如果需要多台機器提供服務或需要註冊中心時，會需要額外的provider配置。

## Service Consumer

消費者的使用非常簡單，一般注入依賴時用的@Autowired改為Dubbo的@Reference即可。

```java
@Reference(url="dubbo://localhost:20880")
private QRCodeValidateService qrCodeValidateService;
```

## 延伸閱讀

https://shekhargulati.com/2018/12/15/getting-started-with-apache-dubbo-and-spring-boot/



# Kafka

## Producer

引入stater包之後可以直接使用。

```java
@Autowired
private KafkaTemplate<Object, Object> kafkaTemplate;
...
kafkaTemplate.send("payment-completed", "this is the message");
```



## Consumer

最基本使用方式只要在處理方法上加上`@KafkaListener` annotation，標明要消費的topic即可。

```java
// PaymentEventListener.java
@Component
public class PaymentEventListener {

    @KafkaListener(id="id01", topics="payment-completed")
    public void doWork(String record) {
        System.out.println(record);
    }
}
```



# 資料庫與ORM: MyBatis

## 使用資料庫

如果沒有特殊需求，在Spring Boot中使用資料庫只需要引入jdb starter包，並在應用配置檔中設置連線訊息就可以了。

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

```
# application.properties
spring.datasource.url = jdbc:mysql://127.0.0.1:3306/transport_service
spring.datasource.username = root
spring.datasource.password = root1234
spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver
```



## MyBatis

過去SSH/J2EE時代，大家慣用的是Hibernate或兼容JPA的ORM，但後來發現這類ORM太過厚重、學習曲線陡峭，而且自動生成的SQL效能不佳，這對query效能有著嚴格要求的現代企業應用已不合時宜，因此大多轉移至較輕量級的ORM系統：開發者自行編寫SQL query，ORM僅負責object mapping工作。

同樣的，引入starter包，然後在希望進行object mapping工作的interface上標上`@Mapping` annotation與SQL query即可。

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
```

```java
// UserMapper.java
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(gmt_created, name) VALUES (CURRENT_TIMESTAMP(), #{name})")
    @Options(useGeneratedKeys=true, keyProperty="id")
    int insert(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(long id);
}
```



# Tracing: Zipkin

## Zipkin Server

https://github.com/openzipkin/zipkin/tree/master/zipkin-server

要收集zipkin數據之前需要先啟用Zipkin server，使用非常簡單，下載jar包之後用java啟動就可以了。

## Zipkin Client

引入starter包，在`application.properties`中加上Zipkin server的位置即可。

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

```
# application.properties
spring.zipkin.base-url = http://localhost:9411/
```


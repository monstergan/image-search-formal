# image-search-formal

基于 **Spring Boot 2.7.x + JDK 1.8 + MyBatis Plus + MySQL 8 + RabbitMQ + 阿里云图搜 SDK** 的正式版项目骨架。

## 技术栈
- JDK 1.8
- Spring Boot 2.7.18
- MyBatis Plus 3.5.5
- MySQL 8
- RabbitMQ
- Alibaba Cloud Image Search Java SDK

## 启动前准备
1. 新建 MySQL 数据库，例如：`image_search_demo`
2. 执行 `src/main/resources/schema.sql`
3. 按需执行 `src/main/resources/data.sql`
4. 修改 `application.yml`
   - `spring.datasource.*`
   - `spring.rabbitmq.*`
   - `image-search.access-key-id`
   - `image-search.access-key-secret`
   - `image-search.instance-name`
   - `image-search.region-id`
   - `image-search.endpoint`
5. 确保 RabbitMQ 已创建并可连接

## 启动命令
```bash
mvn clean package
java -jar target/image-search-formal-1.0.0.jar
```

## 主要接口
### 1. 图片新增/更新同步
POST `/image-search/admin/sync`

```json
{
  "sku": "SKU001",
  "spu": "SPU001",
  "imageId": 10001,
  "imageUrl": "https://example.com/demo.jpg",
  "mimeType": "image/jpeg",
  "width": 800,
  "height": 800,
  "mainFlag": true,
  "platformId": 1,
  "siteId": 100,
  "categoryId": 5,
  "categoryPath": "服饰/女装"
}
```

### 2. 图片删除
POST `/image-search/admin/delete`

```json
{
  "sku": "SKU001",
  "imageId": 10001,
  "imageUrl": "https://example.com/demo.jpg"
}
```

### 3. 图搜查询
POST `/image-search/query`

```json
{
  "imageUrl": "https://example.com/query.jpg",
  "scoreThreshold": 0.85,
  "topK": 10,
  "platformId": 1,
  "siteId": 100,
  "categoryId": 5,
  "mainFlag": true
}
```

## 说明
- 本项目已经改成正式版方向，不再使用 H2/JPA/本地异步线程池。
- 改为 MyBatis Plus + MySQL + RabbitMQ + 阿里云真实 SDK。
- 由于当前运行环境未连接你的 MySQL、RabbitMQ 和阿里云实例，所以请在本地补全配置后启动。

## 官方参考
- 阿里云图像搜索 SDK 文档：<https://help.aliyun.com/zh/image-search/developer-reference/python-sdk-1>
- Java SDK Maven 包：<https://mvnrepository.com/artifact/com.aliyun/imagesearch20201214>

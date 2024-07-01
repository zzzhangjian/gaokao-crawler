## Step1 建库
首先需要有一个MySQL数据库

## Step2 建表
执行database文件夹下的[table.sql](https://github.com/vasthan/gaokao-crawler/blob/master/database/table.sql)即可

## Step3 导入项目到IDE
依赖比较少，应该不会有什么环境问题。
然后把项目里的[mybatis-config.xml](https://github.com/vasthan/gaokao-crawler/blob/master/src/main/resources/mybatis-config.xml)数据库连接信息改成你自己的

## Step4 执行
运行App.main()方法。等待执行结束，就可以去数据库里面分析数据了😋

## 声明
数据来源：[高考数据库](https://gkcx.eol.cn/)，对于数据的准确性请自行考量。

### 学校推荐
    https://static-data.gaokao.cn/www/2.0/samescore3/2023/470.json

### 学校-年-省份数据
    https://static-data.gaokao.cn/www/2.0/schoolprovincescore/2401/2022/41.json

### 专业
    https://static-data.gaokao.cn/www/2.0/school/62/pc_special.json

### 学校介绍
    https://static-data.gaokao.cn/www/2.0/school/62/info.json

### 就业情况
    https://static-data.gaokao.cn/www/2.0/school/62/pc_jobdetail.json

### 学校代码（报考代码）
    全部学校
    https://static-data.gaokao.cn/www/2.0/school/list_v2.json
    报考代码
    https://static-data.gaokao.cn/www/2.0/school/school_code.json
    专业
    https://static-data.gaokao.cn/www/2.0/info/linkage.json

### 批次信息
    https://static-data.gaokao.cn/www/2.0/config/dicprovince/dic.json

### 省份
    https://static-qiniu.mnzy.gaokao.cn/zsgk-volunteer/data/zhao-config.json
### 专业计划
    https://static-data.gaokao.cn/www/2.0/school/2401/dic/specialplan.json
### 类型转换
    https://static-data.gaokao.cn/www/2.0/config/dicprovince/dicname2id.json
### 学校专业分数线
    https://static-data.gaokao.cn/www/2.0/school/2401/dic/professionalscore.json
### 预测分数
    https://static-data.gaokao.cn/www/2.0/school/2401/benchmarkScore.json
### 成绩分段
    https://mnzy.gaokao.cn/api/hint/query/rank?score=470&batch=%E6%9C%AC%E7%A7%91&province=%E6%B2%B3%E5%8D%97&classify=%E7%90%86%E7%A7%91&optional=%E7%90%86%E7%A7%91
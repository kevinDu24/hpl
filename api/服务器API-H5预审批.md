 ## 接口更新:
### 1. 添加"## 1. 自动预审批提交(首次提交时调用该接口)
### 2. 添加"## 2. 手动调用主系统查询状态接口
### 3. 添加"## 3. 录单提交接口
### 4. 添加"## 4. 结束页状态查询接口


# 一、术语
## 1. 请求方式
- GET
- POST
- PUT
- DELETE

## 2. 入参位置
- Request Body
- Request Parameter
- Path Variable
每一个接口不限于使用一种入参位置，具体参考每个接口的说明。

## 3. domain
- `ip`:222.73.56.22
- `port`:80

## 4. 全局返回值
- `status`: 请求状态（成功为SUCCESS,失败为ERROR）
- `error`: 请求失败时，服务器的错误信息
- `data`: 返回的数据

示例：
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": {
  }
}
```


# 二、接口

## 1.自动预审批提交(首次提交时调用该接口)
- url: `http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `.url`: 主系统接口名(必传)
- 入参位置: Request Body
- 入参参数:
  - `userid`: 经销商编号(用户名)
  - `applyId`:申请编号
  - `name`: 姓名
  - `idty`: 身份证号码
  - `mobile`: 手机号
  - `contact1Name`: 联系人1姓名
  - `contact1Mobile`: 联系人1手机
  - `contact1Relationship`: 联系人1关系
  - `contact2Name`: 联系人2姓名
  - `contact2Mobile`: 联系人2手机
  - `contact2Relationship`: 联系人2关系
  - `mateName`: 配偶姓名
  - `mateMobile`: 配偶手机
  - `mateIdty`: 配偶证件号码
  - `firstCommitTime`: 首次提交时间
  - `domicile`: 居住地
  - `workUnit`: 工作单位
  - `salary`: 年薪

- 入参示例
- 入参示例:
   http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!?.url=autoFinancingPreApplyH5
```javascript
{
	"userid": "wangyuetest",
    "applyId": "ead321d82c8f4041a690d5eeea7b0e70",
    "name": "wangyue",
    "idty": "341003199922221123",
    "mobile": "11111111111",
    "contact1Name":"雨",
    "contact1Mobile": "18055313782",
    "contact1Relationship": "本人",
    "contact2Name": "小雪",
    "contact2Mobile": "15055100510",
    "contact2Relationship": "配偶",
    "firstCommitTime": "20160909",
    "mateName": "wangyuetest1",
    "mateMobile": "15005123123",
    "mateIdty": "341003123399922221",
    "domicile": "上海市浦东新区金科路2889弄A座",
    "workUnit": "上海领友",
    "salary": "9999999"
}
```
- 出参参数:
  - `applyId`: 申请编号(用户名)
  - `applyResult`:申请状态
  - `applyResultReason`: 申请结果原因
  - `userid`: 经销商编号(用户名)
  - `applyIdOfMaster`: 主系统申请编号
- 出参示例:
```javascript
{
  "applyId": "ead321d82c8f4041a690d5eeea7b0e70",
  "applyResult": "0001",
  "applyResultReason": "",
  "userid": "wangyuetest",
  "applyIdOfMaster": "ead321d82c8f4041a690d5eeea7b0e70"
}
```


## 2.手动调用主系统查询状态接口
- url: `http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `.url`: 主系统接口名称(必传)
- 入参示例:
    http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!?.url=qureyFinancePreApplyResult
- 出参参数:
- 出参示例:
```javascript
{
  "status": "SUCCESS",
  "error": "",
  "data": null
}
```

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
- 请求方式: `POST`
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
  - `mateCompany`: 配偶工作单位
  - `mateAddress`: 配偶地址
  - `firstCommitTime`: 首次提交时间
- 入参示例
- 入参示例:
   http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!?.url=autoFinancingPreApply
```javascript
{
	"contact2Name":"袁中平",
	"address":"",
	"mateIdty":"",
	"mateMobile":"",
	"mobile":"13865956846",
	"contact1Relationship":"家人",
	"contact2Relationship":"家人",
	"contact1Mobile":"13780608610",
	"contact1Name":"袁单霞",
	"userid":"13865956846",
	"applyId":"13865956846",
	"contact2Mobile":"13791470610",
	"idty":"372925198902231528",
	"mateName":"",
	"name":"袁震霞",
	"company":"",
	"firstCommitTime":"2017-07-24"
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
    "applyId": "13865956846",
    "applyResult": "0001",(000-->1000;0001-->100;0002-->1100;0003-->300)
    "applyResultReason": "",
    "userid": "13865956846",
    "applyIdOfMaster": "13865956846"
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

## 3.录单提交接口
- url: `http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `.url`: 接口名(必传)
- 入参位置: Request Body
- 入参参数:
  - `applyId`: 申请编号
  - `vehicleId`: 所选车辆名称
  - `vehicleColor`: 所选车辆颜色
  - `financeType`: 套餐类型(A套餐，B套餐)
  - `company`: 工作单位
  - `workAdd`: 工作地址
  - `liveAdd`: 实际居住地
  - `localAdd`: 户籍地址
  - `bankUser`: 还贷借记卡户名
  - `bankCard`: 还贷借记卡卡号
  - `bankName`: 还贷借记卡开户行
  - `identifyFrontImg`: 身份证正面
  - `identifyBehindImg`: 身份证反面
  - `driveFrontImg`: 驾驶证正面
  - `driveBehindImg`: 驾驶证反面
  - `workCertificate`: 工作证明附件
  - `creditProxy`: 征信委托授权书
  - `driveLicenceName`: 驾驶证姓名
  - `driveLicenceEffectiveTerm`: 准驾车型
  - `quasiDriveType`: 有效期
  - `driveLicenceNum`: 驾驶证档案号
  - `driveLicenceFn`: 驾驶证号码
- 入参示例:
    http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!?.url=manualFinancingPreApplyCJ
```javascript
{
	"applyId":"18055313782",
	"vehicleId":"帕萨塔",
	"vehicleColor":"内黑外白",
	"financeType":"A套餐",
	"company":"上海领友",
	"workAdd":"新华国际广场",
	"liveAdd":"华府骏苑",
	"localAdd":"舒城",
	"bankUser":"杜雨生",
	"bankCard":"1234567890123456789",
	"bankName":"光大银行",
	"identifyFrontImg":"http://192.168.1.199:8088/30.png   ",
	"identifyBehindImg":"http://192.168.1.199:8088/30.png   ",
	"driveFrontImg":"http://192.168.1.199:8088/30.png   ",
	"driveBehindImg":"http://192.168.1.199:8088/30.png   ",
	"creditProxy":"http://192.168.1.199:8088/30.png   ",
	"workCertificate":"http://192.168.1.199:8088/30.png   ",
	"driveLicenceName":"杜雨生",
	"driveLicenceEffectiveTerm":"20160201",
	"quasiDriveType":"A1",
	"driveLicenceNum":"231231123123",
	"driveLicenceFn":"1231231"
}
```
- 出参参数:
- 出参示例:
```javascript
{
  "error": "",
  "status": "SUCCESS",
  "data": null
}
```

## 4. 结束页状态查询接口
- url: `http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!`
- 请求方式: `GET`
- 入参位置: `Request Parameter`
- 入参参数:
- `.url`: 接口名
- `basqbh`: 申请编号
- 入参示例:
    http://116.236.234.246:7082/XFTM_KF/app/lywxapi.htm!?.url=queryContractState&basqbh=3521799

- 出参参数:
  - `BASQXM`: 申请人姓名
  - `BASQBH`: 申请编号
  - `XTCZRY`: 配偶证件号码
  - `BAGQZT`: 工作单位
  - `BATHYY`: 地址
  - `BASHRQ`: 地址
- 出参示例:
```javascript
{
 "res": {
     "resultMsg": "",
     "resultCode": "",
     "isSuccess": "true"
   },
   "BASQXM": "陈显忠",
   "BASQBH": "3521799",
   "contractstatelist": [
     {
       "XTCZRY": "gongxujia",
       "BAGQZT": "",
       "BASQZT": "审核已通过",
       "BATHYY": "",
       "BASHRQ": "20150417162808"
     },
     {
       "XTCZRY": "wangshuting",
       "BAGQZT": "",
       "BASQZT": "申请待审核",
       "BATHYY": "",
       "BASHRQ": "20150417133401"
     }
   ]
}
```

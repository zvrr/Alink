# Fm 分类训练组件

## 功能介绍

* Fm 分类算法是一个二分类算法
* 算法支持稀疏、稠密两种数据格式
* 支持带样本权重的训练

## 参数说明


| 名称 | 中文名称 | 描述 | 类型 | 是否必须？ | 默认值 |
| --- | --- | --- | --- | --- | --- |
| labelCol | 标签列名 | 输入表中的标签列名 | String | ✓ |  |
| vectorCol | 向量列名 | 向量列对应的列名，默认值是null | String |  | null |
| weightCol | 权重列名 | 权重列对应的列名 | String |  | null |
| epsilon | 收敛阈值 | 迭代方法的终止判断阈值，默认值为 1.0e-6 | Double |  | 1.0E-6 |
| featureCols | 特征列名数组 | 特征列名数组，默认全选 | String[] |  | null |
| withIntercept | 是否有常数项 | 是否有常数项，默认true | Boolean |  | true |
| withLinearItem | 是否含有线性项 | 是否含有线性项 | Boolean |  | true |
| numFactor | 因子数 | 因子数 | Integer |  | 10 |
| lambda0 | 常数项正则化系数 | 常数项正则化系数 | Double |  | 0.0 |
| lambda1 | 线性项正则化系数 | 线性项正则化系数 | Double |  | 0.0 |
| lambda2 | 二次项正则化系数 | 二次项正则化系数 | Double |  | 0.0 |
| numEpochs | epoch数 | epoch数 | Integer |  | 10 |
| learnRate | 学习率 | 学习率 | Double |  | 0.01 |
| initStdev | 初始化参数的标准差 | 初始化参数的标准差 | Double |  | 0.05 |

## 脚本示例
#### 运行脚本
```python
from pyalink.alink import *
import numpy as np
import pandas as pd
data = np.array([
    ["1:1.1 3:2.0", 1.0],
    ["2:2.1 10:3.1", 1.0],
    ["1:1.2 5:3.2", 0.0],
    ["3:1.2 7:4.2", 0.0]])
df = pd.DataFrame({"kv": data[:, 0], 
                   "label": data[:, 1]})

input = dataframeToOperator(df, schemaStr='kv string, label double', op_type='batch')
# load data
dataTest = input
fm = FmClassifierTrainBatchOp().setVectorCol("kv").setLabelCol("label")
model = input.link(fm)

predictor = FmClassifierPredictBatchOp().setPredictionCol("pred")
predictor.linkFrom(model, dataTest).print()
```
#### 运行结果
kv	| label	| pred
---|----|-------
1:1.1 3:2.0|1.0|1.0
2:2.1 10:3.1|1.0|1.0
1:1.2 5:3.2|0.0|0.0
3:1.2 7:4.2|0.0|0.0



## 备注
该组件的输入为训练数据，输出为Fm分类模型。





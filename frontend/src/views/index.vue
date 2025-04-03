<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <!-- 首页user信息 -->
          <el-card shadow='hover'>
            <div class="userCard">
              <el-avatar :size="150" :src=imgUrl></el-avatar>
              <div class="userInfo">
                <p class="important-font">姓名：{{ useUserStore().name }}</p>
                <p class="secondary-font">角色：{{useUserStore().roles.join('/')}}</p>
              </div>
            </div>
          </el-card>
        </div>
        <div class="num graph">
          <el-card style="width: 100%;height: 265px;marginRight: 1%">
            <div style="width: 100%;height: 265px;" ref="pieEcharts">{{ initPieEcharts() }}</div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="16">
        <div class="num graph">
<!--          <el-card style="width: 34%;height: 265px;marginRight: 1%">
            <div style="width: 100%;height: 265px;" ref="pieEcharts">{{ initPieEcharts() }}</div>
          </el-card>-->
          <el-card style="width:65%;height: 265px">
            <div style="height: 265px">
              <el-calendar v-model="value"></el-calendar>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import useUserStore from "../store/modules/user.js";

export default {
  name: "Index",
  data() {
    return {
      imgUrl: useUserStore().avatar,
      value: new Date(),
    }
  }, methods: {
    useUserStore,
    //柱状图
    initBarEcharts() {
      // 新建一个promise对象
      let p = new Promise((resolve) => {
        resolve()
      })
      //然后异步执行echarts的初始化函数
      p.then(() => {
        //	此dom为echarts图标展示dom
        let myChart = echarts.init(this.$refs.barEcharts)
        let option = {
          title: {
            text: '销售表'
          },
          tooltip: {},
          legend: {
            data: ['今日销量', '昨日销量']
          },
          xAxis: {
            data: ['华为', 'vivo', 'oppo', 'ipone', '小米', '三星']
          },
          yAxis: {},
          series: [
            {
              name: '今日销量',
              type: 'bar',
              data: [5, 20, 36, 10, 10, 20]
            },
            {
              name: '昨日销量',
              type: 'bar',
              data: [10, 18, 34, 8, 12, 21]
            }
          ]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
      })
    },
    //饼图
    initPieEcharts() {
      let p = new Promise((resolve) => {
        resolve()
      })
      //然后异步执行echarts的初始化函数
      p.then(() => {
        let myChart = echarts.init(this.$refs.pieEcharts);
        let option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '0%',
            left: 'left'
          },
          series: [
            {
              name: '订单信息',
              type: 'pie',
              radius: ['20%', '65%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'left'
              },
              labelLine: {
                show: false,
              },
              data: [
                {value: 1048, name: '成交订单量'},
                {value: 735, name: '退款订单量'},
                {value: 580, name: '浏览量'},
                {value: 484, name: '加购量'},
                {value: 300, name: '预购量'}
              ]
            }
          ]
        }
        myChart.setOption(option);
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
.el-card__body {
  padding: 10px;
}

.userCard {
  height: 180px;
  display: flex;
  border-bottom: 2px solid #DCDFE6;
  border-radius: 2px;
}

.userInfo {
  width: auto;
  padding: 6% 0 0 6%;
}

.important-font {
  font-weight: 900;
  font-size: 25px;
}

.secondary-font {
  color: #909399;
}

.login-info {
  height: 40px;
  text-align: left;
  color: #909399;
}

.tableInfo {
  margin: 20px 0 0 0;
}

.OrderCard {
  margin: 0 0 30px 30px;
  border: #DCDFE6 1px solid;
  border-radius: 10px;

  i {
    width: 30%;
    line-height: 120px;
    font-size: 30px;
    color: #fff
  }

  div {
    width: 300px;
  }
}

.el-card {
  border: none;
}

.num {
  display: flex;
  flex-wrap: wrap;
}

.graph {
  margin: 15px 0 0 0;
}

.el-calendar {
  height: 265px;
}
</style>
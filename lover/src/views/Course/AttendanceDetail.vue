<template>
  <div class="body" v-if="record != null">
    <van-nav-bar
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
      title="签到详情"
      safe-area-inset-top
    >
      <template #right v-if="mode == 1 && record.abStudentNum != 0">
        <div style="color: #1989fa" @click="checkbox = !checkbox">
          {{ checkbox ? "取消" : "批量操作" }}
        </div>
      </template>
    </van-nav-bar>
    <div class="signInImgCell">
      <van-cell title="签到图片" clickable @click="onclickImgCell" is-link />
    </div>
    <div class="ABCStudentsList">
      <van-tabs v-model="mode" animated sticky>
        <van-tab :title="'已签学生(' + record.acStudentNum + '人)'">
          <van-empty
            v-if="record.acStudent.length == 0"
            :image="noStudentSrc"
            description="无到勤学生"
          />
          <van-cell-group :border="false">
            <van-cell
              v-for="(acStudent, index) in record.acStudent"
              :key="acStudent.id"
              :title="acStudent.name"
              @click="onClickacStudent(index)"
              clickable
            >
              <template #default>
                <span style="font-size: 12px">{{ acStudent.class }}</span>
              </template>
            </van-cell>
          </van-cell-group>
        </van-tab>
        <van-tab :title="'未签学生(' + record.abStudentNum + '人)'">
          <van-empty
            v-if="record.abStudent.length == 0"
            :image="noStudentSrc"
            description="无缺勤学生"
          />
          <van-notice-bar
            mode="closeable"
            wrapable
            :scrollable="false"
            v-if="registeRemind != ''"
            >{{
              registeRemind
            }},请提醒学生进行注册并上传人脸照片。</van-notice-bar
          >
          <van-cell-group v-if="!checkbox" :border="false">
            <van-cell
              v-for="(abStudent, index) in record.abStudent"
              :key="abStudent.id"
              :title="abStudent.name"
              @click="onClickabStudent(index)"
              clickable
            >
              <template #default>
                <span style="font-size: 12px">{{ abStudent.class }}</span>
              </template>
            </van-cell>
          </van-cell-group>
          <div v-if="checkbox">
            <div>
              <van-checkbox-group v-model="checkboxResult">
                <van-cell-group :border="false">
                  <van-cell
                    clickable
                    v-for="(abStudent, index) in record.abStudent"
                    :key="abStudent.id"
                    :title="abStudent.name"
                    @click="toggle(index)"
                  >
                    <template #right-icon>
                      <van-checkbox :name="index" ref="checkboxes" />
                    </template>
                  </van-cell>
                </van-cell-group>
              </van-checkbox-group>
            </div>
            <van-row>
              <van-col span="8"></van-col>
              <van-col span="8">
                <van-button
                  color="linear-gradient(to right, #f83600 0%, #f9d423 100%)"
                  block
                  round
                  @click="supply"
                >
                  补签
                </van-button></van-col
              >
              <van-col span="8">
                <van-checkbox v-model="checkAll" @click="onCheckAll"
                  >全选</van-checkbox
                >
              </van-col>
            </van-row>
            <!-- <div style="text-align: center">
              <span>
                <van-button
                  color="linear-gradient(to right, #f83600 0%, #f9d423 100%)"
                  block
                  round
                  @click="supply"
                  v-if="checkboxResult.length != 0"
                  style="width: 40%; margin-left: 20%"
                >
                  补签
                </van-button></span
              >
              <span
                ><van-checkbox v-model="checkAll">复选框</van-checkbox></span
              >
            </div> -->
          </div>
        </van-tab>
      </van-tabs>
    </div>
    <van-action-sheet
      v-model="showacStudentInfo"
      title="学生信息"
      position="bottom"
    >
      <van-cell-group v-if="record.acStudent.length != 0">
        <van-cell title="姓名" :value="record.acStudent[acStudentIndex].name" />
        <van-cell
          title="班级"
          :value="record.acStudent[acStudentIndex].class"
        />
        <van-cell title="学号" :value="record.acStudent[acStudentIndex].id" />
      </van-cell-group>
    </van-action-sheet>
    <van-action-sheet
      v-model="showabStudentInfo"
      title="学生信息"
      position="bottom"
    >
      <van-cell-group v-if="record.abStudent.length != 0">
        <van-cell title="姓名" :value="record.abStudent[abStudentIndex].name" />
        <van-cell
          title="班级"
          :value="record.abStudent[abStudentIndex].class"
        />
        <van-cell title="学号" :value="record.abStudent[abStudentIndex].id" />
      </van-cell-group>
      <div style="height: 5px"></div>
      <van-button
        type="danger"
        style="width: 60%; margin-left: 20%; padding: 5px"
        round
        @click="supply"
      >
        补签
      </van-button>
      <div style="height: 10px"></div>
    </van-action-sheet>
    <van-image-preview v-model="showImagePreview" :images="signInImg">
    </van-image-preview>
  </div>
</template>

<script>
export default {
  props: ["record"],
  data() {
    return {
      //record: [],
      mode: 0,
      checkbox: false,
      checkboxResult: [],
      showacStudentInfo: false,
      showabStudentInfo: false,
      acStudentIndex: 0,
      abStudentIndex: 0,
      noStudentSrc: require("@/assets/course/noStudent.png"),
      showImagePreview: false,
      signInImg: [],
      checkAll: false,
      registeRemind: "",
    };
  },
  watch: {
    checkboxResult(newVal, oldVal) {
      if (newVal.length < this.record.abStudentNum) {
        this.checkAll = false;
      }
    },
    record(newVal, oldVal) {
      if (newVal != null && oldVal == null) {
        console.log(this.record);
        const _this = this;
        //获得签到图片  this.record.attendanceId
        var axios = require("axios");
        var config = {
          method: "get",
          url:
            this.GLOBAL.port +
            "/teacher/getSignPictures?id=" +
            this.record.attendanceId,
          headers: {},
        };
        axios(config)
          .then(function (response) {
            for (let i = 0; i < response.data.length; i++) {
              _this.signInImg.push("data:image/jpg;base64," + response.data[i]);
            }
          })
          .catch(function (error) {
            console.log(error);
          });
        //查找是否有未注册学生
        for (let i = 0; i < this.record.abStudent.length; i++) {
          if (this.record.abStudent[i].class == "未知班级") {
            this.registeRemind += this.record.abStudent[i].name;
          }
        }
      }
    },
  },
  mounted() {
    if (this.record != null) {
      console.log(this.record);
      const _this = this;
      //获得签到图片  this.record.attendanceId
      var axios = require("axios");
      var config = {
        method: "get",
        url:
          this.GLOBAL.port +
          "/teacher/getSignPictures?id=" +
          this.record.attendanceId,
        headers: {},
      };
      axios(config)
        .then(function (response) {
          for (let i = 0; i < response.data.length; i++) {
            _this.signInImg.push("data:image/jpg;base64," + response.data[i]);
          }
        })
        .catch(function (error) {
          console.log(error);
        });
      //查找是否有未注册学生
      for (let i = 0; i < this.record.abStudent.length; i++) {
        if (this.record.abStudent[i].class == "未知班级") {
          this.registeRemind += this.record.abStudent[i].name;
        }
      }
    }
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
    toggle(index) {
      this.$refs.checkboxes[index].toggle();
    },
    onClickacStudent(index) {
      this.showacStudentInfo = true;
      this.acStudentIndex = index;
    },
    onClickabStudent(index) {
      this.showabStudentInfo = true;
      this.abStudentIndex = index;
    },
    onclickImgCell() {
      // if (this.signInImg.length == 0) {
      //   //this.$toast("获取图片失败");
      // } else {
      //   this.showImagePreview = true;
      // }
      this.showImagePreview = true;
    },
    supply() {
      let studentIds = [];
      if (this.checkbox == true) {
        //多选补签
        for (let i = 0; i < this.checkboxResult.length; i++) {
          studentIds.push(this.record.abStudent[this.checkboxResult[i]].id);
        }
        this.checkboxResult = [];
      } else {
        //单人补签
        studentIds.push(this.record.abStudent[this.abStudentIndex].id);
        this.abStudentIndex = 0;
      }
      let _this = this;
      this.$dialog
        .confirm({
          title: "确认进行补签",
        })
        .then(() => {
          //修改为多人补签接口   参数studentIds，_this.record.attendanceId
          var axios = require("axios");
          var FormData = require("form-data");
          var data = new FormData();
          for (var id = 0; id < studentIds.length; id++) {
            data.append("studentIds", studentIds[id]);
          }
          data.append("attendanceId", _this.record.attendanceId);

          var config = {
            method: "post",
            url: this.GLOBAL.port + "/teacher/supply",
            headers: {
              "Content-Type": "multipart/form-data",
            },
            data: data,
          };

          axios(config)
            .then(function (response) {
              //成功后
              for (let i = 0; i < _this.record.abStudent.length; i++) {
                if (studentIds.indexOf(_this.record.abStudent[i].id) != -1) {
                  _this.record.acStudent.push(
                    _this.record.abStudent.splice(i, 1)[0]
                  );
                  _this.record.acStudentNum++;
                  _this.record.abStudentNum--;
                  i--;
                }
              }
              _this.$toast("补签成功!");
              _this.checkbox = false;
              _this.showabStudentInfo = false;
            })
            .catch(function (error) {
              console.log(error);
            });
        })
        .catch(() => {
          // on cancel
        });
      // if(this.supply())
    },
    onCheckAll() {
      if (this.checkAll == true) {
        var temp = [];
        for (let i = 0; i < this.record.abStudentNum; i++) {
          temp.push(i);
        }
        this.checkboxResult = temp;
      } else {
        this.checkboxResult = [];
      }
    },
  },
};
</script>

<style lang="less" scoped>
.signInImgCell {
  position: relative;
  top: 1vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  animation-duration: 0.8s;
}
.ABCStudentsList {
  position: relative;
  top: 2vh;
  left: 0;
  right: 0;
  margin: 0 5px;
  background: #fff;
  padding: 5px;
  box-sizing: border-box;
  box-shadow: 0 0 24px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  animation-duration: 0.8s;
}
.van-row {
  height: 50px;
  .van-col {
    height: 50px;
    .van-button {
      margin-top: 5px;
    }
    .van-checkbox {
      padding: 15px;
    }
  }
}
</style>
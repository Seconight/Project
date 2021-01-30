<template>
  <div>
    <van-row>
      <van-col span="6">
        <img :src="avatarSrc" alt="" />
      </van-col>
      <van-col span="10" @click="openLoginModal">{{ name }}</van-col>
      <van-col span="8" @click="logout">
        <van-icon name="cross" />
      </van-col>
    </van-row>
    <div v-if="role == '老师'">
      <van-cell-group title="个人信息">
        <van-cell title="身份" :value="role" />
        <van-cell title="工号" :value="id" />
        <van-cell title="邮箱" :value="address" />
      </van-cell-group>
    </div>
    <div v-if="role == '学生'">
      <van-cell-group title="个人信息">
        <van-cell title="身份" :value="role" />
        <van-cell title="学号" :value="id" />
        <van-cell title="班级" :value="class_" />
        <van-cell title="邮箱" :value="address" />
      </van-cell-group>
      <van-cell-group title="人脸信息">
        <van-cell
          title="人脸状态"
          :value="'checkFace' ? '人脸已上传' : '人脸未上传'"
        />
      </van-cell-group>
      <div style="text-align: center;padding:20px">
        <van-uploader
          :preview-options="preview_options"
          v-model="imgList"
          multiple
          accept="image/*"
          preview-size="110px"
          :max-count="3"
        />
      </div>
      <div style="text-align: center;">
        <van-button round type="info" @click="uploadface" 
          >上传人脸</van-button
        >
      </div>
    </div>
    <div class="login_modal" v-if="showLoginModal">
      <section @click="closeLoginModal"></section>
      <van-form @submit="onSubmit">
        <!-- <div class="radio">
          <van-radio-group
            v-model="identity"
            direction="horizontal"
            icon-size="30px"
          >
            <van-radio name="1">老师</van-radio>
            <van-radio name="2">学生</van-radio>
          </van-radio-group>
        </div> -->
        <van-field
          v-model="username"
          name="用户名"
          label="用户名"
          placeholder="用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
        />
        <van-field
          v-model="password"
          type="password"
          name="密码"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
        />
        <div style="margin: 16px">
          <van-button round block type="info" native-type="submit"
            >提交</van-button
          >
        </div>
      </van-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      name: "点击登录",
      avatarSrc: require("@/assets/defualt.jpg"),
      //identity: '1',
      username: "",
      password: "",
      showLoginModal: false,
      role: "",
      id: "",
      address: "",
      class_: "",
      checkFace: false,
      imgList: [],
      preview_options: {
        closeable: true,
      },
    };
  },
  created() {
    //进入user页面判断是否登录
    let userInfo = localStorage.getItem("userInfo");
    if (userInfo) {
      //存在已登录信息
      this.showinfor(); //更新组件信息显示
    }
  },
  methods: {
    onSubmit(values) {
      this.username = values["用户名"];
      this.password = values["密码"];
      //登录逻辑 从接口判断登录是否成功



      //假数据
      //if (this.username == "123" && this.password == "123") {
      if (this.username != "" && this.password != "") {
        //定义userInfo
        let userInfo;
        console.log("this is password:"+this.password);
        //调用接口，返回查询数据
        var axios = require('axios');
        var data = JSON.stringify({"id":this.username,"pwd":this.password});
        
        var config = {
        method: 'post',
        url: 'http://localhost:8081/user/login',
        headers: { 
          'Content-Type': 'application/json'
        },
          data : data
        };

        axios(config)
        .then(function (response) {
          let info = JSON.stringify(response.data);
          console.log(JSON.stringify(response.code));
          console.log(JSON.stringify(response.data));
          //localStorage.setItem("userInfo",JSON.stringify(response.data));
          userInfo = JSON.stringify(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });

        // let userInfo;
        // if (this.username == "123")
        //   userInfo = {
        //     id: "012181088",
        //     name: "田家兴",
        //     class: null,
        //     address: "111@163.com",
        //     role: "老师", //切换 老师/学生
        //   };
        // else
        //   userInfo = {
        //     id: "012181088",
        //     name: "田家兴",
        //     class: "计算机1801",
        //     address: "111@163.com",
        //     role: "学生", //切换 老师/学生
        //   };
        //通过接口获取人脸是否注册
        this.checkFace = true;

        let _userInfo = JSON.stringify(userInfo);
        localStorage.setItem("userInfo", _userInfo);
        localStorage.setItem("checkFace", this.checkFace);
        this.$toast.success("登陆成功");
        this.showinfor(); //更新组件信息显示
        this.closeLoginModal();
      } else {
        //账户密码错误
        this.$toast.fail("账号密码错误");
        this.username = "";
        this.password = "";
      }
    },
    //组件个人信息显示
    showinfor() {
      let _userInfo = JSON.parse(localStorage.getItem("userInfo"));
      this.role = _userInfo.role;
      this.name = _userInfo.name;
      this.id = _userInfo.id;
      this.address = _userInfo.address;
      if (this.role == "老师") {
        this.avatarSrc = require("@/assets/teacher.jpg");
        this.id;
      }
      if (this.role == "学生") {
        this.avatarSrc = require("@/assets/student.jpg");
        this.class_ = _userInfo.class;
        this.checkFace = localStorage.getItem("checkFace");
      }
    },
    //打开登录模态框
    openLoginModal() {
      if (!localStorage.getItem("userInfo")) {
        this.showLoginModal = true;
      }
    },
    //关闭登录模块框
    closeLoginModal() {
      this.showLoginModal = false;
    },
    //上传人脸
    uploadface() {
      this.$dialog
        .confirm({
          title: "确定上传人脸数据",
        })
        .then(() => {
          // on confirm
          //上传人脸数据
          console.log(this.imgList);
          this.$toast.success("上传成功");
          this.imgList = [];
        })
        .catch(() => {
          // on cancel
        });
    },
    //退出登录事件
    logout() {
      this.$dialog
        .confirm({
          title: "确定要退出登录吗？",
        })
        .then(() => {
          // on confirm
          if (!localStorage.getItem("userInfo")) {
            this.$toast.fail("请先登录");
            return;
          }
          localStorage.removeItem("userInfo");
          localStorage.removeItem("checkFace");
          this.name = "点击登录";
          this.avatarSrc = require("@/assets/defualt.jpg");
          this.username = "";
          this.password = "";
          this.showLoginModal = false;
          this.role = "";
          this.id = "";
          this.address = "";
          this.class_ = "";
          this.checkFace = false;
          this.imgList = [];
          this.preview_options.closeable = true;
        })
        .catch(() => {
          // on cancel
        });
    },
  },
};
</script>

<style lang='less' scoped>
.van-row {
  height: 100px;
  padding: 10px;
  background: linear-gradient(to right, #ff0000, #000000);
  color: #fff;
  .van-col {
    height: 100px;
    line-height: 100px;
    font-size: 20px;
    text-align: center;
    img {
      width: 100px;
      border-radius: 50%;
    }
    &:last-child {
      text-align: right;
    }
  }
}
.login_modal {
  width: 100%;
  height: 100vh;
  position: absolute;
  left: 0;
  top: 0;
  z-index: 99;
  section {
    width: 100%;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
  }
  .van-form {
    width: 90%;
    position: absolute;
    top: 30vh;
    left: 5%;
    padding: 10px;
    box-sizing: border-box;
    background: #fff;
  }
}
</style>
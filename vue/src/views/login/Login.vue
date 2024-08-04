<template>
    <div style="height: 100vh;overflow: hidden;position: relative" id="building">
        <el-card class="cover" v-if="loginAdmin.id">
            <slide-verify :l="42"
                          :r="10"
                          :w="310"
                          :h="155"
                          :imgs="imageList"
                          slider-text="向右滑动"
                          @success="onSuccess"
                          @fail="onFail"
                          @refresh="onRefresh"
            ></slide-verify>
        </el-card>

        <div style="width: 400px;height: 370px;background-color:rgba(255,255,255,0.4);border-radius: 10px;margin: 150px auto;padding:50px">
            <div style="width: 300px">
                <img src="@/assets/logo.png" style="width: 40px;position: relative;top: 10px;left: 20px">
                <span style="margin-left: 25px;font-size:24px">图 书 管 理 系 统</span>
            </div>
            <div style="margin: 30px;text-align: center;front-size:30px;font-size:20px;">欢 迎 登 录</div>
            <el-form :model="admin" :rules="rules" ref="loginForm">
                <el-form-item prop="username">
                    <el-input placeholder="请输入用户名" prefix-icon="el-icon-user" size="medium"
                              v-model="admin.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input placeholder="请输入密码" show-password prefix-icon="el-icon-lock" size="medium"
                              v-model="admin.password"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-button style="width: 100%" size="medium" type="primary" @click="login">登录</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>

</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'


export default {
    name: "Login",
    data() {
        return {
            imageList: [
                require('@/assets/01.jpg'),
                require('@/assets/02.jpg'),
                require('@/assets/03.jpg'),
                require('@/assets/04.jpg'),
                require('@/assets/05.jpg')
            ],
            loginAdmin: {},
            admin: {},
            rules: {
                username: [
                    {required: true, message: '请输入用户名', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
                ],

            }
        }
    },
    methods: {
        login() {
            this.$refs["loginForm"].validate((valid) => {
                if (valid) {
                    request.post('/admin/login', this.admin).then(res => {
                        if (res.code == '200') {
                            this.loginAdmin = res.data //出现滑块组件
                        } else {
                            this.$notify.error(res.msg)
                        }
                    })
                }
            })

        },
        onSuccess() { //滑块验证通过后
            Cookies.set('admin', JSON.stringify(this.loginAdmin))
            this.$notify.success("登陆成功")
            this.$router.push('/')
        },
        onFail() {
            console.log('onFail')

        },
        onRefresh() {
            console.log('refresh')
        }

    }
}
</script>


<style>
.cover {
    width: fit-content;
    background-color: white;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1000;
}

#building {
    background: url("@/assets/bg.jpg");
    width: 100%;
    height: 100%;
    position: fixed;
    background-size: 100% 100%;
}


</style>
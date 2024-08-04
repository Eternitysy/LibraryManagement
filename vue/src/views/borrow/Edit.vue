<template>
    <div style="width: 100%">
        <div style="padding: 0 20px">
            <h3>编辑用户</h3>
        </div>
        <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="图书编号" prop="bookNo">
                <el-select v-model="form.bookNo" clearable filterable placeholder="请选择图书编号" @change="selBook">
                    <el-option
                            v-for="item in books"
                            :key="item.id"
                            :label="item.bookNo"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="书名" prop="bookName">
                <el-input v-model="form.bookName" disabled></el-input>
            </el-form-item>
            <el-form-item label="所需积分" prop="score">
                <el-input v-model="form.score" disabled></el-input>
            </el-form-item>
            <el-form-item label="图书数量" prop="score">
                <el-input v-model="form.nums" disabled></el-input>
            </el-form-item>
            <br/>
            <el-form-item label="用户ID" prop="userId">
                <el-select v-model="form.userId" clearable filterable placeholder="请选择用户ID" @change="selUser">
                    <el-option
                            v-for="item in users"
                            :key="item.id"
                            :label="item.cardID"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="用户姓名" prop="userName">
                <el-input v-model="form.userName" disabled></el-input>
            </el-form-item>

            <el-form-item label="用户联系电话" prop="userPhone">
                <el-input v-model="form.userPhone" disabled></el-input>
            </el-form-item>
            <el-form-item label="用户账户余额" prop="account">
                <el-input v-model="form.account" disabled></el-input>
            </el-form-item>

        </el-form>

        <div style="text-align: center;margin-top: 40px">
            <el-button type="primary" @click="save" size="medium">提交</el-button>
            <!--<el-button type="danger">取消</el-button>-->
        </div>
    </div>

</template>

<script>
import request from "@/utils/request";

export default {
    name: "EditBorrow",
    data() {
        const checkPhone = (rule, value, callback) => {
            if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
                callback(new Error('该手机号不合法'));
            }
            callback()
        };
        return {
            form: {},
            rules: {
                name: [
                    {required: true, message: '请输入书名', trigger: 'blur'},
                ],
                phone: [
                    {validator: checkPhone, trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        save() {
            request.put('/borrow/update', this.form).then(res => {
                if (res.code == '200') {
                    this.$notify.success('修改成功')
                    this.$router.push("/borrowList")
                } else {
                    this.$notify.error(res.msg)
                }
            })

        },
        selBook() {
            const book = this.books.find(v => v.bookNo = this.form.bookNo)
            request.get('/book/' + book.id).then(res => {
                this.form.bookName = res.data.name
                this.form.score = res.data.score
                this.form.nums = res.data.nums
            })
        },
        selUser() {
            const user = this.users.find(v => v.cardID = this.form.userId)
            request.get('/user/' + user.id).then(res => {
                this.form.userName = res.data.name
                this.form.userPhone = res.data.phone
                this.form.score = res.data.account
            })

        },
    }
}
</script>

<style scoped>

</style>
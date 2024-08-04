<template>
    <div style="width: 100%">
        <div style="padding: 0 20px">
            <h3>新增图书</h3>
        </div>
        <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
            <el-form-item label="书名" prop="name">
                <el-input v-model="form.name" placeholder="请输入书名"></el-input>
            </el-form-item>
            <el-form-item label="作者" prop="author">
                <el-input v-model="form.author" placeholder="请输入作者"></el-input>
            </el-form-item>
            <el-form-item label="图书编号" prop="bookNo">
                <el-input v-model="form.bookNo" placeholder="请输入图书编号"></el-input>
            </el-form-item>
            <el-form-item label="出版商" prop="publisher">
                <el-input v-model="form.publisher" placeholder="请输入出版商"></el-input>
            </el-form-item>
            <el-form-item label="出版日期" prop="publishDate">
                <el-date-picker
                        v-model="form.publishDate"
                        type="date"
                        placeholder="请选择出版日期">
                </el-date-picker>
            </el-form-item>

            <el-form-item label="分类" prop="category">
                <el-cascader
                        style="width: 200px"
                        :props="{value:'name',label:'name'}"
                        v-model="form.categories"
                        :options="categories"
                        @change="handleChange"></el-cascader>
            </el-form-item>
            <el-form-item label="数量" prop="nums">
                <el-input-number v-model="form.nums" :min="1" label="数量"></el-input-number>
            </el-form-item>
            <el-form-item label="所需积分" prop="score">
                <el-input-number v-model="form.score" :min="10" :max="30" label="所需积分"></el-input-number>
            </el-form-item>
            <br>
            <el-form-item label="封面" prop="cover">
                <el-upload
                        class="avatar-uploader"
                        :action="'http://localhost:9090/api/book/file/upload?token='+this.admin.token"
                        :show-file-list="false"
                        :on-success="handleCoverSuccess">
                    <img v-if="form.cover" :src="form.cover" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-form-item label="内容简介" prop="description">
                <el-input style="width: 250px" type="textarea" v-model="form.description"
                          placeholder="请输入描述"></el-input>
            </el-form-item>
        </el-form>

        <div style="text-align: center;margin-top: 40px">
            <el-button type="primary" @click="save" size="medium">提交</el-button>
        </div>
    </div>

</template>

<script>
import request from "@/utils/request";
import Cookies from "js-cookie";

export default {
    name: "AddBook",
    data() {
        return {
            admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
            form: {score: 10, nums: 5, cover: ''},
            categories: [],
            rules: {
                name: [
                    {required: true, message: '请输入书名', trigger: 'blur'},
                ],
                bookNo: [
                    {required: true, message: '请输入图书编号', trigger: 'blur'},
                ],
                score: [
                    {required: true, message: '请输入借书积分', trigger: 'blur'},
                ],
                nums: [
                    {required: true, message: '请输入借书积分', trigger: 'blur'},
                ],
            }
        }
    },
    created() {
        request.get('/category/tree').then(res => {
            this.categories = res.data
        })
    },
    methods: {
        handleCoverSuccess(res) {
            if (res.code === '200')
                this.form.cover = res.data
            console.log(res.data)

        },
        save() {
            this.$refs["ruleForm"].validate((valid) => {
                if (valid) {
                    request.post('/book/save', this.form).then(res => {
                        if (res.code == '200') {
                            this.$notify.success('新增成功')
                            this.$refs["ruleForm"].resetFields();

                        } else {
                            this.$notify.error(res.msg)
                        }

                    })
                }


            })

        },
        handleChange(val) {
            console.log(val)
        }
    }
}
</script>

<style>
.avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>
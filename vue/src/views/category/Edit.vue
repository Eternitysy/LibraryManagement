<template>
    <div style="width: 100%">
        <div style="padding: 0 20px">
            <h3>编辑分类</h3>
        </div>
        <el-form :inline="true" :rules="rules" ref="ruleForm" :model="form" label-width="68px">
            <el-form-item label="名称">
                <el-input v-model="form.name" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
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
    name: "EditCategory",
    data() {
        return {
            form: {},
            rules: {
                name: [
                    {required: true, message: '请输入种类名称', trigger: 'blur'},
                    {min: 3, max: 10, message: '长度在3-10个字符', trigger: 'blur'}
                ],
            }
        }
    },
    created() {
        const id = this.$route.query.id
        request.get("/category/" + id).then(res => {
            this.form = res.data
        })

    },
    methods: {
        save() {
            request.put('/category/update', this.form).then(res => {
                if (res.code == '200') {
                    this.$notify.success('修改成功')
                    this.$router.push("/categoryList")
                } else {
                    this.$notify.error(res.msg)
                }

            })

        }
    }
}
</script>

<style scoped>

</style>
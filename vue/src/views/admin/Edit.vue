<template>
    <div style="width: 100%">
        <div style="padding: 0 20px">
            <h3>编辑用户</h3>
        </div>
        <el-form :inline="true" :model="form" label-width="68px">
<!--            <el-form-item label="姓名">-->
<!--                <el-input v-model="form.name" placeholder="请输入姓名"></el-input>-->
<!--            </el-form-item>-->
            <el-form-item label="用户名">
                <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="联系方式">
                <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
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
    name: "Edit",
    data() {
        return {
            form: {}
        }
    },
    created() {
        const id=this.$route.query.id
        request.get("/admin/"+id).then(res=>{
            this.form=res.data
        })

    },
    methods:{
        save(){
            request.put('/admin/update',this.form).then(res=>{
                if(res.code=='200'){
                    this.$notify.success('修改成功')
                    this.$router.push("/admin")
                }
                else{
                    this.$notify.error(res.msg)
                }

            })

        }
    }
}
</script>

<style scoped>

</style>